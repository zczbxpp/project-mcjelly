package pl.zczb.packets;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zczb.cashblock.tops.enums.TopType;
import pl.zczb.cashblock.tops.impl.TopList;
import pl.zczb.redis.packet.Packet;

@Data
@RequiredArgsConstructor
public final class TopTrybPacket extends Packet {
    private final TopType topType;
    private final TopList topList;
}


