package pl.zczb.cashblock.database.user.models;

import lombok.Data;
import org.bson.Document;
import pl.zczb.cashblock.database.codec.CodecHelper;
import pl.zczb.cashblock.database.codec.Converter;

import java.util.ArrayList;
import java.util.List;

public class UserHomes {

    @Data
    public static class Home {
        private final String sector;
        private final double x, y, z;
    }

    private static final int MAX_HOMES = 5;
    private final List<Home> homes = new ArrayList<>();

    public boolean addHome(String sector, double x, double y, double z) {
        if (homes.size() >= MAX_HOMES) {
            return false;
        }
        homes.add(new Home(sector, x, y, z));
        return true;
    }

    public static UserHomes createDefault() {
        return new UserHomes();
    }

    public boolean removeHome(String sector) {
        return homes.removeIf(home -> home.getSector().equalsIgnoreCase(sector));
    }

    public Home getHome(String sector) {
        return homes.stream()
                .filter(home -> home.getSector().equalsIgnoreCase(sector))
                .findFirst()
                .orElse(null);
    }

    public List<Home> getHomes() {
        return new ArrayList<>(homes);
    }

    public static class UserHomesConverter implements Converter<UserHomes> {

        @Override
        public Document encode(UserHomes userHomes) {
            Document document = new Document();
            List<Document> homeDocuments = new ArrayList<>();
            for (Home home : userHomes.getHomes()) {
                Document homeDoc = new Document();
                homeDoc.put("sector", home.getSector());
                homeDoc.put("x", home.getX());
                homeDoc.put("y", home.getY());
                homeDoc.put("z", home.getZ());
                homeDocuments.add(homeDoc);
            }
            document.put("homes", homeDocuments);
            return document;
        }

        @Override
        public UserHomes decode(Document document, CodecHelper helper) {
            UserHomes userHomes = new UserHomes();
            List<Document> homeDocuments = document.getList("homes", Document.class, new ArrayList<>());
            for (Document homeDoc : homeDocuments) {
                userHomes.addHome(
                        homeDoc.getString("sector"),
                        homeDoc.getDouble("x"),
                        homeDoc.getDouble("y"),
                        homeDoc.getDouble("z")
                );
            }
            return userHomes;
        }

        @Override
        public Class<UserHomes> getConvertedClass() {
            return UserHomes.class;
        }
    }
}

