package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import pl.zczb.Controller;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.BossbarPacket;
import pl.zczb.redis.packet.Packet;

@Command(name = "bossbar")
@Permission({"zczb.root"})
public class BossbarCmd {
    public static BossBar bossBar;

    static {
        BarColor color;
        String configTitle = GlobalHelper.fixColor(Tools.getSectorConfig().getBossbar_title());
        String configColor = Tools.getSectorConfig().getBossbar_color().toUpperCase();
        try {
            color = BarColor.valueOf(configColor);
        } catch (IllegalArgumentException e) {
            color = BarColor.RED;
        }
        bossBar = Bukkit.createBossBar(configTitle, color, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);
        bossBar.setVisible(true);
    }


    @Execute(name = "set")
    public void setBossbar(@Context CommandSender sender, @Arg("kolor") BarColor color, @Join("wiadomosc") String message) {
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new BossbarPacket(true, message, color.name()));
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Bossbar został pomyślnie ustawiony."));
    }

    @Execute(name = "remove", aliases = {"usun"})
    public void removeBossbar(@Context CommandSender sender) {
        Controller.getInstance().getRedis().publish("CH|" + Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase(), (Packet) new BossbarPacket(false, "", ""));
        sender.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Bossbar został pomyślnie usunięty."));
    }
}


