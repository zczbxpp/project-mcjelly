package pl.zczb.itemshop.commands;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pl.zczb.redis.packet.Packet;

@Data
@RequiredArgsConstructor
public final class ItemshopDiscountPacket extends Packet {
    private final double przecena;
}


