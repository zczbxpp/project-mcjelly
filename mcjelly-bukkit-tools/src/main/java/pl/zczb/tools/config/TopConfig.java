package pl.zczb.tools.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import pl.zczb.tools.config.data.Help;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class TopConfig extends OkaeriConfig {


    @Comment("Lista topek z GUI")
    private Map<String, Help> tops = new HashMap<>();


    public void add(Help help) {
        this.tops.put(help.getName(), help);
    }

    public void remove(Help help) {
        this.tops.remove(help.getName());
    }

    public Help findByName(String name) {
        return this.tops.get(name);
    }

    public Collection<Help> values() {
        return tops.values();
    }
}
