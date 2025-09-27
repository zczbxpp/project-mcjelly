package pl.zczb.itemshop.data.codec;

import org.bson.Document;

public interface Converter<T> {
    Document encode(T paramT);

    T decode(Document paramDocument, CodecHelper paramCodecHelper);

    Class<T> getConvertedClass();
}


