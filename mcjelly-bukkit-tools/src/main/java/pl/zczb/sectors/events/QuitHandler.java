package pl.zczb.sectors.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.zczb.Tools;
import pl.zczb.cashblock.objects.Combat;
import pl.zczb.cashblock.objects.impl.CombatManager;
import pl.zczb.sectors.packets.subs.SectorTransferSubscriber;
import pl.zczb.tools.database.user.models.UserBans;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QuitHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) throws IOException {
        Player p = event.getPlayer();
        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        event.setQuitMessage(null);
        if (u.getJoinTime() > 0L) {
            long timeSpent = (System.currentTimeMillis() - u.getJoinTime()) / 1000L;
            u.setPlayerTime(u.getPlayerTime() + timeSpent);
            u.setJoinTime(0L);
        }

        if (u.isSprawdzany()) {
            u.setSprawdzany(false);
            UserBans bans = u.getUserBans();
            bans.setIpAddress((p.getAddress() != null) ? p.getAddress().getAddress().getHostAddress() : null);
            long muteUntil = System.currentTimeMillis() + parseDuration("30d");

            bans.setBanned(true);
            bans.setBanMessage("\n&cZostałeś zbanowany!\n&cPowód: &4Cheaty wylogowanie\n&cAdministrator: &4Automat");
            bans.setBanTime(Long.valueOf(muteUntil));
        }


        Combat combat = CombatManager.getCombat(p);
        if (combat != null && combat.hasFight()) {
            p.setHealth(0.0D);
            p.getInventory().clear();
            CombatManager.removeCombat(p);
        }


        if (!u.isChangingSector()) {

            Tools.getInstance().getUserHandler().getPlayer(p).getUserSynchro().leaveFromGame(p);
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerKick(PlayerKickEvent e) throws IOException {
        final Player p = e.getPlayer();

    }

    private long parseDuration(String input) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(input);
        long total = 0L;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    total += value * 1000L;
                    continue;
                case "m":
                    total += value * 60000L;
                    continue;
                case "h":
                    total += value * 3600000L;
                    continue;
                case "d":
                    total += value * 86400000L;
                    continue;
            }
            throw new IllegalArgumentException("Niepoprawna jednostka czasu.");
        }


        if (total == 0L) throw new IllegalArgumentException("Niepoprawny czas.");
        return total;
    }
}


