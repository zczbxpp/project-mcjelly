package pl.zczb.tools.config.serdes;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;
import pl.zczb.tools.config.data.Help;


public class HelpSerdes implements ObjectSerializer<Help> {
    public boolean supports(@NotNull Class<? super Help> type) {
        return Help.class.isAssignableFrom(type);
    }


    public void serialize(@NotNull Help object, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.add("name", object.getName());
        data.add("slot", Integer.valueOf(object.getSlot()));
        data.add("material", object.getMaterial());
        data.addCollection("lore", object.getLore(), String.class);
        data.add("command", object.getCommand());
    }


    public Help deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        return new Help((String) data
                .get("name", String.class), ((Integer) data
                .get("slot", Integer.class)).intValue(), (String) data
                .get("material", String.class), data
                .getAsList("lore", String.class), (String) data
                .get("command", String.class));
    }
}


