package pl.zczb.cashblock.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.objects.impl.TurboManager;
import pl.zczb.cashblock.spigot.commands.admin.TurboKasaCmd;
import pl.zczb.cashblock.spigot.events.gorasiana.GoraSianaManager;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.redis.channels.RedisChannel;

import java.util.Arrays;
import java.util.Set;

public class RefreshTask
        extends BukkitRunnable {
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);
            if (System.currentTimeMillis() >= TurboManager.getTurbodrop_time().longValue()) {
                TurboKasaCmd.bossBar.removePlayer(p);
            } else {
                long remainingTime = (TurboManager.getTurbodrop_time().longValue() - System.currentTimeMillis()) / 1000L;
                String timeShow = OtherHelper.formatSecs(remainingTime);
                TurboKasaCmd.bossBar.setTitle(GlobalHelper.fixColor("&#FF0000☀ | &#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(serwer) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow));
            }
            players = RedisChannel.INSTANCE.getOnlinePlayers();
            if (GoraSianaManager.timeRemainingSeconds > 0) {
                GoraSianaManager.bossBar.setTitle(GlobalHelper.fixColor("&#00FF79⛏ | &fAktualnie trwa event &#00FF79&lGóra Siana &8(&#A3FFCFx: " + GoraSianaManager.getCurrentLocation().getPasteLocation().getX() + ", y: " + GoraSianaManager.getCurrentLocation().getPasteLocation().getY() + ", z: " + GoraSianaManager.getCurrentLocation().getPasteLocation().getZ() + "&8) &8(&#00FF79&l" + OtherHelper.formatSecs(GoraSianaManager.timeRemainingSeconds) + "&8)"));
            } else {
                GoraSianaManager.bossBar.removePlayer(p);
            }
            if (u.isTurboDrop() && System.currentTimeMillis() >= u.getTurboDropTime()) {
                BossBar bar = (BossBar) TurboKasaCmd.bossbars.remove(p.getName());
                if (bar != null)
                    bar.removeAll();
                u.setTurboDrop(false);
            } else {
                long remainingTime = (u.getTurboDropTime() - System.currentTimeMillis()) / 1000L;
                String timeShow = OtherHelper.formatSecs(remainingTime);
                BossBar bar = (BossBar) TurboKasaCmd.bossbars.get(p.getName());
                if (bar != null)
                    bar.setTitle(GlobalHelper.fixColor("&#FF0000☀ | &#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow));
            }


            if (u.isAutoCx()) {
                int cobbleCount = Arrays.stream(p.getInventory().getContents())
                        .filter(item -> item != null && (item.getType() == Material.COBBLESTONE || item.getType() == Material.COBBLED_DEEPSLATE))
                        .mapToInt(ItemStack::getAmount)
                        .sum();

                if (cobbleCount < 576) {
                    return;
                }
                int toRemove = 576;
                for (int i = 0; i < p.getInventory().getSize(); i++) {
                    ItemStack item = p.getInventory().getItem(i);
                    if (item != null && (item.getType() == Material.COBBLESTONE || item.getType() == Material.COBBLED_DEEPSLATE)) {
                        int amount = item.getAmount();
                        if (amount <= toRemove) {
                            p.getInventory().clear(i);
                            toRemove -= amount;
                        } else {
                            item.setAmount(amount - toRemove);
                            break;
                        }
                    }
                }

                p.updateInventory();

                if (OtherHelper.getChance(30)) {
                    Bukkit.getScheduler().runTask(Cashblock.getInstance(), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " zwykla 1");
                    });
                }
            }

        });
    }

    public static Set<String> players;
}


