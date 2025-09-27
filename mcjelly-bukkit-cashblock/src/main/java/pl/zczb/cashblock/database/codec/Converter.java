package pl.zczb.cashblock.database.codec;

import org.bson.Document;

public interface Converter<T> {

    Document encode(T t);

    T decode(Document document, CodecHelper helper);

    Class<T> getConvertedClass();
}
