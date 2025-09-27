package pl.zczb.itemshop.events;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.zczb.Itemshop;
import pl.zczb.itemshop.data.user.models.UserDataModel;

import java.text.DecimalFormat;

public class GlobalPlaceHolder extends PlaceholderExpansion {
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");


    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("vpln")) {
            UserDataModel u = Itemshop.getInstance().getUserHandler().getPlayer(player);
            return String.valueOf(decimalFormat.format(u.getPln()));
        }

        return null;
    }

    @NotNull
    public String getIdentifier() {
        return "itemshop";
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


