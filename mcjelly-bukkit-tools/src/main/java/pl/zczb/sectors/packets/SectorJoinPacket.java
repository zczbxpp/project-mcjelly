package pl.zczb.sectors.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class SectorJoinPacket extends Packet {

    private final String playerName;
    private final String sectorName;

}