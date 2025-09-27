package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class TpaAcceptPacket extends Packet {

    private final String targetPlayer;
    private final String teleportPlayer;

}