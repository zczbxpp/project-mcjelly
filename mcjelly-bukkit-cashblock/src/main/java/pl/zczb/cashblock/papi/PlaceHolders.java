package pl.zczb.cashblock.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.boss.BossDamage;
import pl.zczb.cashblock.boss.BossHandler;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.helpers.GlobalHelper;

import java.text.DecimalFormat;
import java.util.List;


public class PlaceHolders extends PlaceholderExpansion {
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public String onPlaceholderRequest(Player player, String identifier) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(player);


        if (identifier.equalsIgnoreCase("vpln")) {
            double plnValue = u.getPln();
            return GlobalHelper.fixColor(decimalFormat.format(plnValue));
        }

        if (identifier.equalsIgnoreCase("lvl")) {
            return GlobalHelper.fixColor(String.valueOf(u.getUserLvl().getLvl()));
        }

        if (identifier.equalsIgnoreCase("prestiz")) {
            return GlobalHelper.fixColor(String.valueOf(u.getUserPrestiz().getPrestiz()));
        }

        if (identifier.equalsIgnoreCase("stone")) {
            return GlobalHelper.fixColor(String.valueOf(OtherHelper.formatNumber(u.getStone())));
        }

        if (identifier.equalsIgnoreCase("doubler")) {
            return GlobalHelper.fixColor(String.valueOf(decimalFormat.format(u.getMnoznik())));
        }

        if (Cashblock.getCashblockConfig().getSector_name().equals("cashblock_event")) {

            List<BossDamage> list = BossHandler.getSortedDamageList(BossHandler.BossType.GOLEM);
            List<BossDamage> list1 = BossHandler.getSortedDamageList(BossHandler.BossType.PINIATA);

            Player first = (list.size() > 0) ? Bukkit.getPlayer(((BossDamage) list.get(0)).getUniqueId()) : ((list1.size() > 0) ? Bukkit.getPlayer(((BossDamage) list1.get(0)).getUniqueId()) : null);
            Player second = (list.size() > 1) ? Bukkit.getPlayer(((BossDamage) list.get(1)).getUniqueId()) : ((list1.size() > 1) ? Bukkit.getPlayer(((BossDamage) list1.get(1)).getUniqueId()) : null);
            Player third = (list.size() > 2) ? Bukkit.getPlayer(((BossDamage) list.get(2)).getUniqueId()) : ((list1.size() > 2) ? Bukkit.getPlayer(((BossDamage) list1.get(2)).getUniqueId()) : null);


            int firstDamage = (list.size() > 0) ? ((BossDamage) list.get(0)).getDamage() : ((list1.size() > 0) ? ((BossDamage) list1.get(0)).getDamage() : 0);
            int secondDamage = (list.size() > 1) ? ((BossDamage) list.get(1)).getDamage() : ((list1.size() > 1) ? ((BossDamage) list1.get(1)).getDamage() : 0);
            int thirdDamage = (list.size() > 2) ? ((BossDamage) list.get(2)).getDamage() : ((list1.size() > 2) ? ((BossDamage) list1.get(2)).getDamage() : 0);


            switch (identifier.toLowerCase()) {
                case "first_boss":
                    return (first != null) ?
                            GlobalHelper.fixColor(first.getName() + " &8(&7" +firstDamage + "&8)") :
                            "Brak";
                case "second_boss":
                    return (second != null) ?
                            GlobalHelper.fixColor(second.getName() + " &8(&7" +secondDamage + "&8)") :
                            "Brak";
                case "third_boss":
                    return (third != null) ?
                            GlobalHelper.fixColor(third.getName() + " &8(&7" + thirdDamage+ "&8)") :
                            "Brak";
                case "health_boss":
                    return String.valueOf((BossHandler.getBossHealth(BossHandler.BossType.GOLEM) > 0) ? Integer.valueOf(BossHandler.getBossHealth(BossHandler.BossType.GOLEM)) : ((BossHandler.getBossHealth(BossHandler.BossType.PINIATA) > 0) ? Integer.valueOf(BossHandler.getBossHealth(BossHandler.BossType.PINIATA)) : "0"));
                case "name_boss":
                    return String.valueOf((BossHandler.getBossHealth(BossHandler.BossType.GOLEM) > 0) ? "&#0046FFL&#1356FFo&#2567FFd&#3877FFo&#4A87FFw&#5D97FFy &#82B8FFG&#94C8FFo&#A7D8FFl&#B9E9FFe&#CCF9FFm" : ((BossHandler.getBossHealth(BossHandler.BossType.PINIATA) > 0) ? "&#E9EA8EP&#DAEA9Di&#CBEAADn&#BCEABCi&#ACEACBa&#9DEADBt&#8EEAEAa" : "Brak"));
            }


        }
        return null;
    }


    @NotNull
    public String getIdentifier() {
        return "tryb";
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


