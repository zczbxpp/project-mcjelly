package pl.zczb.tools.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import pl.zczb.tools.config.data.Help;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class PomocConfig extends OkaeriConfig {


    @Comment("Lista pomocy z GUI")
    private Map<String, Help> helps = new HashMap<>();


    public void add(Help help) {
        this.helps.put(help.getName(), help);
    }

    public void remove(Help help) {
        this.helps.remove(help.getName());
    }

    public Help findByName(String name) {
        return this.helps.get(name);
    }

    public Collection<Help> values() {
        return helps.values();
    }
}
