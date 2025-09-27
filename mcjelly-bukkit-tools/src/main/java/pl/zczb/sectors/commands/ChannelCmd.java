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
import org.bukkit.entity.Player;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.serialization.SerializationHelper;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.helpers.OtherHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Command(name = "ch", aliases = {"kanal", "sector"})
public class ChannelCmd {
    @Execute
    public void openChannelSelector(@Context Player player) {
        if (Tools.getSectorConfig().getCurrentSector().getSectorName().endsWith("_event")) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNa kanale eventowym nie możesz tego używać!"));

            return;
        }
        if (!OtherHelper.isInRegion(player, "spawn")) {
            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FTej komendy możesz użyć tylko na spawnie!"));
            return;
        }


        String prefix = Tools.getSectorConfig()
                .getCurrentSector()
                .getSectorType()
                .toString()
                .toLowerCase() + "_";

        Map<String, Sector> sectors = SectorManager.getAllSectors();

        List<String> sortedSectorNames = sectors.keySet().stream()
                .filter(name ->
                        name.startsWith(prefix) &&
                                !name.endsWith("_event")
                )
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
            long lastUpdateSeconds = lastUpdateMillis / 1000;
            long currentTimeSeconds = System.currentTimeMillis() / 1000;
            long timeElapsed = currentTimeSeconds - lastUpdateSeconds;

            String sectorLabel = sectorName.equals(Tools.getSectorConfig().getCurrentSector().getSectorName())
                    ? "&8(&#FF7D00Jestes tutaj&8)"
                    : "";


            pl.zczb.tools.helpers.ItemBuilder item = new pl.zczb.tools.helpers.ItemBuilder(sector.isOnline() ? (sectorName.equals(Tools.getSectorConfig().getCurrentSector().getSectorName()) ? Material.CHEST_MINECART : Material.FURNACE_MINECART) : Material.MINECART)
                    .setName(GlobalHelper.fixColor("&#FF7D00* &7Kanał: &#FFB874" + sectorName + " &#FF7D00* " + sectorLabel))
                    .addLoreLine(GlobalHelper.fixColor(""))
                    .addLoreLine(GlobalHelper.fixColor("&8» &7Online: &#FFB874" + sector.getPlayerCount() + " graczy"))
                    .addLoreLine(GlobalHelper.fixColor("&8» &7Tps: &#FFB874" + BigDecimal.valueOf(sector.getTps()).setScale(2, RoundingMode.HALF_UP)))
                    .addLoreLine(GlobalHelper.fixColor((sector.isOnline() ? ("&8» &7Ostatnia aktualizacja &#FFB874" + timeElapsed + " &7sekund temu!") : "&8» &7Ostatnia aktualizacja &cbrak danych")))
                    .addLoreLine(GlobalHelper.fixColor(""))
                    .addLoreLine(GlobalHelper.fixColor((sector.isOnline() ? "&aNaciśnij aby zmienić kanał" : "&cTen sektory jest wyłączony")))
                    .addLoreLine(GlobalHelper.fixColor(""));

            GuiItem guiItem = ItemBuilder.from(item.toItemStack())
                    .glow(sector.isOnline() && sectorName.equals(Tools.getSectorConfig().getCurrentSector().getSectorName()))
                    .asGuiItem(event -> {

                        if (sectorName.equals(Tools.getSectorConfig().getCurrentSector().getSectorName())) {
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

                        UserDataModel user = Tools.getInstance().getUserHandler().getPlayer(player);

                        if (user.isChangingSector()) {
                            player.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FZmieniasz już sektor, jeśli uważasz że to błąd zgłoś to administratorowi lub wejdz ponownie!"));
                            return;
                        }

                        SectorManager.transfer(player, sectorName);

                    });


            gui.addItem(guiItem);
        }

        gui.open(player);


    }

}


