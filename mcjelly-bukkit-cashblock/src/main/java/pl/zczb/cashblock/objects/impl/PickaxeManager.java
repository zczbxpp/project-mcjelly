package pl.zczb.cashblock.objects.impl;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.helpers.GlobalHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PickaxeManager {

    public enum UpgradeType {
        EXP("ʙᴏɴᴜѕ ᴇxᴘᴀ"),
        MONEY("ʙᴏɴᴜѕ ᴠᴘʟɴ"),
        KEY("ѕᴢᴀɴѕᴀ ɴᴀ ᴋʟᴜᴄᴢᴇ"),
        EXPLOSION("ᴇᴋѕᴘʟᴏᴢᴊᴀ"),
        PYROMANIAC("ᴘɪʀᴏᴍᴀɴ");

        private final String loreName;

        UpgradeType(String loreName) {
            this.loreName = loreName;
        }

        public String getLoreName() {
            return loreName;
        }
    }

    public static boolean isPersonalPickaxe(ItemStack item) {
        if (item == null || item.getType() != Material.NETHERITE_PICKAXE || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName() || !meta.hasLore()) return false;

        String displayName = ChatColor.stripColor(meta.getDisplayName());
        if (!displayName.contains("Kilof osobisty")) return false;

        return true;
    }


    public static boolean isOwner(ItemStack item, Player player) {
        if (!isPersonalPickaxe(item)) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;

        List<String> lore = item.getItemMeta().getLore();
        if (lore == null) return false;

        for (String line : lore) {
            String plain = GlobalHelper.fixColor(line);
            if (plain.contains("Własciciel")) {
                return plain.toLowerCase().contains(player.getName().toLowerCase());
            }
        }
        return false;
    }

    public static int getPickaxeLevel(ItemStack item) {
        if (!isPersonalPickaxe(item)) return 0;
        String name = GlobalHelper.fixColor(item.getItemMeta().getDisplayName());
        try {
            String[] parts = name.split(" ");
            return Integer.parseInt(parts[2]);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getUpgradeLevel(ItemStack item, UpgradeType type) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0;
        List<String> lore = item.getItemMeta().getLore();
        if (lore == null) return 0;

        for (String line : lore) {
            String plain = GlobalHelper.fixColor(line);
            if (plain.contains(type.getLoreName())) {
                try {
                    String[] parts = plain.split(" ");
                    String last = parts[parts.length - 1];
                    return Integer.parseInt(last);
                } catch (Exception ignored) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public static ItemStack setUpgradeLevel(ItemStack item, UpgradeType type, int value) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return item;

        ItemMeta meta = item.getItemMeta();
        List<String> newLore = new ArrayList<>();

        for (String line : meta.getLore()) {
            String plain = GlobalHelper.fixColor(line);
            if (plain.contains(type.getLoreName())) {
                String updated = GlobalHelper.fixColor(" &6&l| " + getColorFor(type) + type.getLoreName() + " " + value);
                newLore.add(updated);
            } else {
                newLore.add(line);
            }
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addUpgradeLevel(ItemStack item, UpgradeType type, int amount) {
        int current = getUpgradeLevel(item, type);
        int updated = current + amount;

        return setUpgradeLevel(item, type, updated);
    }

    public static ItemStack createPickaxe(Player owner) {

        pl.zczb.cashblock.helpers.ItemBuilder item = new pl.zczb.cashblock.helpers.ItemBuilder(Material.NETHERITE_PICKAXE)
                .setName(GlobalHelper.fixColor("&#A1D42AK&#A6D129i&#ABCE29l&#B0CB28o&#B5C827f &#BFC226o&#C4BF25s&#C9BC24o&#CEB924b&#D3B623i&#D8B322s&#DDB022t&#E2AD21y"))
                .addEnchant(Enchantment.DIG_SPEED, 10)
                .addEnchant(Enchantment.DURABILITY, 10)
                .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&7Jest to kilof osobisty każdego gracza."))
                .addLoreLine(GlobalHelper.fixColor("&7Własciciel &f" + owner.getName()))
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&#DBA100Ulepszenia"))
                .addLoreLine(GlobalHelper.fixColor(" &#DBA100&l| &#3C7CC4ʙᴏɴᴜѕ ᴇxᴘᴀ 0"))
                .addLoreLine(GlobalHelper.fixColor(" &#DBA100&l| &#75C43Cʙᴏɴᴜѕ ᴠᴘʟɴ 0"))
                .addLoreLine(GlobalHelper.fixColor(" &#DBA100&l| &#543CC4ѕᴢᴀɴѕᴀ ɴᴀ ᴋʟᴜᴄᴢᴇ 0"))
                .addLoreLine(GlobalHelper.fixColor(" &#DBA100&l| &#C43C3Cᴇᴋѕᴘʟᴏᴢᴊᴀ 0"))
                .addLoreLine(GlobalHelper.fixColor(" &#DBA100&l| &#C46E3Cᴘɪʀᴏᴍᴀɴ 0"))
                .addLoreLine(GlobalHelper.fixColor(""))
                .addLoreLine(GlobalHelper.fixColor("&fKliknij &aPPM trzymajac kilof &faby ulepszyć"));


        return item.toItemStack();
    }

    public static String getColorFor(UpgradeType type) {
        return switch (type) {
            case EXP -> "&#3C7CC4";
            case MONEY -> "&#75C43C";
            case KEY -> "&#543CC4";
            case EXPLOSION -> "&#C43C3C";
            case PYROMANIAC -> "&#C46E3C";
        };
    }

    public static void openPickaxeMenu(Player p) {
        ItemStack itemStack = p.getInventory().getItemInMainHand();
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Kilof osobisty")))
                .rows(5)
                .disableAllInteractions()
                .create();
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        GuiItem exp = ItemBuilder.from(Material.EXPERIENCE_BOTTLE)
                .name(OtherHelper.mm("<color:#3C7CC4>ʙᴏɴᴜѕ ᴇxᴘᴀ"))
                .glow()
                .lore(
                        OtherHelper.mm("<dark_gray>Zwiększasz szanse na znalezienie"),
                        OtherHelper.mm("<dark_gray>Dodatkowego expa podczas farmienia."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#3C7CC4><bold>|</bold> <gray>Poziom: <color:#68AFFF>" + getUpgradeLevel(itemStack, UpgradeType.EXP) + " <dark_gray>/ <color:#1C62B1>2,000"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#3C7CC4>Kliknij, aby ulepszać")
                ).asGuiItem(event -> {
                    openUpgradePickaxeMenu(p, UpgradeType.EXP);
                });

        GuiItem vpln = ItemBuilder.from(Material.GREEN_DYE)
                .name(OtherHelper.mm("<color:#75C43C>ʙᴏɴᴜѕ ᴠᴘʟɴ"))
                .lore(
                        OtherHelper.mm("<dark_gray>Zwiększasz szanse na znalezienie"),
                        OtherHelper.mm("<dark_gray>Dodatkowej kasy podczas farmienia."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#75C43C><bold>|</bold> <gray>Poziom: <color:#B3FF7C>" + getUpgradeLevel(itemStack, UpgradeType.MONEY) + " <dark_gray>/ <color:#599B29>2,000"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#75C43C>Kliknij, aby ulepszać")
                ).asGuiItem(event -> {
                    openUpgradePickaxeMenu(p, UpgradeType.MONEY);
                });

        GuiItem keys = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(OtherHelper.mm("<color:#543CC4>ѕᴢᴀɴѕᴀ ɴᴀ ᴋʟᴜᴄᴢᴇ"))
                .glow()
                .lore(
                        OtherHelper.mm("<dark_gray>Zwiększasz szanse na znalezienie"),
                        OtherHelper.mm("<dark_gray>Kluczy do skrzynek podczas farmienia."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#543CC4><bold>|</bold> <gray>Poziom: <color:#937CFF>" + getUpgradeLevel(itemStack, UpgradeType.KEY) + " <dark_gray>/ <color:#3922A4>2,000"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#543CC4>Kliknij, aby ulepszać")
                ).asGuiItem(event -> {
                    openUpgradePickaxeMenu(p, UpgradeType.KEY);
                });

        GuiItem explosion = ItemBuilder.from(Material.TNT)
                .name(OtherHelper.mm("<color:#C43C3C>ᴇᴋѕᴘʟᴏᴢᴊᴀ"))
                .lore(
                        OtherHelper.mm("<dark_gray>Zwiększasz szanse na eksplozje"),
                        OtherHelper.mm("<dark_gray>tnt podczas farmienia."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C43C3C><bold>|</bold> <gray>Poziom: <color:#EF6464>" + getUpgradeLevel(itemStack, UpgradeType.EXPLOSION) + " <dark_gray>/ <color:#A02424>2,000"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C43C3C>Kliknij, aby ulepszać")
                ).asGuiItem(event -> {
                    openUpgradePickaxeMenu(p, UpgradeType.EXPLOSION);
                });

        GuiItem pyro = ItemBuilder.from(Material.BLAZE_POWDER)
                .name(OtherHelper.mm("<color:#C46E3C>ᴘɪʀᴏᴍᴀɴ"))
                .lore(
                        OtherHelper.mm("<dark_gray>Zwiększasz szanse na ognisty"),
                        OtherHelper.mm("<dark_gray>pył podczas farmienia."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C46E3C><bold>|</bold> <gray>Poziom: <color:#EE9D6E>" + getUpgradeLevel(itemStack, UpgradeType.PYROMANIAC) + " <dark_gray>/ <color:#B05926>2,000"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C46E3C>Kliknij, aby ulepszać")
                ).asGuiItem(event -> {
                    openUpgradePickaxeMenu(p, UpgradeType.PYROMANIAC);
                });


        GuiItem nokeys = ItemBuilder.from(Material.TRIPWIRE_HOOK)
                .name(OtherHelper.mm("<color:#543CC4>ѕᴢᴀɴѕᴀ ɴᴀ ᴋʟᴜᴄᴢᴇ <dark_gray>(<color:#C44040>ɴɪᴇᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ<dark_gray>)"))
                .glow()
                .lore(
                        OtherHelper.mm("<dark_gray>Zwiększasz szanse na znalezienie"),
                        OtherHelper.mm("<dark_gray>Kluczy do skrzynek podczas farmienia."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#543CC4><bold>|</bold> <gray>Poziom: <color:#937CFF>" + getUpgradeLevel(itemStack, UpgradeType.KEY) + " <dark_gray>/ <color:#3922A4>2,000"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Za niski prestiż wbij go <color:#C44040>/prestiz"),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Wymagany prestiż na odblokowanie <color:#C44040>5"),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Musisz wbić jeszcze <color:#C44040>" + (5 - u.getUserPrestiz().getPrestiz()) + " <color:#E75E5E>prestiży")
                ).asGuiItem(event -> {

                });

        GuiItem noexplosion = ItemBuilder.from(Material.TNT)
                .name(OtherHelper.mm("<color:#C43C3C>ᴇᴋѕᴘʟᴏᴢᴊᴀ <dark_gray>(<color:#C44040>ɴɪᴇᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ<dark_gray>)"))
                .lore(
                        OtherHelper.mm("<dark_gray>Zwiększasz szanse na eksplozje"),
                        OtherHelper.mm("<dark_gray>tnt podczas farmienia."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C43C3C><bold>|</bold> <gray>Poziom: <color:#EF6464>" + getUpgradeLevel(itemStack, UpgradeType.EXPLOSION) + " <dark_gray>/ <color:#A02424>2,000"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Za niski prestiż wbij go <color:#C44040>/prestiz"),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Wymagany prestiż na odblokowanie <color:#C44040>15"),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Musisz wbić jeszcze <color:#C44040>" + (15 - u.getUserPrestiz().getPrestiz()) + " <color:#E75E5E>prestiży")
                ).asGuiItem(event -> {

                });

        GuiItem nopyro = ItemBuilder.from(Material.BLAZE_POWDER)
                .name(OtherHelper.mm("<color:#C46E3C>ᴘɪʀᴏᴍᴀɴ <dark_gray>(<color:#C44040>ɴɪᴇᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ<dark_gray>)"))
                .lore(
                        OtherHelper.mm("<dark_gray>Zwiększasz szanse na ognisty"),
                        OtherHelper.mm("<dark_gray>pył podczas farmienia."),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C46E3C><bold>|</bold> <gray>Poziom: <color:#EE9D6E>" + getUpgradeLevel(itemStack, UpgradeType.PYROMANIAC) + " <dark_gray>/ <color:#B05926>2,000"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Za niski prestiż wbij go <color:#C44040>/prestiz"),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Wymagany prestiż na odblokowanie <color:#C44040>10"),
                        OtherHelper.mm("<color:#C44040>✖ <color:#E75E5E>Musisz wbić jeszcze <color:#C44040>" + (10 - u.getUserPrestiz().getPrestiz()) + " <color:#E75E5E>prestiży")

                ).asGuiItem(event -> {

                });

        GuiItem orange = ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor("&f")))
                .asGuiItem();
        GuiItem yellow = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor("&f")))
                .asGuiItem();
        GuiItem white = ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor("&f")))
                .asGuiItem();
        GuiItem gray = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor("&f")))
                .asGuiItem();

        gui.setItem(0, orange);
        gui.setItem(1, yellow);
        gui.setItem(2, white);
        gui.setItem(3, gray);

        gui.setItem(4, new GuiItem(itemStack));

        gui.setItem(5, gray);
        gui.setItem(6, white);
        gui.setItem(7, yellow);
        gui.setItem(8, orange);
        gui.setItem(9, yellow);
        gui.setItem(10, gray);
        gui.setItem(11, gray);
        gui.setItem(12, gray);
        gui.setItem(13, gray);
        gui.setItem(14, gray);
        gui.setItem(15, gray);
        gui.setItem(16, gray);
        gui.setItem(17, yellow);
        gui.setItem(18, white);

        gui.setItem(19, exp);
        gui.setItem(20, vpln);
        gui.setItem(21, (u.getUserPrestiz().getPrestiz() >= 5) ? keys : nokeys);
        gui.setItem(22, (u.getUserPrestiz().getPrestiz() >= 10) ? pyro : nopyro);
        gui.setItem(23, (u.getUserPrestiz().getPrestiz() >= 15) ? explosion : noexplosion);


        gui.setItem(26, white);
        gui.setItem(27, yellow);
        gui.setItem(35, yellow);

        gui.setItem(36, orange);
        gui.setItem(37, yellow);
        gui.setItem(38, white);
        gui.setItem(39, gray);
        gui.setItem(40, gray);
        gui.setItem(41, gray);
        gui.setItem(42, white);
        gui.setItem(43, yellow);
        gui.setItem(44, orange);
        gui.open(p);

    }


    private static void openUpgradePickaxeMenu(Player p, UpgradeType upgradeType) {
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Kilof osobisty")))
                .rows(3)
                .disableAllInteractions()
                .create();

        ItemStack itemStack = p.getInventory().getItemInMainHand();
        GuiItem upgrade_max = ItemBuilder.from(Material.ANVIL)
                .name(OtherHelper.mm("<color:#3AE253><bold>Maksymalne ulepszenie"))
                .lore(
                        OtherHelper.mm("<gray>Kup tyle ulepszeń na ile"),
                        OtherHelper.mm("<gray>cię stać za <aqua><bold>Odłamki kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#3AE253>Informacje"),
                        OtherHelper.mm("<color:#3AE253><bold>|</bold> <gray>Poziomów: <white>" + getAmountOfItem(p)),
                        OtherHelper.mm("<color:#3AE253><bold>|</bold> <gray>Koszt: <aqua><bold>" + getAmountOfItem(p) + " Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#00FF26>Kliknij, aby zakupić")
                ).asGuiItem(event -> {
                    gui.close(p);

                    int currentLevel = getUpgradeLevel(itemStack, upgradeType);
                    if (currentLevel >= 2000) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cOsiągnięto maksymalny poziom!"));
                        return;
                    }

                    int shards = getAmountOfItem(p);
                    int missing = 2000 - currentLevel;

                    if (shards <= 0) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz wystarczająco odłamków kosmosu!"));
                        return;
                    }

                    int buyAmount = Math.min(shards, missing);

                    deleteOdlamki(p, buyAmount);
                    addUpgradeLevel(itemStack, upgradeType, buyAmount);

                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aZakupiono " + buyAmount + " poziomów ulepszenia!"));
                });


        GuiItem upgrade_1 = ItemBuilder.from(Material.LIME_DYE)
                .name(OtherHelper.mm("<color:#2EB842><bold>+1 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 1)),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Koszt: <aqua><bold>1 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#00FF26>Kliknij, aby zakupić")
                ).asGuiItem(event -> {
                    gui.close(p);
                    int shards = getAmountOfItem(p);
                    if (shards < 1) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz wystarczająco odłamków kosmosu!"));
                        return;
                    }
                    if (getUpgradeLevel(itemStack, upgradeType) + 1 > 2000) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cOsiągnieto maksymalny poziom!"));
                        return;
                    }
                    deleteOdlamki(p, 1);
                    addUpgradeLevel(itemStack, upgradeType, 1);
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aZakupiono 1 poziom ulepszenia!"));
                });

        GuiItem upgrade_16 = ItemBuilder.from(Material.LIME_DYE)
                .name(OtherHelper.mm("<color:#2EB842><bold>+16 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 16)),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Koszt: <aqua><bold>16 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#00FF26>Kliknij, aby zakupić")
                ).asGuiItem(event -> {
                    gui.close(p);
                    int shards = getAmountOfItem(p);
                    if (shards < 16) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz wystarczająco odłamków kosmosu!"));
                        return;
                    }
                    if (getUpgradeLevel(itemStack, upgradeType) + 16 > 2000) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cOsiągnieto maksymalny poziom!"));
                        return;
                    }
                    deleteOdlamki(p, 16);
                    addUpgradeLevel(itemStack, upgradeType, 16);
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aZakupiono 16 poziomów ulepszenia!"));
                });

        GuiItem upgrade_32 = ItemBuilder.from(Material.LIME_DYE)
                .name(OtherHelper.mm("<color:#2EB842><bold>+32 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 32)),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Koszt: <aqua><bold>32 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#00FF26>Kliknij, aby zakupić")
                ).asGuiItem(event -> {
                    gui.close(p);
                    int shards = getAmountOfItem(p);
                    if (shards < 32) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz wystarczająco odłamków kosmosu!"));
                        return;
                    }
                    if (getUpgradeLevel(itemStack, upgradeType) + 32 > 2000) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cOsiągnieto maksymalny poziom!"));
                        return;
                    }
                    deleteOdlamki(p, 32);
                    addUpgradeLevel(itemStack, upgradeType, 32);
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aZakupiono 32 poziomów ulepszenia!"));
                });

        GuiItem upgrade_64 = ItemBuilder.from(Material.LIME_DYE)
                .name(OtherHelper.mm("<color:#2EB842><bold>+64 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 64)),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Koszt: <aqua><bold>64 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#00FF26>Kliknij, aby zakupić")
                ).asGuiItem(event -> {
                    gui.close(p);
                    int shards = getAmountOfItem(p);
                    if (shards < 64) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz wystarczająco odłamków kosmosu!"));
                        return;
                    }
                    if (getUpgradeLevel(itemStack, upgradeType) + 64 > 2000) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cOsiągnieto maksymalny poziom!"));
                        return;
                    }
                    deleteOdlamki(p, 64);
                    addUpgradeLevel(itemStack, upgradeType, 64);
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aZakupiono 64 poziomów ulepszenia!"));
                });

        GuiItem upgrade_128 = ItemBuilder.from(Material.LIME_DYE)
                .name(OtherHelper.mm("<color:#2EB842><bold>+128 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 128)),
                        OtherHelper.mm("<color:#2EB842><bold>|</bold> <gray>Koszt: <aqua><bold>128 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#00FF26>Kliknij, aby zakupić")
                ).asGuiItem(event -> {
                    gui.close(p);
                    int shards = getAmountOfItem(p);
                    if (shards < 128) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie masz wystarczająco odłamków kosmosu!"));
                        return;
                    }
                    if (getUpgradeLevel(itemStack, upgradeType) + 128 > 2000) {
                        p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cOsiągnieto maksymalny poziom!"));
                        return;
                    }
                    deleteOdlamki(p, 128);
                    addUpgradeLevel(itemStack, upgradeType, 128);
                    p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aZakupiono 128 poziomów ulepszenia!"));
                });


        GuiItem noupgrade_1 = ItemBuilder.from(Material.RED_DYE)
                .name(OtherHelper.mm("<color:#C44040><bold>+1 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 1)),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Koszt: <aqua><bold>1 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Brakuje ci <aqua><bold>1 Odłamków kosmosu"),
                        OtherHelper.mm("<gray>do zakupu tego ulepszenia!"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040>Nie stać cię :c")
                ).asGuiItem(event -> {


                });

        GuiItem noupgrade_16 = ItemBuilder.from(Material.RED_DYE)
                .name(OtherHelper.mm("<color:#C44040><bold>+16 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 16)),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Koszt: <aqua><bold>16 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Brakuje ci <aqua><bold>16 Odłamków kosmosu"),
                        OtherHelper.mm("<gray>do zakupu tego ulepszenia!"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040>Nie stać cię :c")
                ).asGuiItem(event -> {

                });

        GuiItem noupgrade_32 = ItemBuilder.from(Material.RED_DYE)
                .name(OtherHelper.mm("<color:#C44040><bold>+32 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 32)),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Koszt: <aqua><bold>32 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Brakuje ci <aqua><bold>32 Odłamków kosmosu"),
                        OtherHelper.mm("<gray>do zakupu tego ulepszenia!"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040>Nie stać cię :c")
                ).asGuiItem(event -> {

                });

        GuiItem noupgrade_64 = ItemBuilder.from(Material.RED_DYE)
                .name(OtherHelper.mm("<color:#C44040><bold>+64 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 64)),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Koszt: <aqua><bold>64 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Brakuje ci <aqua><bold>64 Odłamków kosmosu"),
                        OtherHelper.mm("<gray>do zakupu tego ulepszenia!"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040>Nie stać cię :c")
                ).asGuiItem(event -> {

                });

        GuiItem noupgrade_128 = ItemBuilder.from(Material.RED_DYE)
                .name(OtherHelper.mm("<color:#C44040><bold>+128 Poziom"))
                .lore(
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Poziom po ulepszeniu: <white>" + (getUpgradeLevel(itemStack, upgradeType) + 128)),
                        OtherHelper.mm("<color:#C44040><bold>|</bold> <gray>Koszt: <aqua><bold>128 Odłamków kosmosu"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<gray>Brakuje ci <aqua><bold>128 Odłamków kosmosu"),
                        OtherHelper.mm("<gray>do zakupu tego ulepszenia!"),
                        OtherHelper.mm(""),
                        OtherHelper.mm("<color:#C44040>Nie stać cię :c")
                ).asGuiItem(event -> {

                });


        GuiItem orange = ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor("&f")))
                .asGuiItem();
        GuiItem yellow = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor("&f")))
                .asGuiItem();
        GuiItem white = ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor("&f")))
                .asGuiItem();
        GuiItem gray = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor("&f")))
                .asGuiItem();

        gui.setItem(0, orange);
        gui.setItem(1, yellow);
        gui.setItem(2, white);
        gui.setItem(3, gray);

        gui.setItem(4, new GuiItem(itemStack));

        gui.setItem(5, gray);
        gui.setItem(6, white);
        gui.setItem(7, yellow);
        gui.setItem(8, orange);

        gui.setItem(9, yellow);
        gui.setItem(10, gray);


        Inventory inv = p.getInventory();
        GuiItem odlamek = ItemBuilder.from(Material.AMETHYST_SHARD)
                .name(Component.text(GlobalHelper.fixColor("&b&lOdłamek kosmosu")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to odłamek który możesz wpłacic &f/zbyszek")),
                        Component.text(GlobalHelper.fixColor(" &7oraz możesz wymienić go na &5&lMaterie kosmosu")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .glow()
                .asGuiItem();

        int Count = Arrays.stream(inv.getContents())
                .filter(item -> item != null && item.getItemMeta().getDisplayName().equals(odlamek.getItemStack().getItemMeta().getDisplayName()))
                .mapToInt(ItemStack::getAmount)
                .sum();

        gui.setItem(11, (Count >= 1) ? upgrade_1 : noupgrade_1);

        gui.setItem(12, (Count >= 16) ? upgrade_16 : noupgrade_16);

        gui.setItem(13, (Count >= 32) ? upgrade_32 : noupgrade_32);

        gui.setItem(14, (Count >= 64) ? upgrade_64 : noupgrade_64);

        gui.setItem(15, (Count >= 128) ? upgrade_128 : noupgrade_128);

        gui.setItem(16, gray);
        gui.setItem(17, yellow);


        gui.setItem(18, orange);
        gui.setItem(19, yellow);
        gui.setItem(20, white);
        gui.setItem(21, gray);

        gui.setItem(22, upgrade_max);

        gui.setItem(23, gray);
        gui.setItem(24, white);
        gui.setItem(25, yellow);
        gui.setItem(26, orange);

        gui.open(p);

    }

    private static void deleteOdlamki(Player p, int amountx) {
        Inventory inv = p.getInventory();
        GuiItem odlamek = ItemBuilder.from(Material.AMETHYST_SHARD)
                .name(Component.text(GlobalHelper.fixColor("&b&lOdłamek kosmosu")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to odłamek który możesz wpłacic &f/zbyszek")),
                        Component.text(GlobalHelper.fixColor(" &7oraz możesz wymienić go na &5&lMaterie kosmosu")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .glow()
                .asGuiItem();
        int toRemove = amountx;
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && item.getItemMeta().getDisplayName().equals(odlamek.getItemStack().getItemMeta().getDisplayName())) {
                int amount = item.getAmount();
                if (amount <= toRemove) {
                    inv.clear(i);
                    toRemove -= amount;
                } else {
                    item.setAmount(amount - toRemove);
                    break;
                }
            }
        }

    }

    private static int getAmountOfItem(Player p) {
        Inventory inv = p.getInventory();
        GuiItem odlamek = ItemBuilder.from(Material.AMETHYST_SHARD)
                .name(Component.text(GlobalHelper.fixColor("&b&lOdłamek kosmosu")))
                .lore(
                        Component.text(GlobalHelper.fixColor("")),
                        Component.text(GlobalHelper.fixColor(" &7Jest to odłamek który możesz wpłacic &f/zbyszek")),
                        Component.text(GlobalHelper.fixColor(" &7oraz możesz wymienić go na &5&lMaterie kosmosu")),
                        Component.text(GlobalHelper.fixColor(""))
                )
                .glow()
                .asGuiItem();
        int allAmount = 0;
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null && item.getItemMeta().getDisplayName().equals(odlamek.getItemStack().getItemMeta().getDisplayName())) {
                allAmount += item.getAmount();
            }
        }

        return allAmount;
    }
}
