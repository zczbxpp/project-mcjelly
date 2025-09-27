package pl.zczb.cashblock.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.brush.BrushItem;
import pl.zczb.cashblock.brush.BrushPluginConfiguration;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.helpers.GlobalHelper;

import java.util.List;


@Command(name = "testowybrush")
public class TestBrushCmd {
    public static BossBar bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&fMożesz przetestować brusha na &e1 godzine &fpod komenda &e/testowybrush"), BarColor.YELLOW, BarStyle.SOLID, new org.bukkit.boss.BarFlag[0]);

    private final BrushPluginConfiguration configuration;

    public TestBrushCmd(BrushPluginConfiguration configuration) {
        this.configuration = configuration;
    }

    @Execute
    public void onTestBrush(@Context Player p) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);
        if (u.isTestBrush()) {
            p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FOdebrałeś już swojego testowego brusha!"));

            return;
        }
        u.setTestBrush(true);
        u.setTestBrushTime(System.currentTimeMillis());
        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aOdebrałeś testowego brusha na 1 godzine!"));
        bossBar.removePlayer(p);
        BrushItem item = this.configuration.findByName("test");


        ItemStack itemStack = item.getItemStack().clone();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore != null && !lore.isEmpty()) {
                lore.set(0, ((String) lore.get(0)).replace("{name}", p.getName()));
                meta.setLore(lore);
            }
            itemStack.setItemMeta(meta);
        }

        p.getInventory().addItem(new ItemStack[]{itemStack});
    }
}


