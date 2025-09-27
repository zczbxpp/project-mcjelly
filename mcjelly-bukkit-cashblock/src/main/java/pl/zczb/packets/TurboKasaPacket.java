package pl.zczb.packets;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;


@Data
@RequiredArgsConstructor
public final class TurboKasaPacket extends Packet {
    private final long time;
    private final String admin;
}


