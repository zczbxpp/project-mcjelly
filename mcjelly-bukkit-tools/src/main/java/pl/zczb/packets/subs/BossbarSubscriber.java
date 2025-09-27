 package pl.zczb.packets.subs;

 import org.bukkit.Bukkit;
 import org.bukkit.boss.BarColor;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.BossbarPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.tools.spigot.commands.admin.BossbarCmd;

 public final class BossbarSubscriber
   extends RedisSubscriber<BossbarPacket> {
   public BossbarSubscriber() {
     super(BossbarPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }




   public void onPacketReceived(BossbarPacket packet) {
     if (packet.isBossbar()) {

       BossbarCmd.bossBar.setTitle(GlobalHelper.fixColor(packet.getTitle()));

       BarColor color = BarColor.valueOf(packet.getColor());
       BossbarCmd.bossBar.setColor(color);

       for (Player p : Bukkit.getOnlinePlayers()) {
         BossbarCmd.bossBar.addPlayer(p);
       }

       Tools.getSectorConfig().setBossbar(true);
     } else {
       for (Player p : Bukkit.getOnlinePlayers()) {
         BossbarCmd.bossBar.removePlayer(p);
       }

       Tools.getSectorConfig().setBossbar(false);
     }
   }
 }


