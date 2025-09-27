 package pl.zczb.packets.subs;
 
 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.ChatManagePacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.tools.objects.Chat;
 
 public final class ChatManageSubscriber
   extends RedisSubscriber<ChatManagePacket>
 {
   public ChatManageSubscriber() {
     super(ChatManagePacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }
 
 
   
   public void onPacketReceived(ChatManagePacket packet) {
     switch (packet.getType()) {
       
       case "global":
         Chat.globalChat = packet.isValue();
         for (Player po : Bukkit.getOnlinePlayers()) {
           po.sendMessage("");
           po.sendMessage(GlobalHelper.fixColor("&8[&6!&8] &7Chat został &e" + (packet.isValue() ? "włączony" : "wyłączony") + " &7przez &e" + packet.getPlayerName()));
           po.sendMessage("");
         } 
         break;
 
       
       case "premium":
         Chat.vipChat = packet.isValue();
         
         for (Player po : Bukkit.getOnlinePlayers()) {
           po.sendMessage("");
           po.sendMessage(GlobalHelper.fixColor("&8[&6!&8] &7Chat &epremium &7został &e" + (packet.isValue() ? "włączony" : "wyłączony") + " &7przez &e" + packet.getPlayerName()));
           po.sendMessage("");
         } 
         break;
 
 
       
       case "clear":
         for (Player po : Bukkit.getOnlinePlayers()) {
           for (int i = 0; i < 20; i++) {
             po.sendMessage("");
           }
           po.sendMessage("");
           po.sendMessage(GlobalHelper.fixColor("&8[&6!&8] &7Chat został &ewyczyszczony &7przez &e" + packet.getPlayerName()));
           po.sendMessage("");
         } 
         break;
     } 
   }
 }


