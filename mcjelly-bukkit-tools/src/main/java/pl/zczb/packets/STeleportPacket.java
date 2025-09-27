package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class STeleportPacket extends Packet {

    private final String player;
    private final String target;

}