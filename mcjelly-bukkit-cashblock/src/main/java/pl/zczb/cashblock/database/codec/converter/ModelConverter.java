package pl.zczb.cashblock.database.codec.converter;

import org.bson.Document;
import pl.zczb.cashblock.database.codec.CodecHelper;
import pl.zczb.cashblock.database.codec.Converter;
import pl.zczb.cashblock.database.user.models.*;

public class ModelConverter implements Converter<UserDataModel> {

    public Document encode(UserDataModel dataModel) {
        Document document = new Document();
        document.put("_id", dataModel.getUuid());
        document.put("nick", dataModel.getNick());
        document.put("pln", dataModel.getPln());
        document.put("stone", dataModel.getStone());
        document.put("activePet", dataModel.getActivePet());
        document.put("turboDropTime", dataModel.getTurboDropTime());
        document.put("turboDrop", dataModel.isTurboDrop());
        document.put("userPets", dataModel.getUserPets());
        document.put("userLvl", dataModel.getUserLvl());
        document.put("userHomes", dataModel.getUserHomes());
        document.put("testBrush", dataModel.isTestBrush());
        document.put("testBrushTime", dataModel.getTestBrushTime());
        document.put("autoCx", dataModel.isAutoCx());
        document.put("userPrestiz", dataModel.getUserPrestiz());

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
                dataModel.get("pln", Double.class),
                dataModel.get("stone", Integer.class),
                dataModel.get("activePet", String.class),
                dataModel.get("turboDropTime", Long.class),
                dataModel.get("turboDrop", Boolean.class),
                helper.reinterpret(dataModel.get("userPets", Document.class), UserPets.class),
                helper.reinterpret(dataModel.get("userLvl", Document.class), UserLvl.class),
                helper.reinterpret(dataModel.get("userHomes", Document.class), UserHomes.class),
                dataModel.get("testBrush", Boolean.class),
                dataModel.get("testBrushTime", Long.class),
                dataModel.get("autoCx", Boolean.class),
                helper.reinterpret(dataModel.get("userPrestiz", Document.class), UserPrestiz.class)
        );
    }
}