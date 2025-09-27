package pl.zczb.cashblock.helpers;

import org.bukkit.entity.Player;

public class GroupHelper {
    public static String getGroupSymbol(Player p) {
        if (p.hasPermission("zczb.root"))
            return "";
        if (p.hasPermission("zczb.admin"))
            return "";
        if (p.hasPermission("zczb.mod"))
            return "";
        if (p.hasPermission("zczb.helper"))
            return "";
        if (p.hasPermission("zczb.media"))
            return "";
        if (p.hasPermission("zczb.donator"))
            return "";
        if (p.hasPermission("zczb.mvip"))
            return "";
        if (p.hasPermission("zczb.svip"))
            return "";
        if (p.hasPermission("zczb.vip")) {
            return "";
        }
        return "";
    }
}


