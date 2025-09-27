package pl.zczb.packets;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;

@Data
@RequiredArgsConstructor
public final class GameCreateAccountPacket extends Packet {
    private final String uuid;
    private final String sectorName;
    private final String userData;
}


