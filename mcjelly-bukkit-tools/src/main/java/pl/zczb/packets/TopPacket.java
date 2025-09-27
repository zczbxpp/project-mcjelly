package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;
import pl.zczb.tools.tops.enums.TopType;
import pl.zczb.tools.tops.impl.TopList;

@Data
public final class TopPacket extends Packet {

    private final TopType topType;
    private final TopList topList;

}