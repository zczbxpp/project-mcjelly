 package pl.zczb.packets.subs;
 
 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.NagrodaAdmPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.tools.database.user.models.UserDataModel;
 
 public final class NagrodaAdmSubscriber
   extends RedisSubscriber<NagrodaAdmPacket>
 {
   public NagrodaAdmSubscriber() {
     super(NagrodaAdmPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }
 
   
   public void onPacketReceived(NagrodaAdmPacket packet) {
     UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(packet.getUserName());
 
     
     u.setNagrodaVerified(true);
     
     for (Player po : Bukkit.getOnlinePlayers()) {
       po.sendMessage(" ");
       po.sendMessage(GlobalHelper.fixColor(" &7Gracz &f" + u.getNick() + " &7odebrał nagrodę &b&lDISCORD"));
       po.sendMessage(GlobalHelper.fixColor(" &8(&7https://mcjelly.pl"));
       po.sendMessage("  ");
     } 
   }
 }


