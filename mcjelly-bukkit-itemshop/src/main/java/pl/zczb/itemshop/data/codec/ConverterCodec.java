package pl.zczb.itemshop.data.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;


public class ConverterCodec<T> implements Codec<T> {
    private final Converter<T> converter;
    private final Class<T> tClass;
    private final CodecRegistry registry;
    private final CodecHelper helper;

    public ConverterCodec(Converter<T> converter, Class<T> tClass, CodecRegistry codecRegistry) {
        this.converter = converter;
        this.tClass = tClass;
        this.registry = codecRegistry;

        this.helper = new CodecHelper(codecRegistry);
    }


    public T decode(BsonReader reader, DecoderContext decoderContext) {
        return this.converter.decode((Document) this.registry.get(Document.class).decode(reader, decoderContext), this.helper);
    }


    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        this.registry.get(Document.class).encode(writer, this.converter.encode(value), encoderContext);
    }


    public Class<T> getEncoderClass() {
        return this.tClass;
    }
}


