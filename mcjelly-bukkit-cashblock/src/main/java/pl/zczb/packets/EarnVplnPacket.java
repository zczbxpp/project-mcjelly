package pl.zczb.packets;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;

@Data
@RequiredArgsConstructor
public class EarnVplnPacket extends Packet {
    private final String playerName;
    private final String reward;
}


