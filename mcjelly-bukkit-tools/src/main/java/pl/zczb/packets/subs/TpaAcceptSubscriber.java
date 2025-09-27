 package pl.zczb.packets.subs;
 import org.bukkit.Bukkit;
 import org.bukkit.entity.Entity;
 import org.bukkit.entity.Player;
 import org.bukkit.plugin.Plugin;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.TpaAcceptPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.sectors.managers.SectorManager;
 import pl.zczb.tools.database.user.models.UserDataModel;
 
 public final class TpaAcceptSubscriber extends RedisSubscriber<TpaAcceptPacket> {
   public TpaAcceptSubscriber() {
     super(TpaAcceptPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }
 
 
   
   public void onPacketReceived(TpaAcceptPacket packet) {
     UserDataModel targetUser = Tools.getInstance().getUserHandler().getPlayer(packet.getTargetPlayer());
 
     
     Player p = Bukkit.getPlayer(packet.getTeleportPlayer());
     Player t = Bukkit.getPlayer(packet.getTargetPlayer());
     
     targetUser.removeTpaRequest(packet.getTeleportPlayer());
     
     if (p == null) {
       return;
     }
     if (p != null && p.isOnline()) {
       p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Twoja prosba o teleportacje zostala zaakceptowana!"));
     }
     
     if (targetUser.getSector().equals(Tools.getSectorConfig().getCurrentSector().getSectorName())) {
       Bukkit.getScheduler().runTask((Plugin)Tools.getInstance(), () -> p.teleport((Entity)t));
 
       
       return;
     } 
 
     
     SectorManager.teleportToPlayer(p, targetUser);
   }
 }


