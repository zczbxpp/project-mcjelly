package pl.zczb.tools.config.serdes;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;
import pl.zczb.tools.config.data.Changelog;


public class ChangelogSerdes implements ObjectSerializer<Changelog> {
    public boolean supports(@NotNull Class<? super Changelog> type) {
        return Changelog.class.isAssignableFrom(type);
    }


    public void serialize(@NotNull Changelog object, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("name", object.getName());
        data.addCollection("lore", object.getLore(), String.class);
    }


    public Changelog deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        return new Changelog((String) data
                .get("name", String.class), data
                .getAsList("lore", String.class));
    }
}


