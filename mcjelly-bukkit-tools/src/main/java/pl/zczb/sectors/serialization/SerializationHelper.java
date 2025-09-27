package pl.zczb.sectors.serialization;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SerializationHelper {

    public static byte[] serializeInventoryToBytes(Inventory inventory) {
        if (inventory == null) return new byte[0];
        return serializeBukkitObjectToBytes(inventory.getContents());
    }

    public static ItemStack[] deserializeInventoryFromBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return new ItemStack[0];
        Object obj = deserializeBukkitObjectFromBytes(bytes);
        if (obj instanceof ItemStack[]) {
            return (ItemStack[]) obj;
        }
        throw new IllegalStateException("Deserialized object is not an ItemStack array");
    }

    public static byte[] serializeBukkitObjectToBytes(Object object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos)) {
            boos.writeObject(object);
            return baos.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to serialize object", ex);
        }
    }

    public static Object deserializeBukkitObjectFromBytes(byte[] bytes) {
        try (BukkitObjectInputStream bois = new BukkitObjectInputStream(new ByteArrayInputStream(bytes))) {
            return bois.readObject();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to deserialize object", ex);
        }
    }

    public static void applyPlayerItems(Inventory inventory, byte[] serializedInventory) {
        if (inventory == null || serializedInventory == null || serializedInventory.length == 0) return;
        ItemStack[] items = deserializeInventoryFromBytes(serializedInventory);
        inventory.setContents(items);
    }


    public static String locToString(Location loc) {
        if (loc == null) return "";
        return loc.getWorld().getName() + "," +
                loc.getX() + "," +
                loc.getY() + "," +
                loc.getZ() + "," +
                loc.getYaw() + "," +
                loc.getPitch();
    }


    public static Location stringToLoc(String s) {
        if (s == null || s.isEmpty()) return null;
        String[] parts = s.split(",");
        if (parts.length < 6) return null;
        try {
            String world = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            return new Location(org.bukkit.Bukkit.getWorld(world), x, y, z, yaw, pitch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
