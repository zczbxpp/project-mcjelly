package pl.zczb.itemshop.data.user.packets;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;

@Data
@RequiredArgsConstructor
public final class UserCachePacket extends Packet {
    private final String playerUniqueId;
    private final String playerName;
    private final String userdata;
}