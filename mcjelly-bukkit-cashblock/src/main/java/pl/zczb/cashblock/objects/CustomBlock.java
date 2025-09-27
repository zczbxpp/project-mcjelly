package pl.zczb.cashblock.objects;

import org.bukkit.Location;

public class CustomBlock {
    private final Location location;
    private final double amount;

    public CustomBlock(Location location, double amount) {
        this.location = location;
        this.amount = amount;
    }

    public Location getLocation() {
        return location;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomBlock cashBlock = (CustomBlock) o;
        return location.equals(cashBlock.location);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
}