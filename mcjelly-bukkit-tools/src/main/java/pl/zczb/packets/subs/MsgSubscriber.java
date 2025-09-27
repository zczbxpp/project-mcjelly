 package pl.zczb.packets.subs;

 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.MsgPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.tools.database.user.models.UserDataModel;

 public final class MsgSubscriber extends RedisSubscriber<MsgPacket> {
   public MsgSubscriber() {
     super(MsgPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }


   public void onPacketReceived(MsgPacket packet) {
     UserDataModel receiveUser = Tools.getInstance().getUserHandler().getPlayer(packet.getReceiverName());

     UserDataModel senderUser = Tools.getInstance().getUserHandler().getPlayer(packet.getSenderName());

     Player p = Bukkit.getPlayer(receiveUser.getNick());

     for (Player po : Bukkit.getOnlinePlayers()) {
       UserDataModel pouser = Tools.getInstance().getUserHandler().getPlayer(po);
       if (!po.hasPermission("zczb.helper") || !pouser.isSocialSpy())
         continue;
       po.sendMessage(GlobalHelper.fixColor(" &#507ED4" + packet.getSenderName() + " → " + packet.getReceiverName() + ": " + packet.getMessage()));
     }

     receiveUser.setLastConverser(packet.getSenderName());
     senderUser.setLastConverser(packet.getReceiverName());

     if (p != null && p.isOnline())
       p.sendMessage(GlobalHelper.fixColor(" &7" + packet.getSenderName() + " &8→ &#FFB874Ty&8: &f" + packet.getMessage()));
   }
 }


