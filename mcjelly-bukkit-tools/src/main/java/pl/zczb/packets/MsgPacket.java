package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class MsgPacket extends Packet {

    private final String senderName;
    private final String receiverName;
    private final String message;

}