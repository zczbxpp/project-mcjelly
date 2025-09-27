 package pl.zczb.packets.subs;
 
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import org.bukkit.Bukkit;
 import org.bukkit.Location;
 import org.bukkit.entity.Player;
 import org.bukkit.plugin.Plugin;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.CheckPacket;
 import pl.zczb.redis.packet.Packet;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 import pl.zczb.sectors.managers.SectorManager;
 import pl.zczb.tools.database.user.models.UserBans;
 import pl.zczb.tools.database.user.models.UserDataModel;
 
 public final class CheckSubscriber
   extends RedisSubscriber<CheckPacket>
 {
   public CheckSubscriber() {
     super(CheckPacket.class, "CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase());
   } public void onPacketReceived(CheckPacket packet) {
     long l1;
     Player adm;
     long muteUntil;
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
         l1 = System.currentTimeMillis() + parseDuration("7d");
         
         bans.setBanned(true);
         bans.setBanMessage("\n&cZostałeś zbanowany!\n&cPowód: &4Cheaty\n&cAdministrator: &4" + packet.getAdmin());
         bans.setBanTime(Long.valueOf(l1));
         
         Tools.getInstance().getUserHandler().updateUser(data);
         
         if (player != null) {
           Bukkit.getScheduler().runTask((Plugin)Tools.getInstance(), () -> player.kickPlayer(GlobalHelper.fixColor(bans.getBanMessage())));
         }
         break;
 
 
       
       case "czysty":
         if (player != null) {
           Bukkit.getScheduler().runTask((Plugin)Tools.getInstance(), () -> player.teleport(new Location(Bukkit.getWorld("world"), 0.0D, 121.0D, 0.0D)));
 
           
           player.sendTitle(GlobalHelper.fixColor("&2Jesteś czysty!"), GlobalHelper.fixColor("&aDziękujemy za gre na mcjelly.pl!"));
         } 
         data.setSprawdzany(false);
         break;
       case "sprawdz":
         data.setSprawdzany(true);
         if (player != null) {
           if (data.getSector().equals("event")) {
             SectorManager.teleportToLocationSector(player, SectorManager.getBestSector("cashblock").getSectorName(), new Location(Bukkit.getWorld("world"), -12.0D, 129.0D, -22.0D));
             return;
           } 
           Bukkit.getScheduler().runTask((Plugin)Tools.getInstance(), () -> player.teleport(new Location(Bukkit.getWorld("world"), -12.0D, 129.0D, -22.0D)));
         } 
 
         
         adm = Bukkit.getPlayer(packet.getAdmin());
         
         if (adm != null) {
           SectorManager.teleportToPlayer(adm, data);
         }
         break;
       
       case "przyznajsie":
         if (player != null) {
           bans.setIpAddress((player.getAddress() != null) ? player.getAddress().getAddress().getHostAddress() : null);
         }
         muteUntil = System.currentTimeMillis() + parseDuration("3d");
         
         bans.setBanned(true);
         bans.setBanMessage("\n&cZostałeś zbanowany!\n&cPowód: &4Przyznanie sie do cheatów\n&cAdministrator: &4Automat");
         bans.setBanTime(Long.valueOf(muteUntil));
         data.setSprawdzany(false);
         Tools.getInstance().getUserHandler().updateUser(data);
 
         
         if (player != null) {
           Bukkit.getScheduler().runTask((Plugin)Tools.getInstance(), () -> player.kickPlayer(GlobalHelper.fixColor(bans.getBanMessage())));
         }
         break;
     } 
 
 
 
     
     Tools.getInstance().getUserHandler().updateUser(data);
   }
   
   private long parseDuration(String input) throws IllegalArgumentException {
     Pattern pattern = Pattern.compile("(\\d+)([smhd])");
     Matcher matcher = pattern.matcher(input);
     long total = 0L;
     
     while (matcher.find()) {
       int value = Integer.parseInt(matcher.group(1));
       String unit = matcher.group(2);
       switch (unit) { case "s":
           total += value * 1000L; continue;
         case "m": total += value * 60000L; continue;
         case "h": total += value * 3600000L; continue;
         case "d": total += value * 86400000L; continue; }
        throw new IllegalArgumentException("Niepoprawna jednostka czasu.");
     } 
 
     
     if (total == 0L) throw new IllegalArgumentException("Niepoprawny czas."); 
     return total;
   }
 }


