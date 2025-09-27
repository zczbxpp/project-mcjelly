package pl.zczb.lobby.events;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.zczb.Lobby;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.lobby.queue.QueueInstance;
import pl.zczb.lobby.queue.QueueManager;
import pl.zczb.sectors.managers.SectorManager;
import pl.zczb.sectors.managers.data.Sector;
import pl.zczb.sectors.managers.data.SectorTypeEnum;
import pl.zczb.sectors.tasks.SectorUpdateTask;

import java.util.Objects;
import java.util.stream.Stream;

public class OthersHandler implements Listener {
    private static final String PLAYER_SKIN_BASE64_1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM2MmZkMGQ0MTIzZGVlZWI4MjRhMDBhMTViMDliOTYwMzllZDMzNTk5YzA3N2VmNjc4MjEyMjZhMThmMWYxMSJ9fX0=";
    private static final String PLAYER_SKIN_BASE64_2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZkMTA4MzgzZGZhNWIwMmU4NjYzNTYwOTU0MTUyMGU0ZTE1ODk1MmQ2OGMxYzhmOGYyMDBlYzdlODg2NDJkIn19fQ==";
    private static final String PLAYER_SKIN_BASE64_3 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWY1NzJiNmMwMTc4ZTAxZWM3YzVlMTdiNDE2YzZmYmRjZmJjN2ExYTcxY2FhZDVmMmVkNjZkYzM4NDNjNjRmIn19fQ==";

    private final QueueManager manager;

    public OthersHandler(QueueManager manager) {
        this.manager = manager;
    }


    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            player.setFoodLevel(20);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand() != null) {
            final ItemStack item = player.getInventory().getItemInMainHand();
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {


                if (item.getType() == Material.COMPASS) {

                    Gui gui = Gui.gui()
                            .title(Component.text(GlobalHelper.fixColor("&8Wybierz tryb")))
                            .type(GuiType.HOPPER)

                            .disableAllInteractions()
                            .create();

                    int cashOnline = Stream.of("cashblock_1", "cashblock_2", "cashblock_3", "cashblock_4", "cashblock_5", "event")
                            .map(SectorManager::getSector)
                            .filter(Objects::nonNull)
                            .mapToInt(Sector::getPlayerCount)
                            .sum() + SectorUpdateTask.warta_amount;



                    GuiItem cashblock = dev.triumphteam.gui.builder.item.ItemBuilder.skull().texture(PLAYER_SKIN_BASE64_2)
                            .name(Component.text(GlobalHelper.fixColor("&2* &a&lCashblock &2*")))
                            .lore(
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&8» &7Online: &2" + cashOnline + " graczy")),
                                    Component.text(GlobalHelper.fixColor("&8» &7Wersja: &21.17.1 - 1.20.1")),
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&aNaciśnij aby dołączyć")),
                                    Component.text(GlobalHelper.fixColor(""))
                            )
                            .asGuiItem(evente -> {
                                QueueInstance queue = Lobby.getInstance().getQueueManager().getQueue("cashblock");
                                if (queue != null) {
                                    queue.addPlayer(player);
                                }
                            });

                    GuiItem igrzyska = dev.triumphteam.gui.builder.item.ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTY3ZjJiYzQxMWRkMzhmMzExMWZlMWEzN2UxNzliZGNhYjY2ZTUwOWMyYWZmMjcwMjgxNGQ1ZTA3YTRmYWViNiJ9fX0=")
                            .name(Component.text(GlobalHelper.fixColor("&4* &c&lIgrzyska Śmierci &4*")))
                            .lore(
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&8» &7Online: &c"+SectorManager.getTotalPlayersInMode(SectorTypeEnum.IGRZYSKA)+" graczy")),
                                    Component.text(GlobalHelper.fixColor("&8» &7Wersja: &c1.16.5 - 1.20.1")),
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&aNaciśnij aby dołączyć")),
                                    Component.text(GlobalHelper.fixColor(""))
                            )
                            .asGuiItem(evente -> {
                                QueueInstance queue = Lobby.getInstance().getQueueManager().getQueue("igrzyska");
                                if (queue != null) {
                                    queue.addPlayer(player);
                                }
                            });

                    GuiItem wkrotce = dev.triumphteam.gui.builder.item.ItemBuilder.skull().texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE5OWIwNWI5YTFkYjRkMjliNWU2NzNkNzdhZTU0YTc3ZWFiNjY4MTg1ODYwMzVjOGEyMDA1YWViODEwNjAyYSJ9fX0=")
                            .name(Component.text(GlobalHelper.fixColor("&8* &7&lWkrótce.. &8*")))
                            .lore(
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&8» &7Online: &80 graczy")),
                                    Component.text(GlobalHelper.fixColor("&8» &7Wersja: &8?")),
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&aNaciśnij aby dołączyć")),
                                    Component.text(GlobalHelper.fixColor(""))
                            )
                            .asGuiItem(evente -> {
                            });



                    GuiItem boxpvp = dev.triumphteam.gui.builder.item.ItemBuilder.skull().texture(PLAYER_SKIN_BASE64_1)
                            .name(Component.text(GlobalHelper.fixColor("&6* &e&lBoxpvp &6*")))
                            .lore(
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&8» &7Online: &60 graczy")),
                                    Component.text(GlobalHelper.fixColor("&8» &7Wersja: &61.16.5 - 1.20.1")),
                                    Component.text(GlobalHelper.fixColor("")),
                                    Component.text(GlobalHelper.fixColor("&aNaciśnij aby dołączyć")),
                                    Component.text(GlobalHelper.fixColor(""))
                            )
                            .asGuiItem(evente -> {

                            });


                    gui.setItem(0, wkrotce);
                    gui.setItem(1, igrzyska);

                    gui.setItem(2, cashblock);
                    gui.setItem(3, wkrotce);
                    gui.setItem(4, wkrotce);

                    gui.open(player);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getType() != Material.REDSTONE) return;

        event.setCancelled(true);
        manager.removeFromAll(p);
        p.sendMessage("§cOpuściłeś kolejkę.");
    }
}
