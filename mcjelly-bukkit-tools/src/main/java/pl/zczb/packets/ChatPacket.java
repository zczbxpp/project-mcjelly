package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class ChatPacket extends Packet {

    private final String nick;
    private final String message;
    private final String group;

}