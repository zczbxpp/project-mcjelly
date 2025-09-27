package pl.zczb.cashblock.spigot.commands.admin;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.zczb.cashblock.objects.CustomBlock;
import pl.zczb.cashblock.objects.impl.CustomBlockManager;
import pl.zczb.helpers.GlobalHelper;

@Command(name = "podloz")
@Permission({"zczb.root"})
public class PodlozCmd {

    private final CustomBlockManager cashBlockManager;

    public PodlozCmd(CustomBlockManager cashBlockManager) {
        this.cashBlockManager = cashBlockManager;
    }

    @Execute(name = "set")
    public void onSetCashBlock(@Context CommandSender sender, @Arg("kwota") double amount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTa komenda może być wykonana tylko przez gracza."));
            return;
        }

        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlockExact(5);

        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie patrzysz na żaden blok."));
            return;
        }

        if (amount <= 0) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FKwota musi być większa od zera."));
            return;
        }

        CustomBlock cashBlock = new CustomBlock(targetBlock.getLocation(), amount);
        if (cashBlockManager.addCashBlock(cashBlock)) {
            player.sendMessage(GlobalHelper.fixColor("&aPomyślnie ustawiono &e" + amount + "&a pieniędzy w bloku na &b" +
                    targetBlock.getLocation().getX() + ", " + targetBlock.getLocation().getY() + ", " + targetBlock.getLocation().getZ() + "."));
        } else {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FW tym bloku już jest ustawiona kwota."));
        }
    }

    @Execute(name = "remove")
    public void onRemoveCashBlock(@Context CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTa komenda może być wykonana tylko przez gracza."));
            return;
        }

        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlockExact(5);

        if (targetBlock == null || targetBlock.getType().isAir()) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie patrzysz na żaden blok."));
            return;
        }

        if (cashBlockManager.removeCashBlock(targetBlock.getLocation())) {
            player.sendMessage(GlobalHelper.fixColor("&aPomyślnie usunięto pieniądze z bloku na &b" +
                    targetBlock.getLocation().getX() + ", " + targetBlock.getLocation().getY() + ", " + targetBlock.getLocation().getZ() + "."));
        } else {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FW tym bloku nie ma ustawionej kwoty."));
        }
    }
}