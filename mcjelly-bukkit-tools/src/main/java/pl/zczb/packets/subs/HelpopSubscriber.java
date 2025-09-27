 package pl.zczb.packets.subs;
 
 import org.bukkit.Bukkit;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.HelpopPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 
 public final class HelpopSubscriber extends RedisSubscriber<HelpopPacket> {
   public HelpopSubscriber() {
     super(HelpopPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   }
 
 
   
   public void onPacketReceived(HelpopPacket packet) {
     for (Player po : Bukkit.getOnlinePlayers()) {
       if (po.hasPermission("zczb.helper"))
         po.sendMessage(GlobalHelper.fixColor("&#BA68FF&lH&#A77CFF&lE&#948FFF&lL&#82A3FF&lP&#6FB6FF&lO&#5CCAFF&lP &8(&f" + packet.getSectorName() + "&8) &7" + packet.getPlayerName() + "&8: &7" + packet.getMessage())); 
     } 
   }
 }


