package pl.zczb.redis.codec;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.lettuce.core.codec.RedisCodec;
import pl.zczb.redis.packet.Packet;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class RedisJsonCodec implements RedisCodec<String, Packet> {

    private final Gson gson = new Gson();
    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    public String decodeKey(ByteBuffer buffer) {
        return charset.decode(buffer).toString();
    }

    @Override
    public Packet decodeValue(ByteBuffer buffer) {
        String json = charset.decode(buffer).toString();


        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String className = jsonObject.get("class").getAsString();

        try {
            Class<?> clazz = Class.forName(className);
            return (Packet) gson.fromJson(json, clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Nie można znaleźć klasy: " + className, e);
        }
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return charset.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(Packet packet) {
        JsonObject jsonObject = gson.toJsonTree(packet).getAsJsonObject();
        jsonObject.addProperty("class", packet.getClass().getName());
        String json = gson.toJson(jsonObject);
        return charset.encode(CharBuffer.wrap(json));
    }
}
