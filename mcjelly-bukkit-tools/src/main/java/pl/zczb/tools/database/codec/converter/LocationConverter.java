package pl.zczb.tools.database.codec.converter;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.zczb.tools.database.codec.CodecHelper;
import pl.zczb.tools.database.codec.Converter;

public class LocationConverter implements Converter<Location> {

    public Document encode(Location location) {
        Document document = new Document();
        document.put("world", location.getWorld().getName());
        document.put("x", location.getX());
        document.put("y", location.getY());
        document.put("z", location.getZ());
        document.put("yaw", location.getYaw());
        document.put("pitch", location.getPitch());
        return document;
    }

    public Location decode(Document document, CodecHelper helper) {
        return new Location(
                Bukkit.getWorld(document.getString("world")),
                document.getDouble("x"),
                document.getDouble("y"),
                document.getDouble("z"),
                document.getDouble("yaw").floatValue(),
                document.getDouble("pitch").floatValue()
        );
    }

    @Override
    public Class<Location> getConvertedClass() {
        return Location.class;
    }
}
