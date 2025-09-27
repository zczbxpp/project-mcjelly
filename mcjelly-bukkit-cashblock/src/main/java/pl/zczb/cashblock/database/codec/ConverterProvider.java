package pl.zczb.cashblock.database.codec;


import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.HashMap;
import java.util.Map;

public class ConverterProvider implements CodecProvider {

    private final Map<Class<?>, Converter<?>> classConverterMap = new HashMap<>();

    public ConverterProvider(Converter<?>... converters) {
        for (Converter<?> converter : converters) {
            classConverterMap.put(converter.getConvertedClass(), converter);
        }
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        Converter<T> converter = findAssignableConverter(clazz);
        if (converter == null) {
            return null;
        }

        return new ConverterCodec<>(converter, clazz, registry);
    }

    @SuppressWarnings("unchecked")
    private <T> Converter<T> findAssignableConverter(Class<T> tClass) {
        for (Map.Entry<Class<?>, Converter<?>> converterEntry : classConverterMap.entrySet())
            if (converterEntry.getKey().isAssignableFrom(tClass))
                return (Converter<T>) converterEntry.getValue();
        return null;
    }
}
