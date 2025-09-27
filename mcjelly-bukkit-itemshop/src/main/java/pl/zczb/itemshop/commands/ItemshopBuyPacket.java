package pl.zczb.itemshop.commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;

@Data
@RequiredArgsConstructor
public final class ItemshopBuyPacket extends Packet {
    private final String playerName;
    private final double pln;
}


