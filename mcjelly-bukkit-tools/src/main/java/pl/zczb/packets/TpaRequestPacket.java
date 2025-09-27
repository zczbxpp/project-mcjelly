 package pl.zczb.packets;

 import lombok.Data;
 import pl.zczb.redis.packet.Packet;

 @Data
 public final class TpaRequestPacket extends Packet {

     private final String targetPlayer;
     private final String requestPlayer;

 }