package pl.zczb.tools.helpers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import pl.zczb.Tools;

public class BungeeHelper {
    public static void sendToServer(Player player, String sector) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(sector);

        player.sendPluginMessage((Plugin) Tools.getInstance(), "BungeeCord", output.toByteArray());
    }
}


