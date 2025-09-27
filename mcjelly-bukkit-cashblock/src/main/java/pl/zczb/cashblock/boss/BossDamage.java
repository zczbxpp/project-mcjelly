 package pl.zczb.cashblock.boss;
 
 import java.util.UUID;
 
 public class BossDamage {
   private final UUID uniqueId;
   private int damage;

   public BossDamage(UUID uniqueId) {
     this.damage = 0;
     this.uniqueId = uniqueId;
   }
   
   public UUID getUniqueId() {
     return this.uniqueId;
   }
   
   public int getDamage() {
     return this.damage;
   }
   
   public void addHP() {
     this.damage++;
   }
 }


