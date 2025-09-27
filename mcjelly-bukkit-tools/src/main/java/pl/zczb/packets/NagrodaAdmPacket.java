package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class NagrodaAdmPacket extends Packet {

    private final String userName;

}