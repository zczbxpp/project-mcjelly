package pl.zczb.lobby.events;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.zczb.Lobby;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.SectorTypeEnum;
import pl.zczb.sectors.tasks.SectorUpdateTask;


public class GlobalPlaceHolder extends PlaceholderExpansion {
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("online_global")) {
            return String.valueOf(SectorUpdateTask.global_amount + SectorUpdateTask.warta_amount);
        }

        if (identifier.equalsIgnoreCase("online_cashblock")) {
            return String.valueOf(SectorManager.getTotalPlayersInMode(SectorTypeEnum.CASHBLOCK) + SectorUpdateTask.warta_amount);
        }
        if (identifier.equalsIgnoreCase("online_igrzyska")) {
            return String.valueOf(SectorManager.getTotalPlayersInMode(SectorTypeEnum.IGRZYSKA));
        }

        if (identifier.equalsIgnoreCase("sector")) {
            return String.valueOf(Lobby.getCfg().getCurrentSector().getSectorName());
        }


        return null;
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


