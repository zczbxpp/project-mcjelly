package pl.zczb.sectors.serialization;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EffectSerializationHelper {

    private static final Gson gson = new Gson();

    public static class SerializedEffect {
        public String type;
        public int amplifier;
        public int duration;
        public boolean ambient;
        public boolean particles;

        public SerializedEffect(PotionEffect effect) {
            this.type = effect.getType().getName();
            this.amplifier = effect.getAmplifier();
            this.duration = effect.getDuration();
            this.ambient = effect.isAmbient();
            this.particles = effect.hasParticles();
        }
    }

    public static String serializeEffects(List<PotionEffect> effects) {
        List<SerializedEffect> serialized = new ArrayList<>();
        for (PotionEffect effect : effects) {
            serialized.add(new SerializedEffect(effect));
        }
        return gson.toJson(serialized);
    }

    public static List<PotionEffect> deserializeEffects(String json) {
        if (json == null || json.isEmpty()) return new ArrayList<>();
        Type listType = new TypeToken<List<SerializedEffect>>() {
        }.getType();
        List<SerializedEffect> serialized = gson.fromJson(json, listType);
        List<PotionEffect> effects = new ArrayList<>();
        for (SerializedEffect se : serialized) {
            PotionEffectType type = PotionEffectType.getByName(se.type);
            if (type != null) {
                effects.add(new PotionEffect(type, se.duration, se.amplifier, se.ambient, se.particles));
            } else {
                System.out.println("Nieznany efekt: " + se.type);
            }
        }
        return effects;
    }
}
