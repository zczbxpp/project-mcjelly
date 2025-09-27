package pl.zczb.itemshop.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemshopRegistry {
    private static final List<ShopItem> items = new ArrayList<>();
    private static double discountPercentage = 0.0;

    public static void registerItem(ShopItem item) {
        items.add(item);
    }

    public static List<ShopItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public static boolean hasItems() {
        return !items.isEmpty();
    }

    public static void clear() {
        items.clear();
    }

    public static void setDiscountPercentage(double percentage) {
        discountPercentage = Math.max(0.0, Math.min(100.0, percentage));
    }

    public static double getDiscountPercentage() {
        return discountPercentage;
    }

    public static double calculateDiscountedPrice(double originalPrice) {
        if (discountPercentage <= 0) {
            return originalPrice;
        }
        double discountMultiplier = 1.0 - (discountPercentage / 100.0);
        return originalPrice * discountMultiplier;
    }
}


