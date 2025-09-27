 package pl.zczb.packets.subs;

 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.TpaRequestPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.tools.database.user.models.UserDataModel;

 public final class TpaRequestSubscriber
   extends RedisSubscriber<TpaRequestPacket> {
   public TpaRequestSubscriber() {
     super(TpaRequestPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }



   public void onPacketReceived(TpaRequestPacket packet) {
     UserDataModel targetUser = Tools.getInstance().getUserHandler().getPlayer(packet.getTargetPlayer());
     UserDataModel requestUser = Tools.getInstance().getUserHandler().getPlayer(packet.getRequestPlayer());

     targetUser.registerTpaRequest(requestUser.getNick());


     Player p = Bukkit.getPlayer(packet.getTargetPlayer());
     if (p != null && p.isOnline())
       p.sendMessage(GlobalHelper.fixColor("&8[&#FF7D00&l!&8] &7Otrzymales prosbe o teleportacje od gracza &#FFB874" + requestUser.getNick() + "&7, wpisz &#FFB874/tpaccept &7aby zaakceptowac."));
   }
 }


