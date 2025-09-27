package pl.zczb.itemshop.data.user.packets.impl;

import pl.zczb.Itemshop;
import pl.zczb.itemshop.data.user.models.UserDataModel;
import pl.zczb.itemshop.data.user.packets.UserCachePacket;
import pl.zczb.redis.subscriber.RedisSubscriber;

import java.util.UUID;

public final class UserCacheSubscriber extends RedisSubscriber<UserCachePacket> {
    public UserCacheSubscriber() {
        super(UserCachePacket.class, "CH|itemshop");
    }


    public void onPacketReceived(UserCachePacket packet) {
        Itemshop.getInstance().getUserHandler().cacheUser(UUID.fromString(packet.getPlayerUniqueId()), UserDataModel.deserialize(packet.getUserdata()));
    }
}


