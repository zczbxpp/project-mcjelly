 package pl.zczb.tools.spigot.commands.user;

 import dev.rollczi.litecommands.annotations.command.Command;
 import dev.rollczi.litecommands.annotations.context.Context;
 import dev.rollczi.litecommands.annotations.execute.Execute;
 import dev.triumphteam.gui.builder.item.ItemBuilder;
 import dev.triumphteam.gui.guis.Gui;
 import dev.triumphteam.gui.guis.GuiItem;
 import net.kyori.adventure.text.Component;
 import org.bukkit.Material;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.HumanEntity;
 import org.bukkit.entity.Player;
 import org.bukkit.event.inventory.InventoryClickEvent;
 import pl.zczb.Tools;
 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.redis.channels.RedisChannel;
 import pl.zczb.sectors.managers.data.SectorTypeEnum;
 import pl.zczb.tools.database.user.models.UserDataModel;
 import pl.zczb.tools.helpers.OtherHelper;

 @Command(name = "warta")
 public class WartaCmd {
   @Execute
   public void onChannel(@Context CommandSender sender) {
       final Player p = (Player)sender;




     if (Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_event")) {
       p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNa kanale eventowym nie możesz tego używać!"));

       return;
     }
     if (!OtherHelper.isInRegion(p, "spawn")) {
       p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTej komendy możesz użyc tylko na spawnie!"));

       return;
     }


       Gui gui = Gui.gui()
               .title(Component.text(GlobalHelper.fixColor("&8Warta")))
               .rows(3)
               .disableAllInteractions()
               .create();



       UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
       GuiItem shield = ItemBuilder.from(Material.SHIELD)
               .name(Component.text(GlobalHelper.fixColor("&6&lWarta")))
               .lore(
                       Component.text(GlobalHelper.fixColor("")),
                       Component.text(GlobalHelper.fixColor(" &7Nie masz co robić? Idz na &6WARTĘ!")),
                       Component.text(GlobalHelper.fixColor(" &7Każda godzina na warcie to")),
                       Component.text(GlobalHelper.fixColor(" &7od &e0.01 zł &7do nawet &e0.05 zł")),
                       Component.text(GlobalHelper.fixColor(" &7w zależności od rangi!")),
                       Component.text(GlobalHelper.fixColor(" &7Środki same trafią na twój portfel,")),
                       Component.text(GlobalHelper.fixColor(" &7gdy tylko wrócisz na serwer!")),
                       Component.text(GlobalHelper.fixColor(" &cPamiętaj pójscie na wartę wyrzuca cię z serwera!")),
                       Component.text(GlobalHelper.fixColor(" ")),
                       Component.text(GlobalHelper.fixColor("&aKliknij aby przejść na wartę"))
               )
               .asGuiItem(evente -> {
                   gui.close(p);
                   u.setWarta(true);
                   u.setWartaTime(System.currentTimeMillis());


                   RedisChannel.INSTANCE.addOnlineWartaCash(1);
                   p.kickPlayer(GlobalHelper.fixColor("&cZostałeś wyrzucony z serwera z powodu WARTY!"));
               });
       gui.setItem(13, shield);


       gui.open(p);

   }

 }
