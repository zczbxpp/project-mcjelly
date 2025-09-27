package pl.zczb.tools.database.user.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import pl.zczb.tools.database.codec.CodecHelper;
import pl.zczb.tools.database.codec.Converter;

import java.util.Base64;

@Data
public class UserEnderchests {
    private byte[] ender_1;
    private byte[] ender_2;
    private byte[] ender_3;
    private byte[] ender_4;
    private byte[] ender_5;



    private UserEnderchests(byte[] ender_1,byte[] ender_2,
                            byte[] ender_3,byte[] ender_4,
                            byte[] ender_5) {
        this.ender_1 = ender_1;
        this.ender_2 = ender_2;
        this.ender_3 = ender_3;
        this.ender_4 = ender_4;
        this.ender_5 = ender_5;

    }

    public static UserEnderchests createDefault() {
        byte[] defaultEnderchest = Base64.getDecoder().decode("rO0ABXVyACFbTG9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFjazuWEWyPcqQUzwIAAHhwAAAANnBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");

        return new UserEnderchests(defaultEnderchest, defaultEnderchest, defaultEnderchest, defaultEnderchest, defaultEnderchest);
    }



    public static class UserEnderchestsConverter implements Converter<UserEnderchests> {

        @Override
        public Document encode(UserEnderchests userKits) {
            Document document = new Document();
            document.put("ender_1", userKits.ender_1);
            document.put("ender_2", userKits.ender_2);
            document.put("ender_3", userKits.ender_3);
            document.put("ender_4", userKits.ender_4);
            document.put("ender_5", userKits.ender_5);

            return document;
        }

        @Override
        public UserEnderchests decode(Document document, CodecHelper helper) {
            return new UserEnderchests(
                    ((org.bson.types.Binary) document.get("ender_1")).getData(),
                    ((org.bson.types.Binary) document.get("ender_2")).getData(),
                    ((org.bson.types.Binary) document.get("ender_3")).getData(),
                    ((org.bson.types.Binary) document.get("ender_4")).getData(),
                    ((org.bson.types.Binary) document.get("ender_5")).getData()
            );
        }

        @Override
        public Class<UserEnderchests> getConvertedClass() {
            return UserEnderchests.class;
        }
    }
}
