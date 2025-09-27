package pl.zczb.sectors.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class SectorCreateAccountPacket extends Packet {

    private final String uuid;
    private final String sectorName;
    private final String userData;

}