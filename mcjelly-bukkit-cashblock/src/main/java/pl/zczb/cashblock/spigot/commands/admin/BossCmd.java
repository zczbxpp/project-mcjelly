package pl.zczb.cashblock.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import pl.zczb.Controller;
import pl.zczb.cashblock.boss.BossHandler;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.BossPacket;
import pl.zczb.redis.packet.Packet;

@Command(name = "boss")
@Permission({"zczb.root"})
public class BossCmd {

    @Execute
    public void onBoss(@Context CommandSender sender, @Arg("typ") BossHandler.BossType type, @Arg("ilosc") int amount) {
        if (amount <= 0) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FIlość musi być większa od zera."));
            return;
        }
        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new BossPacket(type.name().toLowerCase(), Integer.valueOf(amount)));
    }
}


