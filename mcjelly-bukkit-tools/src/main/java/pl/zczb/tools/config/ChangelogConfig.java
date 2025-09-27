package pl.zczb.tools.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import pl.zczb.tools.config.data.Changelog;

import java.util.Arrays;
import java.util.List;

@Data
public class ChangelogConfig extends OkaeriConfig {

    private List<Changelog> changelogs = Arrays.asList(
            new Changelog("Nazwa changeloga", Arrays.asList("linia1", "linia2"))
    );
}
