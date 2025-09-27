package pl.zczb.cashblock.database.user.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import pl.zczb.cashblock.database.codec.CodecHelper;
import pl.zczb.cashblock.database.codec.Converter;

public class UserLvl {

    @Setter
    @Getter
    private int lvl;

    @Setter
    @Getter
    private double xp;

    @Setter
    @Getter
    private double xpToLvl;

    private UserLvl(int lvl, double xp, double xpToLvl) {
        this.lvl = lvl;
        this.xp = xp;
        this.xpToLvl = xpToLvl;
    }

    public double addXp(final double coins) {
        this.xp += coins;
        return coins;
    }

    public double addXpToLvl(final double coins) {
        this.xpToLvl += coins;
        return coins;
    }

    public double addLvl(final int coins) {
        this.lvl += coins;
        return coins;
    }

    public double removeLvl(final int coins) {
        this.lvl -= coins;
        return coins;
    }

    public static UserLvl createDefault() {
        return new UserLvl(1, 0, 1000);
    }

    public static class UserLvlConverter implements Converter<UserLvl> {

        @Override
        public Document encode(UserLvl userSynchro) {
            Document document = new Document();
            document.put("lvl", userSynchro.lvl);
            document.put("xp", userSynchro.xp);
            document.put("xpToLvl", userSynchro.xpToLvl);

            return document;
        }

        @Override
        public UserLvl decode(Document document, CodecHelper helper) {
            return new UserLvl(
                    document.getInteger("lvl"),
                    document.getDouble("xp"),
                    document.getDouble("xpToLvl")

            );
        }

        @Override
        public Class<UserLvl> getConvertedClass() {
            return UserLvl.class;
        }
    }
}