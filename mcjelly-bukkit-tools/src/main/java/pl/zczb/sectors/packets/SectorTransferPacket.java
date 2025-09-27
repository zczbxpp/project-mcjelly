 package pl.zczb.sectors.packets;

 import lombok.Data;
 import lombok.NoArgsConstructor;
 import lombok.AllArgsConstructor;
 import lombok.RequiredArgsConstructor;
 import pl.zczb.redis.packet.Packet;

 @Data
 @AllArgsConstructor
 @RequiredArgsConstructor
 public class SectorTransferPacket extends Packet {

   private String userData;
   private String targetName;
   private String targetLocation;

 }