package pl.zczb.tools.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.serialization.SerializationHelper;


@Command(name = "spawn")
public class SpawnCmd {
    @Execute
    public void onChannel(@Context CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Ta komenda jest dostępna tylko dla graczy.");

            return;
        }
        Player p = (Player) sender;
        if (Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_event")) {
            SectorManager.teleportToLocationSector(p, SectorManager.getBestSector("cashblock").getSectorName(), new Location(Bukkit.getWorld("world"), 0.0D, 121.0D, 0.0D));

            return;
        }
        p.teleport(SerializationHelper.stringToLoc(Tools.getSectorConfig().getSpawn()));
        p.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie przeteleportowano"));
    }
}


