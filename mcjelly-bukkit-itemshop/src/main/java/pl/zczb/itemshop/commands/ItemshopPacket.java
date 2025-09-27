package pl.zczb.itemshop.commands;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;

@Data
@RequiredArgsConstructor
public final class ItemshopPacket extends Packet {
    private final String playerName;
    private final double pln;
    private final boolean hidden;
}