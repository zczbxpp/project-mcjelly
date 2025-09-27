package pl.zczb.lobby.queue;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Lobby;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.lobby.helpers.Items;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;

import java.util.*;

public class QueueInstance {

    private final String name;
    private final LinkedList<UUID> players = new LinkedList<>();
    private final Map<UUID, BossBarHandler> bossBars = new HashMap<>();

    public QueueInstance(String name) {
        this.name = name;
    }

    public void addPlayer(Player player) {
        if (isInQueue(player)) return;
        players.add(player.getUniqueId());
        bossBars.put(player.getUniqueId(), new BossBarHandler(player, this));
        giveLeaveItem(player);
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        if (bossBars.containsKey(player.getUniqueId())) {
            bossBars.get(player.getUniqueId()).remove();
            bossBars.remove(player.getUniqueId());
        }
        resetItem(player);
    }

    public void tryJoinServer() {
        if (players.isEmpty()) return;

        UUID firstUUID = players.peek();
        Player first = Bukkit.getPlayer(firstUUID);
        if (first == null || !first.isOnline()) {
            players.poll();
            return;
        }

        Sector best = SectorManager.getBestSector(name);
        if (best == null) {
            first.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cBrak dostępnego sektora. Oczekiwanie..."));
            return;
        }

        SectorManager.sendToServer(first, best.getSectorName());
        first.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPróba łączenia z sektorem &2" + best.getSectorName()));
        new BukkitRunnable() {
            @Override
            public void run() {
                Player stillHere = Bukkit.getPlayer(firstUUID);
                if (stillHere != null && stillHere.isOnline()) {
                } else {
                    players.poll();
                    removePlayer(first);
                }
            }
        }.runTaskLater(Lobby.getInstance(), 40L);
    }


    public boolean isInQueue(Player player) {
        return players.contains(player.getUniqueId());
    }

    public int getPosition(Player player) {
        return new ArrayList<>(players).indexOf(player.getUniqueId()) + 1;
    }

    public int getSize() {
        return players.size();
    }

    public String getName() {
        return name;
    }

    public void updateBossBars() {
        for (UUID uuid : players) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && bossBars.containsKey(uuid)) {
                bossBars.get(uuid).update(this);
            }
        }
    }

    private void giveLeaveItem(Player p) {
        ItemStack leaveItem = new ItemStack(Material.REDSTONE);
        ItemMeta meta = leaveItem.getItemMeta();
        meta.setDisplayName("§cKliknij aby opuścić kolejkę");
        leaveItem.setItemMeta(meta);
        p.getInventory().setItem(4, leaveItem);
    }

    private void resetItem(Player p) {
        p.getInventory().setItem(4, Items.compass());
    }
}
