package pl.zczb.tools.database.user.models;


import lombok.Data;
import org.bson.Document;
import pl.zczb.tools.database.codec.CodecHelper;
import pl.zczb.tools.database.codec.Converter;


@Data
public class UserBans {

    private String ipAddress;

    private boolean banned;
    private String banMessage;
    private Long banTime;
    private String banAdmin;

    private boolean muted;
    private String muteMessage;
    private Long muteTime;
    private String muteAdmin;

    private UserBans(String ipAddress, boolean banned, String banMessage, Long banTime, boolean muted, String muteMessage, Long muteTime,String banAdmin, String muteAdmin) {
        this.ipAddress = ipAddress;
        this.banned = banned;
        this.banMessage = banMessage;
        this.banTime = banTime;
        this.muted = muted;
        this.muteMessage = muteMessage;
        this.muteTime = muteTime;
        this.banAdmin = banAdmin;
        this.muteAdmin = muteAdmin;
    }

    public static UserBans createDefault() {
        return new UserBans(null, false, null, null, false,null,  null,"","");
    }


    public static class UserBansConverter implements Converter<UserBans> {

        @Override
        public Document encode(UserBans userBans) {
            Document document = new Document();
            document.put("ipAddress", userBans.ipAddress);
            document.put("banned", userBans.banned);
            document.put("banMessage", userBans.banMessage);
            document.put("banTime", userBans.banTime);
            document.put("muted", userBans.muted);
            document.put("muteMessage", userBans.muteMessage);
            document.put("muteTime", userBans.muteTime);
            document.put("banAdmin", userBans.banAdmin);
            document.put("muteAdmin", userBans.muteAdmin);
            return document;
        }

        @Override
        public UserBans decode(Document document, CodecHelper helper) {
            return new UserBans(
                    document.getString("ipAddress"),

                    document.getBoolean("banned"),
                    document.getString("banMessage"),
                    document.getLong("banTime"),

                    document.getBoolean("muted"),
                    document.getString("muteMessage"),
                    document.getLong("muteTime"),
                    document.getString("banAdmin"),
                    document.getString("muteAdmin")


            );
        }

        @Override
        public Class<UserBans> getConvertedClass() {
            return UserBans.class;
        }

    }

}