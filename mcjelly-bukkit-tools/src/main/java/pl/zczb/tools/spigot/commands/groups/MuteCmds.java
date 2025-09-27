package pl.zczb.tools.spigot.commands.groups;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.MutePacket;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RootCommand
public class MuteCmds {

    @Execute(name = "mute")
    @Permission({"zczb.helper"})
    public void mutePlayer(
            @Context Player sender,
            @Arg("gracz") UserDataModel targetUser,
            @Arg("czas_lub_typ") String timeOrType,
            @Join("wiadomosc") String reason
    ) {
        Long muteUntilTimestamp;
        String muteDurationDisplay;

        if (timeOrType.equalsIgnoreCase("perm")) {
            muteUntilTimestamp = -1L;
            muteDurationDisplay = "permanentnie";
        } else {
            try {
                Duration parsedDuration = parseDuration(timeOrType);
                long muteDurationMillis = parsedDuration.toMillis();

                if (muteDurationMillis <= 0L) {
                    sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNiepoprawny format czasu! Czas musi być większy niż zero."));
                    return;
                }
                muteUntilTimestamp = System.currentTimeMillis() + muteDurationMillis;
                muteDurationDisplay = formatDurationValue(parsedDuration);

            } catch (IllegalArgumentException e) {
                sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNieznany typ wyciszenia. Użyj 'perm' dla permanentnego lub czas (np. 1d, 5h)."));
                return;
            }
        }

        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new MutePacket("mute", targetUser.getNick(), reason, sender.getName(), muteUntilTimestamp));

        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie wyciszono gracza &2" + targetUser.getNick() + " &ana czas &2" + muteDurationDisplay + " &az powodem &2" + reason));
    }

    @Execute(name = "unmute")
    @Permission({"zczb.helper"})
    public void unmute(@Context Player sender, @Arg("gracz") UserDataModel targetUser) {
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), new MutePacket("unmute", targetUser.getNick(), null, sender.getName(), null));
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie odciszono gracza &2" + targetUser.getNick()));
    }

    private Duration parseDuration(String input) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(input.toLowerCase());
        long totalMillis = 0L;

        boolean foundMatch = false;
        while (matcher.find()) {
            foundMatch = true;
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    totalMillis += (long) value * 1000L;
                    break;
                case "m":
                    totalMillis += (long) value * 60 * 1000L;
                    break;
                case "h":
                    totalMillis += (long) value * 60 * 60 * 1000L;
                    break;
                case "d":
                    totalMillis += (long) value * 24 * 60 * 60 * 1000L;
                    break;
            }
        }

        if (!foundMatch || totalMillis <= 0L) {
            throw new IllegalArgumentException("Nieprawidłowy format czasu.");
        }
        return Duration.ofMillis(totalMillis);
    }


    public static String formatRemainingTime(Long endTimeMillis) {
        if (endTimeMillis == null || endTimeMillis == -1L) {
            return "Permanentnie";
        }

        long remainingMillis = endTimeMillis - System.currentTimeMillis();

        if (remainingMillis <= 0) {
            return "Skończył się";
        }

        Duration remainingDuration = Duration.ofMillis(remainingMillis);
        return formatDurationValue(remainingDuration);
    }


    private static String formatDurationValue(Duration duration) {
        if (duration.isZero() || duration.isNegative()) {
            return "<1s";
        }

        long totalSeconds = duration.getSeconds();
        long days = totalSeconds / (24 * 3600);
        long hours = (totalSeconds % (24 * 3600)) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d");
        if (hours > 0) sb.append(hours).append("h");
        if (minutes > 0) sb.append(minutes).append("m");
        if (seconds > 0) sb.append(seconds).append("s");

        String formatted = sb.toString();
        return formatted.isEmpty() ? "<1s" : formatted;
    }
}