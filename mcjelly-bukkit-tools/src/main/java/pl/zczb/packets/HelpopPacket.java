package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class HelpopPacket extends Packet {

    private final String sectorName;
    private final String playerName;
    private final String message;

}