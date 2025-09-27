 package pl.zczb.packets.subs;

 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.ChatPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.tools.database.user.models.UserDataModel;

 public final class ChatSubscriber
   extends RedisSubscriber<ChatPacket>
 {
   public ChatSubscriber() {
     super(ChatPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }


   public void onPacketReceived(ChatPacket packet) {
     UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(packet.getNick());




     String message = Tools.getSectorConfig().getChat_template().replace("%group%", packet.getGroup()).replace("%nick%", packet.getNick());


     if (u.isSprawdzany()) {



       message = "&b&lSprawdzany&f " + Tools.getSectorConfig().getChat_template().replace("%group%", packet.getGroup()).replace("%nick%", packet.getNick());

       for (Player po : Bukkit.getOnlinePlayers())
       {
         po.sendMessage(GlobalHelper.fixColor(message).replace("%message%", packet.getMessage()));
       }
       return;
     }
     Bukkit.broadcastMessage(GlobalHelper.fixColor(message).replace("%message%", packet.getMessage()));
   }
 }


