package pl.zczb.sectors.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;
import pl.zczb.sectors.managers.data.SectorTypeEnum;

@Data
public final class SectorStatsPacket extends Packet {

  private final String sectorName;
  private final int playerCount;
  private final double tps;
  private final long lastUpdate;
  private final boolean isAdminMode;
  private final SectorTypeEnum sectorType;

}