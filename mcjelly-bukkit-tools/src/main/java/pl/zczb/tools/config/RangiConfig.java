package pl.zczb.tools.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import pl.zczb.tools.config.data.Help;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class RangiConfig extends OkaeriConfig {

    @Comment("Lista rang z GUI")
    private Map<String, Help> ranks = new HashMap<>();


    public void add(Help help) {
        this.ranks.put(help.getName(), help);
    }

    public void remove(Help help) {
        this.ranks.remove(help.getName());
    }

    public Help findByName(String name) {
        return this.ranks.get(name);
    }

    public Collection<Help> values() {
        return ranks.values();
    }
}
