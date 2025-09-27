package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

import java.util.Set;

@Data
public final class TpaAcceptAllPacket extends Packet {

    private final String targetPlayer;
    private final Set<String> requestPlayers;

}