package pl.zczb.tools.objects.impl;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.zczb.Tools;

import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.serialization.SerializationHelper;
import pl.zczb.tools.database.user.models.UserDataModel;
import pl.zczb.tools.database.user.models.UserEnderchests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EnderChestsManager {

    private static final Map<Integer, String> ENDERCHEST_PERMISSIONS = new HashMap<>();
    private static final Map<Integer, String> ENDERCHEST_RANK_NAMES = new HashMap<>();
    private static final int ENDERCHEST_SIZE = 54;

    static {
        ENDERCHEST_PERMISSIONS.put(1, "zczb.gracz");
        ENDERCHEST_PERMISSIONS.put(2, "zczb.vip");
        ENDERCHEST_PERMISSIONS.put(3, "zczb.svip");
        ENDERCHEST_PERMISSIONS.put(4, "zczb.mvip");
        ENDERCHEST_PERMISSIONS.put(5, "zczb.donator");

        ENDERCHEST_RANK_NAMES.put(1, "Gracz");
        ENDERCHEST_RANK_NAMES.put(2, "Vip");
        ENDERCHEST_RANK_NAMES.put(3, "Svip");
        ENDERCHEST_RANK_NAMES.put(4, "Mvip");
        ENDERCHEST_RANK_NAMES.put(5, "Jelly");
    }

    private static final String ENDERCHEST_SKULL_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdhY2I4Yjk5ZDQ3OGJhMzUwNTNlOWYyMTJhY2I1YzU1Y2MxNDQ4NDA0NjhmZDAyNDJiMzlmNWJkNzVhY2I0MSJ9fX0=";

    public static void openEnderGui(Player p) {
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Enderchesty")))
                .rows(3)
                .disableAllInteractions()
                .create();

        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        UserEnderchests userEnderchests = u.getUserEnderchests();

        GuiItem orange = ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE).name(Component.text(GlobalHelper.fixColor("&f"))).asGuiItem();
        GuiItem yellow = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).name(Component.text(GlobalHelper.fixColor("&f"))).asGuiItem();
        GuiItem white = ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE).name(Component.text(GlobalHelper.fixColor("&f"))).asGuiItem();
        GuiItem gray = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text(GlobalHelper.fixColor("&f"))).asGuiItem();

        Function<Integer, GuiItem> createEnderchestItem = (i) -> {
            String requiredPermission = ENDERCHEST_PERMISSIONS.getOrDefault(i, "zczb.gracz");
            String requiredRankName = ENDERCHEST_RANK_NAMES.getOrDefault(i, "Gracz");
            boolean playerHasPermission = p.hasPermission(requiredPermission);

            String permissionLore = GlobalHelper.fixColor(" &7Wymagana ranga: &e" + requiredRankName);
            String accessStatus = playerHasPermission ? "&#6AF454TAK" : "&#F45454NIE";
            String actionLore = playerHasPermission ? GlobalHelper.fixColor("&aKliknij aby otworzyć") : GlobalHelper.fixColor("&cBrakuje Ci wymaganej rangi!");



            pl.zczb.cashblock.helpers.ItemBuilder item = new pl.zczb.cashblock.helpers.ItemBuilder(Material.PLAYER_HEAD)
                    .setName(GlobalHelper.fixColor(String.format("&#9154F4E&#8C55EFn&#8756EAd&#8257E5e&#7D57DFr&#7858DAc&#7359D5h&#6D5AD0e&#685BCBs&#635CC6t &#595DBB#&#545EB6%d", i)))
                    .addLoreLine(GlobalHelper.fixColor(""))
                    .addLoreLine(GlobalHelper.fixColor(" &7Możesz przechowywywać w nim przedmioty."))
                    .addLoreLine(permissionLore)
                    .addLoreLine(GlobalHelper.fixColor(" &7Posiadasz: " + accessStatus))
                    .addLoreLine(GlobalHelper.fixColor(""))
                    .addLoreLine(actionLore);


            return ItemBuilder.from(item.toItemStack()).setSkullTexture(ENDERCHEST_SKULL_TEXTURE)
                    .asGuiItem(event -> {
                        event.setCancelled(true);
                        handleEnderchestClick(p, u, userEnderchests, i);
                    });
        };

        gui.setItem(0, orange); gui.setItem(1, yellow); gui.setItem(2, white); gui.setItem(3, gray);
        gui.setItem(5, gray); gui.setItem(6, white); gui.setItem(7, yellow); gui.setItem(8, orange);
        gui.setItem(9, yellow); gui.setItem(10, gray);
        gui.setItem(16, gray); gui.setItem(17, yellow);
        gui.setItem(18, orange); gui.setItem(19, yellow); gui.setItem(20, white); gui.setItem(21, gray);
        gui.setItem(23, gray); gui.setItem(24, white); gui.setItem(25, yellow); gui.setItem(26, orange);

        gui.setItem(11, createEnderchestItem.apply(1));
        gui.setItem(12, createEnderchestItem.apply(2));
        gui.setItem(13, createEnderchestItem.apply(3));
        gui.setItem(14, createEnderchestItem.apply(4));
        gui.setItem(15, createEnderchestItem.apply(5));

        gui.open(p);
    }

    private static void handleEnderchestClick(Player p, UserDataModel u, UserEnderchests userEnderchests, int enderchestNum) {
        String requiredPermission = ENDERCHEST_PERMISSIONS.getOrDefault(enderchestNum, "zczb.gracz");
        String requiredRankName = ENDERCHEST_RANK_NAMES.getOrDefault(enderchestNum, "Gracz");
        boolean playerHasPermission = p.hasPermission(requiredPermission);

        if (playerHasPermission) {
            openEnderWithItems(p, enderchestNum);
        } else {
            p.sendMessage(GlobalHelper.fixColor("&cNie posiadasz wymaganej rangi do tego enderchestu: &e" + requiredRankName + "&c!"));
        }
    }

    public static void openEnderWithItems(Player p, int lvl) {
        UserDataModel u = Tools.getInstance().getUserHandler().getPlayer(p);
        UserEnderchests userEnderchests = u.getUserEnderchests();

        byte[] enderchestBytes = null;
        String guiTitle = GlobalHelper.fixColor("&8Enderchest #" + lvl);

        switch (lvl) {
            case 1: enderchestBytes = userEnderchests.getEnder_1(); break;
            case 2: enderchestBytes = userEnderchests.getEnder_2(); break;
            case 3: enderchestBytes = userEnderchests.getEnder_3(); break;
            case 4: enderchestBytes = userEnderchests.getEnder_4(); break;
            case 5: enderchestBytes = userEnderchests.getEnder_5(); break;
            default: p.sendMessage(GlobalHelper.fixColor("&cBłąd: Nieprawidłowy numer enderchestu.")); return;
        }

        ItemStack[] currentEnderchestContent = (enderchestBytes != null && enderchestBytes.length > 0) ?
                SerializationHelper.deserializeInventoryFromBytes(enderchestBytes) :
                new ItemStack[ENDERCHEST_SIZE];
        if (currentEnderchestContent.length != ENDERCHEST_SIZE) {
            ItemStack[] resizedContent = new ItemStack[ENDERCHEST_SIZE];
            System.arraycopy(currentEnderchestContent, 0, resizedContent, 0, Math.min(currentEnderchestContent.length, ENDERCHEST_SIZE));
            currentEnderchestContent = resizedContent;
        }

        Gui enderGui = Gui.gui()
                .title(Component.text(guiTitle))
                .rows(6)
                .create();

        for (int i = 0; i < currentEnderchestContent.length; i++) {
            if (currentEnderchestContent[i] != null) {
                enderGui.setItem(i, ItemBuilder.from(currentEnderchestContent[i]).asGuiItem());
            }
        }

        enderGui.setCloseGuiAction(event -> {
            byte[] serializedBytes = SerializationHelper.serializeInventoryToBytes(enderGui.getInventory());
            switch (lvl) {
                case 1: userEnderchests.setEnder_1(serializedBytes); break;
                case 2: userEnderchests.setEnder_2(serializedBytes); break;
                case 3: userEnderchests.setEnder_3(serializedBytes); break;
                case 4: userEnderchests.setEnder_4(serializedBytes); break;
                case 5: userEnderchests.setEnder_5(serializedBytes); break;
            }
            Tools.getInstance().getUserHandler().updateUser(u);
        });

        enderGui.open(p);
    }
}