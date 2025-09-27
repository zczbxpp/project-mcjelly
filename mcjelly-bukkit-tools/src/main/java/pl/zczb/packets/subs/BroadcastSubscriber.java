 package pl.zczb.packets.subs;
 import net.md_5.bungee.api.ChatMessageType;
 import net.md_5.bungee.api.chat.BaseComponent;
 import net.md_5.bungee.api.chat.TextComponent;
 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.BroadcastPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 
 public final class BroadcastSubscriber extends RedisSubscriber<BroadcastPacket> {
   public BroadcastSubscriber() {
     super(BroadcastPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }
 
 
   
   public void onPacketReceived(BroadcastPacket packet) {
     switch (packet.getType()) {
       case "actionbar":
         for (Player p : Bukkit.getOnlinePlayers()) {
           p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)new TextComponent(GlobalHelper.fixColor(packet.getMessage())));
         }
         break;
       case "subtitle":
         for (Player p : Bukkit.getOnlinePlayers()) {
           p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor(packet.getMessage()));
         }
         break;
       case "chat":
         Bukkit.broadcastMessage(GlobalHelper.fixColor(packet.getMessage()));
         break;
     } 
   }
 }


