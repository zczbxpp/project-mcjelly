package pl.zczb.cashblock.database.user.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import pl.zczb.cashblock.database.codec.CodecHelper;
import pl.zczb.cashblock.database.codec.Converter;

public class UserPets {


    @Setter
    @Getter
    private boolean gburek;

    @Setter
    @Getter
    private boolean wesolek;

    @Setter
    @Getter
    private boolean apsik;

    @Setter
    @Getter
    private boolean gapcio;

    @Setter
    @Getter
    private boolean aronek;

    private UserPets(boolean gburek, boolean wesolek, boolean apsik, boolean gapcio, boolean aronek) {
        this.gburek = gburek;
        this.wesolek = wesolek;
        this.apsik = apsik;
        this.gapcio = gapcio;
        this.aronek = aronek;
    }

    public static UserPets createDefault() {
        return new UserPets(false, false, false, false, false);
    }


    public static class UserPetsConverter implements Converter<UserPets> {

        @Override
        public Document encode(UserPets userPets) {
            Document document = new Document();
            document.put("gburek", userPets.gburek);
            document.put("wesolek", userPets.wesolek);
            document.put("apsik", userPets.apsik);
            document.put("gapcio", userPets.gapcio);
            document.put("aronek", userPets.aronek);
            return document;
        }

        @Override
        public UserPets decode(Document document, CodecHelper helper) {
            return new UserPets(
                    document.getBoolean("gburek"),
                    document.getBoolean("wesolek"),
                    document.getBoolean("apsik"),
                    document.getBoolean("gapcio"),
                    document.getBoolean("aronek")
            );
        }

        @Override
        public Class<UserPets> getConvertedClass() {
            return UserPets.class;
        }
    }
}