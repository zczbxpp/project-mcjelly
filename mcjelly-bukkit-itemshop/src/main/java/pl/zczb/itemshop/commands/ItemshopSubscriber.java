package pl.zczb.itemshop.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.zczb.Itemshop;
import pl.zczb.itemshop.data.user.models.UserDataModel;
import pl.zczb.itemshop.helpers.GlobalHelper;
import pl.zczb.redis.subscriber.RedisSubscriber;

import java.text.DecimalFormat;

public final class ItemshopSubscriber extends RedisSubscriber<ItemshopPacket> {
    public ItemshopSubscriber() {
        super(ItemshopPacket.class, "CH|itemshop");
    }

    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public void onPacketReceived(ItemshopPacket packet) {
        UserDataModel u = Itemshop.getInstance().getUserHandler().getPlayer(packet.getPlayerName());
        u.addPln(packet.getPln());

        if (packet.isHidden()) {
            return;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(" ");
            p.sendMessage(GlobalHelper.fixColor("    &f" + packet.getPlayerName() + " &7doładował &6&l" + decimalFormat.format(packet.getPln()) + " &7zł!"));
            p.sendMessage(GlobalHelper.fixColor("             &aDziękujemy za wsparcie serwera!"));
            p.sendMessage(GlobalHelper.fixColor("             &8(&7https://mcjelly.pl/&8)"));
            p.sendMessage("  ");
        }
    }
}


