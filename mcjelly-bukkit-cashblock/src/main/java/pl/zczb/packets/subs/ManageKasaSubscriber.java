package pl.zczb.packets.subs;

import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.packets.ManageKasaPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

public final class ManageKasaSubscriber extends RedisSubscriber<ManageKasaPacket> {
    public ManageKasaSubscriber() {
        super(ManageKasaPacket.class, "CH|cashblock_tryb");
    }
    public void onPacketReceived(ManageKasaPacket packet) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(packet.getPlayerName());
        switch (packet.getType()) {

            case "set":
                u.setPln(packet.getPln());
                break;


            case "add":
                u.addPln(packet.getPln());
                break;


            case "remove":
                u.removePln(packet.getPln());
                break;
        }
    }
}


