package pl.zczb.tools.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zczb.tools.config.data.Kit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Data
@NoArgsConstructor
public class KitConfig extends OkaeriConfig {

    @Comment("Lista kitów z konfiguracji")
    private Map<String, Kit> kits = new HashMap<>();

    public void add(Kit kit) {
        this.kits.put(kit.getName(), kit);
    }

    public void remove(Kit kit) {
        this.kits.remove(kit.getName());
    }

    public Kit findByName(String name) {
        return this.kits.get(name);
    }

    public Collection<Kit> values() {
        return kits.values();
    }
}
