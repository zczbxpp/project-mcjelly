package pl.zczb.cashblock.database.codec.converter;

import org.bson.Document;
import pl.zczb.cashblock.database.codec.CodecHelper;
import pl.zczb.cashblock.database.codec.Converter;
import pl.zczb.cashblock.database.zbyszek.models.ZbyszekDataModel;


public class ZbyszekConverter implements Converter<ZbyszekDataModel> {

    public Document encode(ZbyszekDataModel dataModel) {
        Document document = new Document();
        document.put("name", dataModel.getName());
        document.put("balance", dataModel.getBalance());

        return document;
    }

    @Override
    public Class<ZbyszekDataModel> getConvertedClass() {
        return ZbyszekDataModel.class;
    }

    public ZbyszekDataModel decode(Document dataModel, CodecHelper helper) {
        return new ZbyszekDataModel(
                dataModel.get("name", String.class),
                dataModel.get("balance", Integer.class)

        );
    }
}