package pl.zczb.tools.config.serdes;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;
import pl.zczb.tools.config.data.Warp;

public class WarpSerdes implements ObjectSerializer<Warp> {

    @Override
    public boolean supports(@NotNull Class<? super Warp> type) {
        return Warp.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull Warp object, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("name", object.getName());
        data.add("slot", object.getSlot());
        data.add("location", object.getLocation());
        data.add("yaw", object.getYaw());
        data.add("material", object.getMaterial());
        data.addCollection("lore", object.getLore(), String.class);
    }

    @Override
    public Warp deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        return new Warp(
                data.get("name", String.class),
                data.get("slot", Integer.class),
                data.get("location", String.class),
                data.get("yaw", Float.class),
                data.get("material", String.class),
                data.getAsList("lore", String.class)
        );
    }
}

