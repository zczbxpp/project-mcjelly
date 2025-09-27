package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class ChatManagePacket extends Packet {

    private final String playerName;
    private final String type;
    private final boolean value;

}