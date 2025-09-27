 package pl.zczb.packets.subs;
 
 import java.util.Collection;
 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import org.bukkit.plugin.Plugin;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.BanPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.tools.database.user.models.UserBans;
 import pl.zczb.tools.database.user.models.UserDataModel;
 
 public final class BanSubscriber extends RedisSubscriber<BanPacket> {
   public BanSubscriber() {
     super(BanPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }
   public void onPacketReceived(BanPacket packet) {
     String ipAddress;
     Collection<UserDataModel> usersToUnban;
     UserDataModel data = Tools.getInstance().getUserHandler().getPlayer(packet.getNick());
     if (data == null)
       return; 
     UserBans bans = data.getUserBans();
     Player player = Bukkit.getPlayer(packet.getNick());
     
     switch (packet.getType().toLowerCase()) {
       case "ban":
         if (player != null) {
           bans.setIpAddress((player.getAddress() != null) ? player.getAddress().getAddress().getHostAddress() : null);
         }
         bans.setBanned(true);
         bans.setBanTime(packet.getCzas());
         bans.setBanMessage(packet.getPowod());
         bans.setBanAdmin(packet.getAdmin());
         Tools.getInstance().getUserHandler().updateUser(data);
         
         if (player != null) {
           Bukkit.getScheduler().runTask((Plugin)Tools.getInstance(), () -> player.kickPlayer(GlobalHelper.fixColor(bans.getBanMessage())));
         }
         break;
 
 
       
       case "unban":
         ipAddress = bans.getIpAddress();
         
         if (ipAddress == null || ipAddress.isEmpty()) {
           bans.setBanned(false);
           bans.setBanMessage(null);
           bans.setBanTime(null);
           bans.setBanAdmin(null);
           Tools.getInstance().getUserHandler().updateUser(data);
           
           return;
         } 
         usersToUnban = Tools.getInstance().getUserHandler().getUsersByIp(ipAddress);
         
         for (UserDataModel user : usersToUnban) {
           UserBans userBans = user.getUserBans();
           userBans.setBanned(false);
           userBans.setBanMessage(null);
           userBans.setBanTime(null);
           userBans.setBanAdmin(null);
           Tools.getInstance().getUserHandler().updateUser(user);
         } 
         break;
     } 
     
     Tools.getInstance().getUserHandler().updateUser(data);
   }
 }


