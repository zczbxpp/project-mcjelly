 package pl.zczb.packets.subs;
 
 import pl.zczb.Tools;
 import pl.zczb.packets.TopPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 
 public final class TopSubscriber
   extends RedisSubscriber<TopPacket>
 {
   public TopSubscriber() {
     super(TopPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }
 
 
 
   
   public void onPacketReceived(TopPacket packet) {
     Tools.getInstance().getTopManager().replaceTop(packet.getTopType(), packet.getTopList());
   }
 }


