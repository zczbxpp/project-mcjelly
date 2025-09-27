package pl.zczb.cashblock.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.zczb.cashblock.helpers.SerializationHelper;

@Data
public class CashblockConfig extends OkaeriConfig {
    @Comment({"Nazwa sektora"})
    private String sector_name = "cashblock_1";

    @Comment({"Lokalizacja skrzyni donate"})
    private String donate_chest = SerializationHelper.locToString(new Location(Bukkit.getWorld("world"), 0.0D, 90.0D, 0.0D));
}


