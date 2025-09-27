 package pl.zczb.cashblock.spigot.events;

 import java.util.Objects;
 import java.util.UUID;
 import org.bukkit.Bukkit;
 import org.bukkit.boss.BarColor;
 import org.bukkit.boss.BarStyle;
 import org.bukkit.boss.BossBar;
 import org.bukkit.entity.Player;
 import org.bukkit.event.EventHandler;
 import org.bukkit.event.EventPriority;
 import org.bukkit.event.Listener;
 import org.bukkit.event.player.PlayerJoinEvent;
 import org.bukkit.inventory.ItemStack;
 import org.bukkit.plugin.Plugin;
 import pl.zczb.Cashblock;
 import pl.zczb.Controller;
 import pl.zczb.cashblock.helpers.OtherHelper;
 import pl.zczb.cashblock.objects.impl.PetManager;
 import pl.zczb.cashblock.objects.impl.PickaxeManager;

 import pl.zczb.cashblock.objects.impl.TurboManager;
 import pl.zczb.cashblock.spigot.commands.admin.TurboKasaCmd;
 import pl.zczb.cashblock.spigot.commands.user.TestBrushCmd;
 import pl.zczb.cashblock.spigot.events.gorasiana.GoraSianaManager;

 import pl.zczb.cashblock.database.user.models.UserDataModel;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.packets.GameCreateAccountPacket;


 public class JoinHandler implements Listener {
   private final PetManager petHandler;
   
   public JoinHandler(PetManager petHandler) {
     this.petHandler = petHandler;
   }
   
   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
   public void banEvent(PlayerJoinEvent event) {
     Player player = event.getPlayer();
     UUID playerUUID = player.getUniqueId();
     
     UserDataModel user = Cashblock.getInstance().getUserHandler().getPlayer(player);
     if (user == null) {
       user = Cashblock.getInstance().getUserHandler().createUser(player);
       Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new GameCreateAccountPacket(playerUUID.toString(), Cashblock.getCashblockConfig().getSector_name(), user.serialize()));
       player.getInventory().addItem(new ItemStack[] { PickaxeManager.createPickaxe(player) });
       OtherHelper.randomTeleport(player, 1500, 3000, 1500, 3000);
     } 
 
     
     if (TurboManager.isOnTurboDrop()) {
       long remainingTime = (TurboManager.getTurbodrop_time().longValue() - System.currentTimeMillis()) / 1000L;
       String timeShow = OtherHelper.formatSecs(remainingTime);
       
       TurboKasaCmd.bossBar.addPlayer(player);
     } 
     
     if (user.isTurboDrop()) {
       long remainingTime = (user.getTurboDropTime() - System.currentTimeMillis()) / 1000L;
       String timeShow = OtherHelper.formatSecs(remainingTime);
 
       
       BossBar bar = (BossBar)TurboKasaCmd.bossbars.get(player.getName());
       if (bar != null) {
         bar.setTitle(
             GlobalHelper.fixColor("&#FF0000☀ | &#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow));
       }
       else {
         
         BossBar bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&#FF0000☀ | &#FF0000&lT&#FF6E07&lu&#FFDB0E&lr&#81ED07&lb&#03FF00&lo&#02FC55&lK&#01F8AA&la&#00F5FF&ls&#077BFF&la &#8700E2&lx&#FF00C4&l2 &7(twój) &fbedzie trwać jeszcze przez &#FFDB0E" + timeShow), BarColor.RED, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
         bossBar.addPlayer(player);
         TurboKasaCmd.bossbars.put(player.getName(), bossBar);
       } 
     } 
     
     if (Cashblock.getCashblockConfig().getSector_name().equals("cashblock_event")) {
       player.setInvulnerable(false);
     } else {
       player.setInvulnerable(true);
     } 
     
     if (!user.isTestBrush()) {
       TestBrushCmd.bossBar.addPlayer(player);
     }
 
     
     if (GoraSianaManager.timeRemainingSeconds > 0) {
       GoraSianaManager.bossBar.addPlayer(player);
     }
     
     if (!user.getNick().equalsIgnoreCase(player.getName())) {
       user.setNick(player.getName());
     }
     UserDataModel finalU = user;
     Bukkit.getScheduler().runTaskLater((Plugin)Cashblock.getInstance(), () -> { if (!Objects.equals(finalU.getActivePet(), "")) { this.petHandler.spawnPet(player); this.petHandler.applyPetEffects(player); }  },20L);
 
 
 
 
 
     
     OtherHelper.syncMinecraftLevelBar(player, user);
     BoostsHandler.checkBonuses(player);
   }
 }


