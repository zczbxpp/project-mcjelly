package pl.zczb.sectors.packets.subs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.redis.packet.Packet;
import pl.zczb.redis.subscriber.RedisSubscriber;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.packets.SectorJoinPacket;
import pl.zczb.sectors.packets.SectorTransferPacket;
import pl.zczb.sectors.serialization.SerializationHelper;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SectorTransferSubscriber extends RedisSubscriber<SectorTransferPacket> {

    public static final Map<UUID, String> pendingTransfers = new ConcurrentHashMap<>();

    public SectorTransferSubscriber() {
        super(SectorTransferPacket.class, Tools.getSectorConfig().getCurrentSector().getSectorName());
    }

    public void onPacketReceived(SectorTransferPacket packet) {
        UserDataModel user = UserDataModel.deserialize(packet.getUserData());
        UUID uuid = UUID.fromString(user.getUuid());
        user.setChangingSector(false);
        String targetName = packet.getTargetName();
        String targetLocation = packet.getTargetLocation();


        if (targetName != null) {
            Player target = Bukkit.getPlayer(packet.getTargetName());
            assert target != null;
            user.getUserSynchro().setLocation(SerializationHelper.locToString(target.getLocation()));
        }


        if (targetLocation != null) {
            user.getUserSynchro().setLocation(targetLocation);
        }

        Player p = Bukkit.getPlayer(uuid);

        if (p != null && p.isOnline()) {

            SectorManager.applySynchro(p, user);
            //Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new SectorJoinPacket(p.getName(), Tools.getSectorConfig().getCurrentSector().getSectorName()));


            if (targetName != null) {
                Player target = Bukkit.getPlayer(targetName);
                assert target != null;

                Location loc = target.getLocation();
                p.teleport(loc);

                return;
            }


            Location loc = SerializationHelper.stringToLoc(user.getUserSynchro().getLocation());
            p.teleport(loc);

        } else {
            pendingTransfers.put(uuid, user.serialize());
        }
    }
}


