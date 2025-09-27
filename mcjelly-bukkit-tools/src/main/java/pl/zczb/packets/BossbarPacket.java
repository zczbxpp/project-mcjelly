package pl.zczb.packets;

import lombok.Data;
import pl.zczb.redis.packet.Packet;

@Data
public final class BossbarPacket extends Packet {

  private final boolean isBossbar;
  private final String title;
  private final String color;

}