package pl.zczb.tools.database.codec.converter;

import org.bson.Document;
import pl.zczb.tools.database.codec.CodecHelper;
import pl.zczb.tools.database.codec.Converter;
import pl.zczb.tools.database.user.models.*;

public class ModelConverter implements Converter<UserDataModel> {

    public Document encode(UserDataModel dataModel) {
        Document document = new Document();
        document.put("_id", dataModel.getUuid());
        document.put("nick", dataModel.getNick());
        document.put("userSynchro", dataModel.getUserSynchro());
        document.put("warta", dataModel.isWarta());
        document.put("wartaTime", dataModel.getWartaTime());
        document.put("parkourTime", dataModel.getParkourTime());
        document.put("nagroda", dataModel.isNagroda());
        document.put("nagrodaVerified", dataModel.isNagrodaVerified());
        document.put("playerTime", dataModel.getPlayerTime());
        document.put("userKits", dataModel.getUserKits());
        document.put("userBans", dataModel.getUserBans());
        document.put("userEnderchests", dataModel.getUserEnderchests());

        return document;
    }

    @Override
    public Class<UserDataModel> getConvertedClass() {
        return UserDataModel.class;
    }

    public UserDataModel decode(Document dataModel, CodecHelper helper) {
        return new UserDataModel(
                dataModel.get("_id", String.class),
                dataModel.get("nick", String.class),
                helper.reinterpret(dataModel.get("userSynchro", Document.class), UserSynchro.class),
                dataModel.get("warta", Boolean.class),
                dataModel.get("wartaTime", Long.class),
                dataModel.get("parkourTime", Long.class),
                dataModel.get("nagroda", Boolean.class),
                dataModel.get("nagrodaVerified", Boolean.class),
                dataModel.get("playerTime", Long.class),
                helper.reinterpret(dataModel.get("userKits", Document.class), UserKits.class),
                helper.reinterpret(dataModel.get("userBans", Document.class), UserBans.class),
                helper.reinterpret(dataModel.get("userEnderchests", Document.class), UserEnderchests.class)
        );
    }
}