package pl.zczb.velocity.helpers;

import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class PluginConfig {

    private final Path filePath;
    private Map<String, Object> config;

    public PluginConfig(Path filePath) {
        this.filePath = filePath;
        load();
    }

    public void load() {
        try (InputStream input = new FileInputStream(filePath.toFile())) {
            Yaml yaml = new Yaml();
            this.config = yaml.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMotd() {
        Object value = config.get("motd");
        return value != null ? value.toString() : "&amotd";
    }
}
