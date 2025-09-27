package pl.zczb.itemshop.data.codec;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public final class CodecHelper {
    private final CodecRegistry registry;

    public CodecHelper(CodecRegistry registry) {
        this.registry = registry;
    }

    public <T> T reinterpret(Document document, Class<T> tClass) {
        BsonDocument bson = document.toBsonDocument(null, this.registry);

        return (T) this.registry.get(tClass)
                .decode(bson.asBsonReader(), DecoderContext.builder().build());
    }

    public <T> Map<String, T> reinterpretMap(Document document, Class<T> mapValueType) {
        Map<String, T> map = new HashMap<>();

        for (Map.Entry<String, Object> mapEntry : (Iterable<Map.Entry<String, Object>>) document.entrySet()) {
            map.put(mapEntry.getKey(), reinterpret((Document) mapEntry.getValue(), mapValueType));
        }

        return map;
    }

    public <T> List<T> reinterpretList(List<Document> documentList, Class<T> userBackupClass) {
        return (List<T>) documentList
                .stream()
                .map(d -> (d == null) ? null : reinterpret(d, userBackupClass))
                .collect(Collectors.toList());
    }
}


