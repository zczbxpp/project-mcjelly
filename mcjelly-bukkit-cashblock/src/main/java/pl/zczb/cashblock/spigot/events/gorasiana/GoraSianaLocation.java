package pl.zczb.cashblock.spigot.events.gorasiana;


import lombok.Data;
import lombok.Getter;
import org.bukkit.Location;

@Data
public class GoraSianaLocation {
    private final String regionId;
    private final Location pasteLocation;
    private final String schematicFileName;
    private final Location centerLocation;

    public GoraSianaLocation(String regionId, Location pasteLocation, String schematicFileName, Location centerLocation) {
        this.regionId = regionId;
        this.pasteLocation = pasteLocation;
        this.schematicFileName = schematicFileName;
        this.centerLocation = centerLocation;
    }

}
