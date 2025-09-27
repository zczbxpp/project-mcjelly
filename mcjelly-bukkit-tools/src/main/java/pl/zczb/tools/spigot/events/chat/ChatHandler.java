package pl.zczb.tools.spigot.events.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.ChatPacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.database.user.models.UserBans;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.helpers.GroupHelper;
import pl.zczb.tools.objects.Chat;
import pl.zczb.tools.spigot.commands.groups.MuteCmds;

import java.util.*;
import java.util.regex.Pattern;

public class ChatHandler implements Listener {
    private final Map<UUID, Long> lastMessageTime = new HashMap<>();
    private final Map<UUID, String> lastMessageContent = new HashMap<>();
    private final Map<UUID, Integer> messageCount = new HashMap<>();

    private static final Pattern UNWANTED_CHAR_PATTERN = Pattern.compile("[\\uE000-\\uE029]");

    private final List<String> blockedCommands = Arrays.asList(
            ";", "?", "about", "alerts", "br", "brush", "list", "logs", "none", "ping", "pl", "plugins",
            "recentlogs", "reloadlang", "teammsg", "tm", "toggleplace", "tool", "trigger", "ver", "version",
            "meg", "me", "w", "we", "worldedit"
    );

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        e.setCancelled(true);

        if (!Chat.globalChat && !p.hasPermission("zczb.helper")) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FChat aktualnie jest wyłączony!"));
            return;
        }
        if (Chat.vipChat && !p.hasPermission("zczb.vip")) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FChat aktualnie jest w trybie premium!"));
            return;
        }

        UserDataModel data = Tools.getInstance().getUserHandler().getPlayer(p);
        if (data == null) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FBłąd danych gracza. Skontaktuj się z administracją."));
            return;
        }

        UserBans bans = data.getUserBans();
        if (bans.isMuted()) {
            Long muteEndTime = bans.getMuteTime();

            if (muteEndTime != null && (muteEndTime == -1L || System.currentTimeMillis() < muteEndTime)) {
                String remainingTimeFormatted = MuteCmds.formatRemainingTime(muteEndTime);
                String muteReasonMessage = GlobalHelper.fixColor(
                        "&#980000Zostałeś wyciszony!\n" +
                                "&#C64949Powód: &#980000" + bans.getMuteMessage() + "\n" +
                                "&#C64949Administrator: &#980000" + bans.getMuteAdmin() + "\n" +
                                "&#C64949Czas: &#980000" + remainingTimeFormatted
                );
                p.sendMessage(muteReasonMessage);
                return;
            } else {
                bans.setMuted(false);
                bans.setMuteMessage(null);
                bans.setMuteTime(null);
                Tools.getInstance().getUserHandler().updateUser(data);
                p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Twoje wyciszenie wygasło. Możesz ponownie rozmawiać."));
            }
        }

        UUID uuid = p.getUniqueId();
        long now = System.currentTimeMillis();

        String message = UNWANTED_CHAR_PATTERN.matcher(e.getMessage()).replaceAll("")
                .replaceAll("(?i)s+[\\W_]*c+[\\W_]*a+[\\W_]*m+", "kocham ten serwer")
                .replace("<3", GlobalHelper.fixColor("&4❤&r"))
                .replaceAll("(?i)\\bgz\\b", GlobalHelper.fixColor("&b&lGZ!&r"))
                .replaceAll("(?i)\\bgg\\b", GlobalHelper.fixColor("&E&lGG&r"));

        cleanOldEntries(now);

        if (lastMessageTime.containsKey(uuid) && (now - lastMessageTime.get(uuid) < 1500L)) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz spamować! Odczekaj chwilę."));
            return;
        }

        if (lastMessageContent.containsKey(uuid) && lastMessageContent.get(uuid).equalsIgnoreCase(message)) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie możesz wysyłać identycznych wiadomości!"));
            return;
        }

        messageCount.put(uuid, messageCount.getOrDefault(uuid, 0) + 1);
        if (messageCount.get(uuid) > 4) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FWysyłasz wiadomości zbyt szybko!"));
            return;
        }

        lastMessageTime.put(uuid, now);
        lastMessageContent.put(uuid, message);

        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new ChatPacket(e.getPlayer().getName(), message, GroupHelper.getGroupSymbol(p)));
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        Set<String> hardBlockedPrefixes = new HashSet<>(Arrays.asList(
                "/plugins", "/pl", "/me", "/bukkit:plugins", "/ver", "/version",
                "/bukkit:version", "/bukkit:ver", "/bukkit:pl", "/bukkit:about",
                "/bukkit:?", "/bukkit:help", "/help", "/?"
        ));

        if (hardBlockedPrefixes.contains(message.split(" ")[0])) {
            event.setCancelled(true);
            player.sendMessage(GlobalHelper.fixColor("&cServer coded by &4zczbi"));
            return;
        }
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        if (event.getPlayer().isOp()) {
            return;
        }

        Iterator<String> iterator = event.getCommands().iterator();
        while (iterator.hasNext()) {
            String command = iterator.next();

            if (command.equals("msg") || command.equals("w") || command.equals("tell") || command.equals("r")) {
                iterator.remove();
                continue;
            }
            if (command.contains(":") && !event.getPlayer().isOp()) {
                iterator.remove();
                continue;
            }
            if (blockedCommands.contains(command)) {
                iterator.remove();
                continue;
            }

            if (command.startsWith("/") && blockedCommands.contains(command.substring(1))) {
                iterator.remove();
            }
        }
    }

    private void cleanOldEntries(long now) {
        final long CLEANUP_THRESHOLD = 5000L;

        lastMessageTime.entrySet().removeIf(entry -> {
            if (now - entry.getValue() > CLEANUP_THRESHOLD) {
                lastMessageContent.remove(entry.getKey());
                messageCount.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
}