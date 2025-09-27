package pl.zczb.itemshop.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.zczb.Controller;
import pl.zczb.Itemshop;
import pl.zczb.itemshop.commands.ItemshopDiscountCmd;
import pl.zczb.itemshop.data.ItemshopRegistry;
import pl.zczb.itemshop.data.user.models.UserDataModel;
import pl.zczb.itemshop.data.user.packets.UserCachePacket;
import pl.zczb.itemshop.helpers.GlobalHelper;
import pl.zczb.redis.packet.Packet;

import java.util.UUID;

public class UserJoinEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        UserDataModel u = Itemshop.getInstance().getUserHandler().getPlayer(player);


        if (u == null) {
            u = Itemshop.getInstance().getUserHandler().createUser(player);
            Controller.getInstance().getRedis().publish("CH|itemshop", (Packet) new UserCachePacket(player.getUniqueId().toString(), player.getName(), u.serialize()));
        }


        if (!u.getNick().equalsIgnoreCase(player.getName())) {
            u.setNick(player.getName());
        }

        if(ItemshopRegistry.getDiscountPercentage() > 0) {

            int przecenaProcent = (int) ItemshopRegistry.getDiscountPercentage();

            ItemshopDiscountCmd.bossBar.setTitle(GlobalHelper.fixColor("&fAktualnie trwa promocja &#FFDE70&l-" + przecenaProcent + "% &fpod &6/itemshop"));
            ItemshopDiscountCmd.bossBar.addPlayer(player);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        UserDataModel u = Itemshop.getInstance().getUserHandler().getPlayer(p);
        Controller.getInstance().getRedis().publish("CH|itemshop", (Packet) new UserCachePacket(p.getUniqueId().toString(), p.getName(), u.serialize()));
        ItemshopDiscountCmd.bossBar.removePlayer(p);
    }
}


