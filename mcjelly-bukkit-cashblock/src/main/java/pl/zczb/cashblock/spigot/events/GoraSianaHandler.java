package pl.zczb.cashblock.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.objects.impl.GoraSianaBlockManager;
import pl.zczb.cashblock.spigot.events.gorasiana.GoraSianaManager;
import pl.zczb.helpers.GlobalHelper;

import java.util.HashMap;
import java.util.Map;

public class GoraSianaHandler implements Listener {
    private final GoraSianaManager manager;
    private final Cashblock plugin;
    public static Map<Player, BossBar> bossbars = new HashMap<>();

    public GoraSianaHandler(Cashblock plugin, GoraSianaManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        boolean inAny = isInAnyRegion(player);

        BossBar bar = bossbars.get(player);

        if (inAny && bar == null) {
            BossBar bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&#00FF79⛏ | &fJesteś na terenie &cnieaktywnego &feventu &#00FF79&lGóra Siana"), BarColor.RED, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);

            boolean inCurrent = (GoraSianaManager.getCurrentLocation() != null && OtherHelper.isInRegion(player, GoraSianaManager.getCurrentLocation().getRegionId()));
            if (inCurrent) {
                bossBar.setTitle(GlobalHelper.fixColor("&#00FF79⛏ | &fJesteś na terenie &aaktywnego &feventu &#00FF79&lGóra Siana"));
                bossBar.setColor(BarColor.YELLOW);
            }
            bossBar.addPlayer(player);
            bossbars.put(player, bossBar);
        } else if (!inAny && bar != null) {
            bar.removeAll();
            bossbars.remove(player);
        }
    }

    private boolean isInAnyRegion(Player player) {
        return (OtherHelper.isInRegion(player, "gora_event_break1") ||
                OtherHelper.isInRegion(player, "gora_event_break2") ||
                OtherHelper.isInRegion(player, "gora_event_break3"));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getBlock().getLocation();

        if (!isInAnyRegion(player))
            return;
        if (!GoraSianaBlockManager.isSpecialBlock(loc))
            return;
        event.setDropItems(false);
        double value = GoraSianaBlockManager.getValue(loc);

        event.getBlock().setType(Material.AIR);
        GoraSianaBlockManager.removeBlock(loc);

        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(player);
        u.addPln(value);
        player.sendTitle(GlobalHelper.fixColor("&#00FF79&lEVENT"), GlobalHelper.fixColor(" &fWykopałeś &8(&a" + value + " vPLN&8) &f"));

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.equals(player))
                p.sendTitle(GlobalHelper.fixColor("&#00FF79&lEVENT"), GlobalHelper.fixColor(player.getName() + " &7wykopał &8(&a" + player.getName() + " vPLN&8) &e&lGZ!"));
        }
    }
}


