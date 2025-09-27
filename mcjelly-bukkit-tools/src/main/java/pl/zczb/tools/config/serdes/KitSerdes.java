package pl.zczb.tools.config.serdes;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;
import pl.zczb.tools.config.data.Kit;

public class KitSerdes implements ObjectSerializer<Kit> {

    @Override
    public boolean supports(@NotNull Class<? super Kit> type) {
        return Kit.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull Kit object, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("name", object.getName());
        data.add("label", object.getLabel());
        data.add("slot", object.getSlot());
        data.add("material", object.getMaterial());
        data.addCollection("lore", object.getLore(), String.class);
        data.addCollection("items", object.getItems(), String.class);
        data.add("cooldown", object.getCooldown());
        data.add("permission", object.getPermission());
    }

    @Override
    public Kit deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        return new Kit(
                data.get("name", String.class),
                data.get("label", String.class),
                data.get("slot", Integer.class),
                data.get("material", String.class),
                data.getAsList("lore", String.class),
                data.getAsList("items", String.class),
                data.get("cooldown", Long.class),
                data.get("permission", String.class)
        );
    }
}

