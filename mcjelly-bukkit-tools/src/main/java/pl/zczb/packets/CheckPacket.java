package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class CheckPacket extends Packet {

  private final String type;
  private final String nick;
  private final String admin;

}