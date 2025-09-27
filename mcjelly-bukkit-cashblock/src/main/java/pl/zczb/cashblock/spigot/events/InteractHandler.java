package pl.zczb.cashblock.spigot.events;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.DataUtil;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.helpers.SerializationHelper;
import pl.zczb.cashblock.objects.impl.PickaxeManager;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.DonateBroadcastPacket;
import pl.zczb.packets.TurboKasaUserPacket;
import pl.zczb.redis.packet.Packet;

public class InteractHandler implements Listener {
    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        BoostsHandler.checkBonuses(p);
    }

    @EventHandler
    public void onSoloRandomTeleport(PlayerInteractEvent event) {
        Block clicked = event.getClickedBlock();
        if (clicked == null) {
            return;
        }
        Player p = event.getPlayer();
        if (clicked.getType() == Material.STONE_BUTTON) {
            Button button = (Button) clicked.getState().getData();
            Block face = clicked.getRelative(button.getAttachedFace());
            if (face.getType() == Material.NOTE_BLOCK) {
                Gui gui = Gui.gui()
                        .title(Component.text(GlobalHelper.fixColor("&8Losowy Teleport")))
                        .rows(5)

                        .disableAllInteractions()
                        .create();


                GuiItem gracz = ItemBuilder.from(Material.WOODEN_AXE)
                        .name(Component.text(GlobalHelper.fixColor("&5&lᴛᴇʟᴇᴘᴏʀᴛ &f\uE801")))
                        .lore(
                                Component.text(GlobalHelper.fixColor("")),
                                Component.text(GlobalHelper.fixColor(" &8- &7Zasięg &f1000x1000")),
                                Component.text(GlobalHelper.fixColor("")),
                                Component.text(GlobalHelper.fixColor("&aKliknij aby się teleportować"))
                        )
                        .asGuiItem(evente -> {
                            gui.close(p);
                            OtherHelper.randomTeleport(p, 200, 1000, 200, 1000);
                        });

                GuiItem vipplus = ItemBuilder.from(Material.GOLDEN_AXE)
                        .name(Component.text(GlobalHelper.fixColor("&5&lᴛᴇʟᴇᴘᴏʀᴛ &8(&f\uE802&7,&f\uE803&7,&f\uE804&8)")))
                        .lore(
                                Component.text(GlobalHelper.fixColor("")),
                                Component.text(GlobalHelper.fixColor(" &8- &7Zasięg &f3000x3000")),
                                Component.text(GlobalHelper.fixColor("")),
                                Component.text(GlobalHelper.fixColor("&aKliknij aby się teleportować"))
                        )
                        .asGuiItem(evente -> {
                            gui.close(p);
                            if (!p.hasPermission("zczb.vip")) {
                                p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz rangi aby użyć tego teleportu!"));
                                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz rangi aby użyć teleportu!"));
                                return;
                            }
                            OtherHelper.randomTeleport(p, 1500, 3000, 1500, 3000);
                        });

                GuiItem donator = ItemBuilder.from(Material.DIAMOND_AXE)
                        .name(Component.text(GlobalHelper.fixColor("&5&lᴛᴇʟᴇᴘᴏʀᴛ &f\uE805")))
                        .lore(
                                Component.text(GlobalHelper.fixColor("")),
                                Component.text(GlobalHelper.fixColor(" &8- &7Zasięg &f5000x5000")),
                                Component.text(GlobalHelper.fixColor("")),
                                Component.text(GlobalHelper.fixColor("&aKliknij aby się teleportować"))
                        )
                        .asGuiItem(evente -> {
                            gui.close(p);
                            if (!p.hasPermission("zczb.donator")) {
                                p.sendMessage(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FNie posiadasz rangi aby użyć tego teleportu!"));
                                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie posiadasz rangi aby użyć teleportu!"));
                                return;
                            }
                            OtherHelper.randomTeleport(p, 3500, 5000, 3500, 5000);
                        });

                GuiItem white = ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
                        .name(Component.text(GlobalHelper.fixColor(" ")))
                        .asGuiItem();
                GuiItem yellow = ItemBuilder.from(Material.MAGENTA_STAINED_GLASS_PANE)
                        .name(Component.text(GlobalHelper.fixColor(" ")))
                        .asGuiItem();
                GuiItem orange = ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE)
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

                gui.setItem(20, gracz);
                gui.setItem(22, vipplus);
                gui.setItem(24, donator);

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


                gui.open(p);

            }

        }
    }


    @EventHandler
    public void onKilofOsobisty(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            if (!PickaxeManager.isPersonalPickaxe(itemStack)) {
                return;
            }

            if (!PickaxeManager.isOwner(itemStack, p)) {
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie jesteś włascicielem kilofa!"));
                return;
            }
            PickaxeManager.openPickaxeMenu(p);
        }
    }

    @EventHandler
    public void onDonateChest(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null) {
            Location clicked = e.getClickedBlock().getLocation();
            Location target = SerializationHelper.stringToLoc(Cashblock.getCashblockConfig().getDonate_chest());

            if (clicked.getBlockX() == target.getBlockX() && clicked
                    .getBlockY() == target.getBlockY() && clicked
                    .getBlockZ() == target.getBlockZ() && clicked
                    .getWorld().getName().equals(target.getWorld().getName())) {

                e.setCancelled(true);
                openDonateChest(p);
            }
        }
    }

    @EventHandler
    public void onUseInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack itemStack = p.getInventory().getItemInMainHand();

        if ((e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (itemStack.getType() != Material.BOOK && itemStack.getType() != Material.PLAYER_HEAD && itemStack.getType() != Material.PAPER && itemStack.getType() != Material.PAINTING) {
                return;
            }
            if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
                return;
            }


            String displayName = itemStack.getItemMeta().getDisplayName();
            UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);
            if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Voucher na &eVIP &8(&f7 dni&8)"))) {
                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "luckperms user " + p.getName() + " parent addtemp vip 7d");
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś voucher na VIP'a 7 dni"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Voucher na &6SVIP &8(&f7 dni&8)"))) {
                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "luckperms user " + p.getName() + " parent addtemp svip 7d");

                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś voucher na SVIP'a 7 dni"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Voucher na &bMVIP &8(&f7 dni&8)"))) {
                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "luckperms user " + p.getName() + " parent addtemp mvip 7d");
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś voucher na MVIP'a 7 dni"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Voucher na &fFly &8(&f3 dni&8)"))) {
                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission settemp zczb.fly true 3d");
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś voucher na Fly'a 3 dni"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Apsik"))) {

                u.getUserPets().setApsik(true);
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś peta aktywuj pod /pety"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Gburek"))) {
                u.getUserPets().setGburek(true);
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś peta aktywuj pod /pety"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Wesolek"))) {
                u.getUserPets().setWesolek(true);
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś peta aktywuj pod /pety"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Gapcio"))) {
                u.getUserPets().setGapcio(true);
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś peta aktywuj pod /pety"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Aronek"))) {
                u.getUserPets().setAronek(true);
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś peta aktywuj pod /pety"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Voucher na &bTurboKase &8(&f15 min&8)"))) {
                long eventtime = DataUtil.parseDateDiff("15min", true);

                Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new TurboKasaUserPacket(eventtime, u.getNick()));


                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś voucher turbokase"));
            } else if (displayName.equalsIgnoreCase(GlobalHelper.fixColor("&7Voucher na &bTurboKase &8(&f30 min&8)"))) {
                long eventtime = DataUtil.parseDateDiff("30min", true);

                Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new TurboKasaUserPacket(eventtime, u.getNick()));

                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aUżyłeś voucher turbokase"));
            } else {
                return;
            }

            if (itemStack.getAmount() > 1) {
                itemStack.setAmount(itemStack.getAmount() - 1);
            } else {
                p.getInventory().setItemInMainHand(null);
            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        Player player;
        HumanEntity humanEntity = event.getEntity();
        if (humanEntity instanceof Player) {
            player = (Player) humanEntity;
        } else {
            return;
        }
        if (player.hasPermission("zczb.vip") &&
                event.getFoodLevel() < player.getFoodLevel()) {
            player.setFoodLevel(20);
            event.setCancelled(true);
        }
    }

    public void openDonateChest(Player p) {
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Skrzynia donejtów")))
                .rows(5)

                .disableAllInteractions()
                .create();
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        GuiItem exp = ItemBuilder.from(Material.GOLD_NUGGET)
                .name(Component.text(GlobalHelper.fixColor("&6&lWESPRZYJ ADMINISTRACJE I SERWER")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Możesz wepsrzeć &fserwer &7i &fAdministracje")),
                        Component.text(GlobalHelper.fixColor(" &7symboliczną &fpiątka &8(&e5vPLN&8)")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Wzamian otrzymasz: &6&lLegendarny klucz")),
                        Component.text(GlobalHelper.fixColor(" &7Zapłacisz: &a5vPLN")),
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor("&aKliknij aby wpłacić"))
                )
                .asGuiItem(evente -> {
                    gui.close(p);
                    if (u.getPln() < 5) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz 5 vPLN"));
                        return;
                    }
                    u.removePln(5);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase give " + p.getName() + " legendarna 1");
                    Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new DonateBroadcastPacket(p.getName()));
                });


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

        gui.setItem(22, exp);

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


        gui.open(p);
    }
}


