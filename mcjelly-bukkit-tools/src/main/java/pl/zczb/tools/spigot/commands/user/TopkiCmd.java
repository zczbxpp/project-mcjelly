 package pl.zczb.tools.spigot.commands.user;
 import dev.rollczi.litecommands.annotations.argument.Arg;
 import dev.rollczi.litecommands.annotations.command.Command;
 import dev.rollczi.litecommands.annotations.context.Context;
 import dev.rollczi.litecommands.annotations.execute.Execute;
 import java.util.List;

 import dev.triumphteam.gui.builder.item.ItemBuilder;
 import dev.triumphteam.gui.builder.item.SkullBuilder;
 import dev.triumphteam.gui.guis.Gui;
 import net.kyori.adventure.text.Component;
 import org.bukkit.Bukkit;
 import org.bukkit.OfflinePlayer;
 import org.bukkit.entity.HumanEntity;
 import org.bukkit.entity.Player;
 import pl.zczb.Tools;

 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.tools.helpers.OtherHelper;
 import pl.zczb.tools.tops.enums.TopType;
 import pl.zczb.tools.tops.impl.Top;
 
 @Command(name = "top-core")
 public class TopkiCmd {
   @Execute
   public void showTopGui(@Context Player player, @Arg("topka") TopType topType) {
       List<Top> tops = Tools.getInstance().getTopManager().getTopList(topType).getTops();

       Gui gui = Gui.gui()
               .title(Component.text(GlobalHelper.fixColor("&8Topka " + topType.name())))
               .rows(3)
               .disableAllInteractions()
               .create();

       String displayValue = switch (topType) {
           case TIME -> "Ilość spedzonego czasu";
           case PARKOUR -> "Ilość czasu parkour";
           default -> "&f";
       };

       int slot = 0;
       for (Top top : tops) {
           if (slot >= 27)
               break;
           String nick = top.getNickName();
           String value = top.getTopValue();
           OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(nick);

           ItemBuilder skull = ItemBuilder.from(ItemBuilder.skull().owner(offlinePlayer)
                   .name(Component.text(GlobalHelper.fixColor("&7#" + (slot + 1) + " &f" + nick)))
                   .lore(
                           Component.text(GlobalHelper.fixColor("&8* &7" + displayValue + "&8: &f" + OtherHelper.formatSecs(Long.parseLong(value))))
                   ).build());

           gui.setItem(slot, skull.asGuiItem());

           slot++;
       }

       gui.open((HumanEntity) player);
   }
 }


