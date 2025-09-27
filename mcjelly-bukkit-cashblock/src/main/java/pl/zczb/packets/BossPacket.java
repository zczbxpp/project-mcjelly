package pl.zczb.packets;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;

@Data
@RequiredArgsConstructor
public final class BossPacket extends Packet {
    private final String type;
    private final Integer amount;
}


