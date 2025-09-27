package pl.zczb.tools.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.tasks.SectorUpdateTask;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.helpers.GroupHelper;
import pl.zczb.tools.helpers.OtherHelper;
import pl.zczb.tools.tops.enums.TopType;
import pl.zczb.tools.tops.impl.Top;
import pl.zczb.tools.tops.impl.TopList;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


public class CorePlaceHolder extends PlaceholderExpansion {
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public String onPlaceholderRequest(Player player, String identifier) {
        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(player);


        if (identifier.equalsIgnoreCase("sector")) {
            return GlobalHelper.fixColor(Tools.getSectorConfig().getCurrentSector().getSectorName());
        }

        if (identifier.equalsIgnoreCase("timeplayed")) {
            return GlobalHelper.fixColor(OtherHelper.formatSecs(u.getActualPlayerTime()));
        }

        if (identifier.equalsIgnoreCase("rank")) {
            return GlobalHelper.fixColor(GroupHelper.getGroupSymbolSide(player));
        }

        if (identifier.equalsIgnoreCase("online_global")) {
            return String.valueOf(SectorUpdateTask.global_amount);
        }

        if (identifier.equalsIgnoreCase("online_tryb")) {


            int cashOnline = Stream.<String>of(new String[]{"cashblock_1", "cashblock_2", "cashblock_3", "cashblock_4", "cashblock_5", "event"}).map(SectorManager::getSector).filter(Objects::nonNull).mapToInt(Sector::getPlayerCount).sum();
            return String.valueOf(cashOnline + SectorUpdateTask.warta_amount);
        }

        if (identifier.equalsIgnoreCase("warta")) {
            return String.valueOf(SectorUpdateTask.warta_amount);
        }

        if (identifier.equalsIgnoreCase("logo")) {
            return GlobalHelper.fixColor("");
        }

        if (identifier.startsWith("parkourtop")) {
            try {
                int rank = Integer.parseInt(identifier.replace("parkourtop", ""));
                if (rank >= 1 && rank <= 10) {
                    return getParkourTop(rank);
                }
            } catch (NumberFormatException numberFormatException) {
            }
        }


        if (identifier.startsWith("timeplayedtop")) {
            try {
                int rank = Integer.parseInt(identifier.replace("timeplayedtop", ""));
                if (rank >= 1 && rank <= 10) {
                    return getTimePlayedTop(rank);
                }
            } catch (NumberFormatException numberFormatException) {
            }
        }

        return null;
    }

    private String getParkourTop(int rank) {
        TopList parkourTopList = Tools.getInstance().getTopManager().getTopList(TopType.PARKOUR);
        List<Top> tops = parkourTopList.getTops();

        if (rank <= tops.size()) {
            Top topPlayer = tops.get(rank - 1);
            String formattedTime = OtherHelper.formatMillis(Long.parseLong(topPlayer.getTopValue()));
            return GlobalHelper.fixColor("&7" + rank + ". &f" + topPlayer.getNickName() + " &7(&d" + formattedTime + "&7)");
        }

        return GlobalHelper.fixColor("&7" + rank + ". &fBrak");
    }


    private String getTimePlayedTop(int rank) {
        TopList parkourTopList = Tools.getInstance().getTopManager().getTopList(TopType.TIME);
        List<Top> tops = parkourTopList.getTops();

        if (rank <= tops.size()) {
            Top topPlayer = tops.get(rank - 1);
            String formattedTime = OtherHelper.formatSecs(Long.parseLong(topPlayer.getTopValue()));
            return GlobalHelper.fixColor("&7" + rank + ". &f" + topPlayer.getNickName() + " &7(&d" + formattedTime + "&7)");
        }

        return GlobalHelper.fixColor("&7" + rank + ". &fBrak");
    }

    @NotNull
    public String getIdentifier() {
        return "core";
    }

    @NotNull
    public String getAuthor() {
        return "zczb";
    }

    @NotNull
    public String getVersion() {
        return "1.0";
    }
}


