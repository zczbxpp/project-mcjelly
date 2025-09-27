package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class MutePacket extends Packet {

  private final String type;
  private final String nick;
  private final String powod;
  private final String admin;
  private final Long czas;

}