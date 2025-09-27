package pl.zczb.sectors.managers.data;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Sector {

    private String sectorName;
    private int playerCount;
    private double tps;
    private long lastUpdate;
    private SectorTypeEnum sectorType;
    private boolean isOnline;
    private boolean isAdminMode;


    public Sector(String sectorName, SectorTypeEnum sectorType, boolean isAdminMode) {
        this.sectorName = sectorName;
        this.playerCount = 0;
        this.tps = 0.0D;
        this.lastUpdate = System.currentTimeMillis();
        this.sectorType = sectorType;
        this.isOnline = false;
        this.isAdminMode = isAdminMode;
    }
}