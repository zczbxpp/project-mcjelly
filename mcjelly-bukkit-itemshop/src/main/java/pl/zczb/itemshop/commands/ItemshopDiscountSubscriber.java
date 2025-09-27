 package pl.zczb.itemshop.commands;

 import org.bukkit.Bukkit;
 import org.bukkit.boss.BarColor;
 import org.bukkit.boss.BarStyle;
 import org.bukkit.entity.Player;
 import pl.zczb.Itemshop;
 import pl.zczb.itemshop.data.ItemshopRegistry;
 import pl.zczb.itemshop.data.user.models.UserDataModel;
 import pl.zczb.itemshop.helpers.GlobalHelper;
 import pl.zczb.redis.subscriber.RedisSubscriber;

 public final class ItemshopDiscountSubscriber extends RedisSubscriber<ItemshopDiscountPacket> {
   public ItemshopDiscountSubscriber() {
     super(ItemshopDiscountPacket.class, "CH|itemshop");
   }


   public void onPacketReceived(ItemshopDiscountPacket packet) {
       ItemshopRegistry.setDiscountPercentage(packet.getPrzecena());

       if(packet.getPrzecena() == 0) {


           for(Player p : Bukkit.getOnlinePlayers()) {
               ItemshopDiscountCmd.bossBar.removePlayer(p);
           }
           return;
       }

       int przecenaProcent = (int) packet.getPrzecena();

       ItemshopDiscountCmd.bossBar.setTitle(GlobalHelper.fixColor("&fAktualnie trwa promocja &#FFDE70&l-" + przecenaProcent + "% &fpod &6/itemshop"));

        for(Player p : Bukkit.getOnlinePlayers()) {
            ItemshopDiscountCmd.bossBar.addPlayer(p);
        }
   }
 }


