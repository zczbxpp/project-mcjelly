package pl.zczb.sectors.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.zczb.Lobby;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Command(name = "ch", aliases = {"kanal"})
public class ChannelCmd {

    @Execute
    public void onChannel(@Context CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Ta komenda jest dostępna tylko dla graczy.");
            return;
        }

        Player player = (Player) sender;

        Map<String, Sector> sectors = SectorManager.getAllSectors();

        List<String> sortedSectorNames = sectors.keySet().stream()
                .filter(name -> name.startsWith(Lobby.getCfg().getCurrentSector().getSectorName()))
                .sorted()
                .toList();

        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Zmien kanal")))
                .type(GuiType.HOPPER)
                .disableAllInteractions()
                .create();

        for (String sectorName : sortedSectorNames) {
            Sector sector = SectorManager.getSector(sectorName);

            long lastUpdateMillis = sector.getLastUpdate();

            long lastUpdateSeconds = lastUpdateMillis / 1000L;
            long currentTimeSeconds = System.currentTimeMillis() / 1000L;
            long timeElapsed = currentTimeSeconds - lastUpdateSeconds;


            String sectorLabel = sectorName.equals(Lobby.getCfg().getCurrentSector().getSectorName())
                    ? "&8(&#FF7D00Jestes tutaj&8)"
                    : "";


            pl.zczb.lobby.helpers.ItemBuilder item = new pl.zczb.lobby.helpers.ItemBuilder(sector.isOnline()
                    ? (sectorName.equals(Lobby.getCfg().getCurrentSector().getSectorName()) ? Material.CHEST_MINECART : Material.FURNACE_MINECART)
                    : Material.MINECART)
                    .setName(GlobalHelper.fixColor("&#FF7D00* &7Kanał: &#FFB874" + sectorName + " &#FF7D00* " + sectorLabel))
                    .addLoreLine(GlobalHelper.fixColor(""))
                    .addLoreLine(GlobalHelper.fixColor("&8» &7Online: &#FFB874" + sector.getPlayerCount() + " graczy"))
                    .addLoreLine(GlobalHelper.fixColor("&8» &7Tps: &#FFB874" + BigDecimal.valueOf(sector.getTps()).setScale(2, RoundingMode.HALF_UP)))
                    .addLoreLine(GlobalHelper.fixColor(sector.isOnline()
                            ? ("&8» &7Ostatnia aktualizacja &#FFB874" + timeElapsed + " &7sekund temu!")
                            : "&8» &7Ostatnia aktualizacja &cbrak danych"))
                    .addLoreLine(GlobalHelper.fixColor(""))
                    .addLoreLine(GlobalHelper.fixColor(sector.isOnline()
                            ? "&aNaciśnij aby zmienić kanał"
                            : "&cTen sektor jest wyłączony"))
                    .addLoreLine(GlobalHelper.fixColor(""));

            GuiItem guiItem = ItemBuilder.from(item.toItemStack())
                    .glow(sector.isOnline() && sectorName.equals(Lobby.getCfg().getCurrentSector().getSectorName()))
                    .asGuiItem(event -> {
                        if (sectorName.equals(Lobby.getCfg().getCurrentSector().getSectorName())) {
                            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZnajdujesz już się na tym kanale!"));
                            return;
                        }

                        if (!sector.isOnline()) {
                            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTen sektor jest wyłączony!"));
                            return;
                        }

                        if (sector.isAdminMode() && !player.isOp()) {
                            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTen sektor jest w trybie administracyjnym!"));
                            return;
                        }

                        SectorManager.sendToServer(player, sectorName);
                    });

            gui.addItem(guiItem);
        }

        gui.open(player);
    }

}