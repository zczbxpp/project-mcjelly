 package pl.zczb.packets.subs;

 import org.bukkit.Bukkit;
 import org.bukkit.entity.Entity;
 import org.bukkit.entity.Player;
 import org.bukkit.plugin.Plugin;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.TpaAcceptAllPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.sectors.managers.SectorManager;
 import pl.zczb.tools.database.user.models.UserDataModel;

 public final class TpaAcceptAllSubscriber extends RedisSubscriber<TpaAcceptAllPacket> {
   public TpaAcceptAllSubscriber() {
     super(TpaAcceptAllPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }



   public void onPacketReceived(TpaAcceptAllPacket packet) {
     UserDataModel targetUser = Tools.getInstance().getUserHandler().getPlayer(packet.getTargetPlayer());
     targetUser.clearTpaRequests();

     for (String requesterName : packet.getRequestPlayers()) {

       Player requester = Bukkit.getPlayer(requesterName);

       if (requester == null) {
         return;
       }
       requester.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Twoja prośba o teleportację została zaakceptowana!"));

       if (targetUser.getSector().equals(Tools.getSectorConfig().getCurrentSector().getSectorName())) {

         Player targetPlayer = Bukkit.getPlayer(packet.getTargetPlayer());

         Bukkit.getScheduler().runTask((Plugin)Tools.getInstance(), () -> {
               assert targetPlayer != null;

               requester.teleport((Entity)targetPlayer);
             });
         continue;
       }
       SectorManager.teleportToPlayer(requester, targetUser);
     }
   }
 }


