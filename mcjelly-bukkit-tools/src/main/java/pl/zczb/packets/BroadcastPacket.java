package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class BroadcastPacket extends Packet {

    private final String type;
    private final String message;

}