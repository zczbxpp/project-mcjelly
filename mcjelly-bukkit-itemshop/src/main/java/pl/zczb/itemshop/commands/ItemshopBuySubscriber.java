 package pl.zczb.itemshop.commands;
 
 import pl.zczb.Itemshop;
 import pl.zczb.itemshop.data.user.models.UserDataModel;
 import pl.zczb.redis.subscriber.RedisSubscriber;
 
 public final class ItemshopBuySubscriber extends RedisSubscriber<ItemshopBuyPacket> {
   public ItemshopBuySubscriber() {
     super(ItemshopBuyPacket.class, "CH|itemshop");
   }
 
   
   public void onPacketReceived(ItemshopBuyPacket packet) {
     UserDataModel u = Itemshop.getInstance().getUserHandler().getPlayer(packet.getPlayerName());
     u.removePln(packet.getPln());
   }
 }


