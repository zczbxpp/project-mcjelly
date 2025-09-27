package pl.zczb.tools.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import pl.zczb.tools.config.data.Warp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class WarpConfig extends OkaeriConfig {

    @Comment("Lista warpów z GUI")
    private Map<String, Warp> warps = new HashMap<>();


    public void add(Warp warp) {
        this.warps.put(warp.getName(), warp);
    }

    public void remove(Warp warp) {
        this.warps.remove(warp.getName());
    }

    public Warp findByName(String name) {
        return this.warps.get(name);
    }

    public Collection<Warp> values() {
        return warps.values();
    }
}
