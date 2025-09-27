 package pl.zczb.packets.subs;
 
 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.packets.STeleportPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.sectors.managers.SectorManager;
 import pl.zczb.tools.database.user.models.UserDataModel;
 
 public final class STeleportSubscriber extends RedisSubscriber<STeleportPacket> {
   public STeleportSubscriber() {
     super(STeleportPacket.class, Tools.getSectorConfig().getCurrentSector().getSectorName());
   }
 
 
 
   
   public void onPacketReceived(STeleportPacket packet) {
     UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(packet.getPlayer());
     UserDataModel target = Tools.getInstance().getUserHandler().getPlayer(packet.getTarget());
     
     if (!target.getSector().equals(u.getSector())) {
       
       Player p = Bukkit.getPlayer(target.getNick());
       if (p != null)
         SectorManager.teleportToPlayer(p, u); 
     } 
   }
 }


