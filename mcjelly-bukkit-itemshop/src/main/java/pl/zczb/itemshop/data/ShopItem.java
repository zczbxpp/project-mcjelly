package pl.zczb.itemshop.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

public class ShopItem {
    private final ItemStack item;
    private final double price;
    private final int slot;
    private final Consumer<Player> onBuy;

    public ShopItem(ItemStack item, double price, int slot, Consumer<Player> onBuy) {
        this.item = item;
        this.price = price;
        this.slot = slot;
        this.onBuy = onBuy;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
    }


    public double getPrice() {
        return this.price;
    }

    public Consumer<Player> getOnBuy() {
        return this.onBuy;
    }

    public double getFinalPrice() {
        return ItemshopRegistry.calculateDiscountedPrice(this.price);
    }
}


