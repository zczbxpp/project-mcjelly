package pl.zczb.tools.database.user.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import pl.zczb.tools.database.codec.CodecHelper;
import pl.zczb.tools.database.codec.Converter;


public class UserKits {


    @Setter
    @Getter
    private long kit_gracz;

    @Setter
    @Getter
    private long kit_vip;

    @Setter
    @Getter
    private long kit_svip;

    @Setter
    @Getter
    private long kit_mvip;

    @Setter
    @Getter
    private long kit_donator;

    @Setter
    @Getter
    private long kit_media;

    private UserKits(long kit_gracz, long kit_vip, long kit_svip, long kit_mvip, long kit_donator, long kit_media) {
        this.kit_gracz = kit_gracz;
        this.kit_vip = kit_vip;
        this.kit_svip = kit_svip;
        this.kit_mvip = kit_mvip;
        this.kit_donator = kit_donator;
        this.kit_media = kit_media;
    }

    public static UserKits createDefault() {
        return new UserKits(0, 0, 0, 0, 0, 0);
    }


    public boolean isKitGracz() {
        return (getKit_gracz() > System.currentTimeMillis());
    }

    public boolean isKitVip() {
        return (getKit_vip() > System.currentTimeMillis());
    }

    public boolean isKitSvip() {
        return (getKit_svip() > System.currentTimeMillis());
    }

    public boolean isKitMvip() {
        return (getKit_mvip() > System.currentTimeMillis());
    }

    public boolean isKitDonator() {
        return (getKit_donator() > System.currentTimeMillis());
    }

    public boolean isKitMedia() {
        return (getKit_media() > System.currentTimeMillis());
    }

    public long getCooldownForKit(String kitName) {
        switch (kitName.toLowerCase()) {
            case "gracz":
                return kit_gracz;
            case "vip":
                return kit_vip;
            case "svip":
                return kit_svip;
            case "mvip":
                return kit_mvip;
            case "donator":
                return kit_donator;
            case "media":
                return kit_media;
            default:
                return 0;
        }
    }

    public boolean isCooldownForKit(String kitName) {
        return switch (kitName.toLowerCase()) {
            case "gracz" -> (getKit_gracz() > System.currentTimeMillis());
            case "vip" -> (getKit_vip() > System.currentTimeMillis());
            case "svip" -> (getKit_svip() > System.currentTimeMillis());
            case "mvip" -> (getKit_mvip() > System.currentTimeMillis());
            case "donator" -> (getKit_donator() > System.currentTimeMillis());
            case "media" -> (getKit_media() > System.currentTimeMillis());
            default -> false;
        };
    }

    public void setCooldownForKit(String kitName, long value) {
        switch (kitName.toLowerCase()) {
            case "gracz":
                kit_gracz = value;
                break;
            case "vip":
                kit_vip = value;
                break;
            case "svip":
                kit_svip = value;
                break;
            case "mvip":
                kit_mvip = value;
                break;
            case "donator":
                kit_donator = value;
                break;
            case "media":
                kit_media = value;
                break;
        }
    }


    public static class UserKitsConverter implements Converter<UserKits> {

        @Override
        public Document encode(UserKits userKits) {
            Document document = new Document();
            document.put("kit_gracz", userKits.kit_gracz);
            document.put("kit_vip", userKits.kit_vip);
            document.put("kit_svip", userKits.kit_svip);
            document.put("kit_mvip", userKits.kit_mvip);
            document.put("kit_donator", userKits.kit_donator);
            document.put("kit_media", userKits.kit_media);
            return document;
        }

        @Override
        public UserKits decode(Document document, CodecHelper helper) {
            return new UserKits(
                    document.getLong("kit_gracz"),
                    document.getLong("kit_vip"),
                    document.getLong("kit_svip"),
                    document.getLong("kit_mvip"),
                    document.getLong("kit_donator"),
                    document.getLong("kit_media")
            );
        }

        @Override
        public Class<UserKits> getConvertedClass() {
            return UserKits.class;
        }
    }
}
