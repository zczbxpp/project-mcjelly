 package pl.zczb.packets.subs;

 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.KickPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;

 public final class KickSubscriber
   extends RedisSubscriber<KickPacket> {
   public KickSubscriber() {
     super(KickPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }



   public void onPacketReceived(KickPacket packet) {
     Player p = Bukkit.getPlayer(packet.getNick());
     if (p != null)
       p.kickPlayer(GlobalHelper.fixColor(packet.getText()));
   }
 }


