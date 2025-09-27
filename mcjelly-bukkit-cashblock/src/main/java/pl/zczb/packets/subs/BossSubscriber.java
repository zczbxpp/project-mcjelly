package pl.zczb.packets.subs;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.boss.BossHandler;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.BossPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

public final class BossSubscriber extends RedisSubscriber<BossPacket> {
    public BossSubscriber() {
        super(BossPacket.class, "CH|cashblock_tryb");
    }


    public void onPacketReceived(BossPacket packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNa warpie pvp pojawił się boss!"));
        }

        String typeStr = packet.getType().toLowerCase();



        if(typeStr.equals("golem")) {
            BossHandler.setGolem(true);
        } else if(typeStr.equals("piniata")) {
            BossHandler.setLama(true);
        } else if(typeStr.equals("die_lama")) {
            BossHandler.setLama(false);
            return;
        } else if(typeStr.equals("die_golem")) {
            BossHandler.setGolem(false);
            return;
        }
        if (!Cashblock.getCashblockConfig().getSector_name().equalsIgnoreCase("cashblock_event"))
            return;


        int hp = packet.getAmount().intValue();

        Bukkit.getScheduler().runTask(Cashblock.getInstance(), () -> {
            MythicMob mob;
            World pvp = Bukkit.getWorld("world");
            if (pvp == null)
                return;
            Location spawnLocation = new Location(pvp, 0.0D, 28.0D, 7.0D);
            LivingEntity boss = null;
            switch (typeStr) {
                case "golem":
                    mob = MythicBukkit.inst().getMobManager().getMythicMob("golem_prismarine_gm_rain").orElse(null);
                    if (mob != null)
                        mob.spawn(BukkitAdapter.adapt(spawnLocation), 1.0D);
                    BossHandler.spawnGolemBoss(hp);
                    break;
                case "piniata":
                    boss = (LivingEntity) pvp.spawn(spawnLocation, Llama.class);
                    boss.setGlowing(true);
                    BossHandler.spawnLamaBoss((Llama) boss, hp);
                    break;
                default:
                    return;
            }
            ((BossBar) BossHandler.getBossBars().get(BossHandler.BossType.valueOf(typeStr.toUpperCase()))).removeAll();
            for (Player player : Bukkit.getOnlinePlayers())
                ((BossBar) BossHandler.getBossBars().get(BossHandler.BossType.valueOf(typeStr.toUpperCase()))).addPlayer(player);
        });
    }
}


