package pl.zczb.itemshop.data.codec.converter;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.zczb.itemshop.data.codec.CodecHelper;
import pl.zczb.itemshop.data.codec.Converter;

public class LocationConverter implements Converter<Location> {
    public Document encode(Location location) {
        Document document = new Document();
        document.put("world", location.getWorld().getName());
        document.put("x", Double.valueOf(location.getX()));
        document.put("y", Double.valueOf(location.getY()));
        document.put("z", Double.valueOf(location.getZ()));
        document.put("yaw", Float.valueOf(location.getYaw()));
        document.put("pitch", Float.valueOf(location.getPitch()));
        return document;
    }

    public Location decode(Document document, CodecHelper helper) {
        return new Location(
                Bukkit.getWorld(document.getString("world")), document
                .getDouble("x").doubleValue(), document
                .getDouble("y").doubleValue(), document
                .getDouble("z").doubleValue(), document
                .getDouble("yaw").floatValue(), document
                .getDouble("pitch").floatValue());
    }


    public Class<Location> getConvertedClass() {
        return Location.class;
    }
}


