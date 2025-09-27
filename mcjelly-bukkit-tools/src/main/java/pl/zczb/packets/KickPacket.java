package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class KickPacket extends Packet {

  private final String nick;
  private final String text;

}