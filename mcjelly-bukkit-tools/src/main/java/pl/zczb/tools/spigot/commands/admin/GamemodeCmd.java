package pl.zczb.tools.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import pl.zczb.helpers.GlobalHelper;

@Command(name = "gm", aliases = {"gamemode"})
@Permission({"zczb.root"})
public class GamemodeCmd {
    @Execute
    public void setGameMode(@Context Player player, @Arg("tryb") GameMode gameMode) {
        player.setGameMode(gameMode);
        player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie zmieniono tryb gry na " + gameMode.name().toLowerCase()));
    }
}


