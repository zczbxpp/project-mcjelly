package pl.zczb.itemshop.data.codec.converter;

import org.bson.Document;
import pl.zczb.itemshop.data.codec.CodecHelper;
import pl.zczb.itemshop.data.codec.Converter;
import pl.zczb.itemshop.data.user.models.UserDataModel;

public class ModelConverter implements Converter<UserDataModel> {
    public Document encode(UserDataModel dataModel) {
        Document document = new Document();
        document.put("_id", dataModel.getUuid());
        document.put("nick", dataModel.getNick());
        document.put("pln", Double.valueOf(dataModel.getPln()));

        return document;
    }


    public Class<UserDataModel> getConvertedClass() {
        return UserDataModel.class;
    }

    public UserDataModel decode(Document dataModel, CodecHelper helper) {
        return new UserDataModel((String) dataModel
                .get("_id", String.class), (String) dataModel
                .get("nick", String.class), ((Double) dataModel
                .get("pln", Double.class)).doubleValue());
    }
}


