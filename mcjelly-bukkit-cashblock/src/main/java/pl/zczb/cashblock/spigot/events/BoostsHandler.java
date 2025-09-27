package pl.zczb.cashblock.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.objects.Pet;

public class BoostsHandler implements Listener {

    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack newOffhand = event.getOffHandItem();

        if (newOffhand.getType() == Material.SPYGLASS || newOffhand.getType() == Material.HONEYCOMB)
            checkBonuses(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 40) {
            Bukkit.getScheduler().runTaskLater((Plugin) Cashblock.getInstance(), () -> checkBonuses(player), 1L);
        } else if (event.getSlot() == 39) {
            Bukkit.getScheduler().runTaskLater((Plugin) Cashblock.getInstance(), () -> checkBonuses(player), 1L);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.TURTLE_HELMET)
            return;
        Bukkit.getScheduler().runTaskLater((Plugin) Cashblock.getInstance(), () -> {
            ItemStack helmet = player.getInventory().getHelmet();
            if (helmet != null && helmet.getType() == Material.TURTLE_HELMET) checkBonuses(player);
        }, 1L);
    }

    public static void checkBonuses(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        boolean hasHelmet = OtherHelper.hasBonusItem(helmet, Material.TURTLE_HELMET, "&b&lOkularki swagu");
        boolean hasSpyglass = OtherHelper.hasBonusItem(offHandItem, Material.SPYGLASS, "&a&lLornetka");
        boolean hasHoneycomb = OtherHelper.hasBonusItem(offHandItem, Material.HONEYCOMB, "&6&lPlaster Miodu");

        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(player);
        u.setMnoznik(1.0D);

        if (hasHelmet) u.addMnoznik(0.25D);
        if (hasSpyglass) u.addMnoznik(0.25D);
        if (hasHoneycomb) u.addMnoznik(0.1D);

        u.addMnoznik(u.getUserPrestiz().getMnoznikVpln() * 0.05D);

        Pet pet = Cashblock.getInstance().getPetHandler().getPet(u.getActivePet());
        if (pet != null) {
            u.addMnoznik(pet.getMnoznik());
        }

        if (player.hasPermission("zczb.media") || player.hasPermission("zczb.donator")) {
            u.addMnoznik(0.25D);
        } else if (player.hasPermission("zczb.mvip")) {
            u.addMnoznik(0.2D);
        } else if (player.hasPermission("zczb.svip")) {
            u.addMnoznik(0.15D);
        } else if (player.hasPermission("zczb.vip")) {
            u.addMnoznik(0.1D);
        }


        float baseSpeed = 0.2F;
        float bonusSpeed = (float) (u.getUserPrestiz().getMoreSpeed() * 0.01D);
        float finalSpeed = baseSpeed + bonusSpeed;

        if (finalSpeed > 1.0F) finalSpeed = 1.0F;
        player.setWalkSpeed(finalSpeed);
    }
}


