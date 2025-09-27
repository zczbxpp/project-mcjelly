 package pl.zczb.tools.spigot.commands.user;
 
 import dev.rollczi.litecommands.annotations.command.Command;
 import dev.rollczi.litecommands.annotations.context.Context;
 import dev.rollczi.litecommands.annotations.execute.Execute;
 import java.util.List;
 import java.util.stream.Collectors;

 import dev.triumphteam.gui.builder.item.ItemBuilder;
 import dev.triumphteam.gui.guis.Gui;
 import dev.triumphteam.gui.guis.GuiItem;
 import net.kyori.adventure.text.Component;
 import org.bukkit.Bukkit;
 import org.bukkit.Location;
 import org.bukkit.Material;
 import org.bukkit.command.CommandSender;
 import org.bukkit.entity.HumanEntity;
 import org.bukkit.entity.Player;
 import org.bukkit.event.inventory.InventoryClickEvent;
 import pl.zczb.Tools;

 import pl.zczb.helpers.GlobalHelper;
 import pl.zczb.sectors.managers.SectorManager;
 import pl.zczb.sectors.managers.data.Sector;
 import pl.zczb.sectors.serialization.SerializationHelper;
 import pl.zczb.tools.config.data.Warp;
 import pl.zczb.tools.database.user.models.UserDataModel;

 @Command(name = "warp", aliases = {"warpy"})
 public class WarpCmd {
   @Execute
   public void onWarpOpen(@Context CommandSender sender) {
       if (!(sender instanceof Player player)) return;

     if (Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_event")) {
       sender.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNa kanale eventowym nie możesz tego używać!"));
       return;
     }

       Gui gui = Gui.gui()
               .title(Component.text(GlobalHelper.fixColor("&8Warpy")))
               .rows(5)
               .disableAllInteractions()
               .create();



       for (Warp warp : Tools.getWarpConfig().values()) {
           Material material = Material.getMaterial(warp.getMaterial().toUpperCase());
           if (material == null) continue;

           Location loc = SerializationHelper.stringToLoc(warp.getLocation());
           if (loc == null) continue;
           loc.setYaw(warp.getYaw());
           List<Component> lore = warp.getLore().stream()
                   .map(GlobalHelper::fixColor)
                   .map(Component::text)
                   .collect(Collectors.toList());

           GuiItem item = ItemBuilder.from(material)
                   .name(Component.text(GlobalHelper.fixColor(warp.getName())))
                   .lore(lore)
                   .asGuiItem(e -> {
                       gui.close(player);
                       player.teleport(loc);
                   });

           gui.setItem(warp.getSlot(), item);
       }

       if(Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase().equals("cashblock")) {
           GuiItem warppvp = ItemBuilder.from(Material.DIAMOND_SWORD)
                   .name(Component.text(GlobalHelper.fixColor("&4&lᴡᴀʀᴘ ᴘᴠᴘ")))
                   .lore(
                           Component.text(GlobalHelper.fixColor("")),
                           Component.text(GlobalHelper.fixColor("&fCo tutaj znajdziesz?")),
                           Component.text(GlobalHelper.fixColor(" &7Warp na którym możesz sie bić")),
                           Component.text(GlobalHelper.fixColor(" &7i &cwalczyć &7z bossami!")),
                           Component.text(GlobalHelper.fixColor("")),
                           Component.text(GlobalHelper.fixColor("&aKliknij, aby się teleportować!"))
                   )
                   .asGuiItem(e -> {
                       gui.close(player);

                       Sector sector = SectorManager.getSector("cashblock_event");
                       if (!sector.isOnline()) {
                           player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTen sektor jest wyłączony!"));
                           return;
                       }

                       if (sector.isAdminMode() && !player.isOp()) {
                           player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTen sektor jest w trybie administracyjnym!"));
                           return;
                       }

                       UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(player);

                       if (user.isChangingSector()) {
                           player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
                           return;
                       }

                       SectorManager.teleportToLocationSector(player,"cashblock_event",new Location(Bukkit.getWorld("world"),0.0D, 70.0D, 0.0D));
                   });
           gui.setItem(13, warppvp);
       }


       GuiItem white = ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
               .name(Component.text(GlobalHelper.fixColor(" ")))
               .asGuiItem();
       GuiItem yellow = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE)
               .name(Component.text(GlobalHelper.fixColor(" ")))
               .asGuiItem();
       GuiItem orange = ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
               .name(Component.text(GlobalHelper.fixColor(" ")))
               .asGuiItem();

       gui.setItem(0, orange);
       gui.setItem(1, yellow);
       gui.setItem(2, white);
       gui.setItem(3, white);
       gui.setItem(5, white);
       gui.setItem(6, white);
       gui.setItem(7, yellow);
       gui.setItem(8, orange);
       gui.setItem(9, yellow);
       gui.setItem(17, yellow);
       gui.setItem(27, yellow);
       gui.setItem(35, yellow);
       gui.setItem(36, orange);
       gui.setItem(37, yellow);
       gui.setItem(38, white);
       gui.setItem(39, white);
       gui.setItem(41, white);
       gui.setItem(42, white);
       gui.setItem(43, yellow);
       gui.setItem(44, orange);

       gui.open(player);
   }

     private Location parseLocation(String locString) {
         try {
             String[] parts = locString.split(":");
             if (parts.length < 4) return null;

             String worldName = parts[0];
             double x = Double.parseDouble(parts[1]);
             double y = Double.parseDouble(parts[2]);
             double z = Double.parseDouble(parts[3]);

             return new Location(Bukkit.getWorld(worldName), x, y, z);
         } catch (Exception e) {
             return null;
         }
     }
 }

