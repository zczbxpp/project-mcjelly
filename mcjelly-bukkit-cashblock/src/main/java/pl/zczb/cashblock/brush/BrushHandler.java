package pl.zczb.cashblock.brush;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.objects.CustomBlock;
import pl.zczb.cashblock.objects.impl.CustomBlockManager;
import pl.zczb.cashblock.objects.impl.TurboManager;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.EarnVplnPacket;
import pl.zczb.redis.packet.Packet;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BrushHandler implements Listener {
    private static final BlockFace[] AXIS = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private final BrushPluginConfiguration configuration;
    private final CustomBlockManager cashBlockManager;

    public BrushHandler(BrushPluginConfiguration configuration,CustomBlockManager cashBlockManager) {
        this.configuration = configuration;
        this.cashBlockManager = cashBlockManager;
    }

    public static BlockFace getDirection(Player player) {
        float pitch = player.getLocation().getPitch();
        if (pitch > 45.0F) return BlockFace.DOWN;
        if (pitch < -45.0F) return BlockFace.UP;
        return AXIS[Math.round(player.getLocation().getYaw() / 90.0F) & 0x3].getOppositeFace();
    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent event) {
        BrushItem item = this.configuration.findByItemStack2(event.getItemDrop().getItemStack());
        if (item != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void handle(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR)
            return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;
        if (OtherHelper.isInRegion(player, "spawn") || OtherHelper.isInRegion(player, "gora_event_safe1") || OtherHelper.isInRegion(player, "gora_event_safe2") || OtherHelper.isInRegion(player, "gora_event_safe3") || event.getClickedBlock().getType() == Material.BEDROCK) {
            return;
        }
        BrushItem brushItem = this.configuration.findByItemStack2(item);
        if (brushItem == null)
            return;

        if (!this.configuration.hasSameNameInLore(item, player.getName())) {
            player.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cNie jesteś włascicielem brusha!"));
            return;
        }

        UserDataModel user = Cashblock.getInstance().getUserHandler().getPlayer(player);
        if (isTestBrushExpired(user, player, item)) {
            return;
        }

        List<Block> blocks = brushItem.getArea().getAreaBlocks(event.getClickedBlock().getLocation(), getDirection(player));

        for (Block block : blocks) {
            if (this.configuration.isAllowed(block.getType()))
                breakBlock(block, player);
        }
    }

    public void breakBlock(Block block, Player p) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        Collection<ItemStack> drops = block.getDrops();
        block.setType(Material.AIR);

        for (ItemStack drop : drops) {
            if (!p.getInventory().addItem(drop).isEmpty()) {
                break;
            }
        }

        Location blockLocation = block.getLocation();

        CustomBlock cashBlock = cashBlockManager.getCashBlock(blockLocation);

        if (cashBlock != null) {
            cashBlockManager.removeCashBlock(blockLocation);

            double amount = cashBlock.getAmount();

            p.sendTitle(GlobalHelper.fixColor("\uE822 Wykopałeś &8(&a" + decimalFormat.format(amount) + " vPLN&8) &f\uE822"),
                    GlobalHelper.fixColor("&fStan konta &8(&e/portfel&8) &fzarabiaj &6&lWIĘCEJ &f/rangi &8(&3+5 exp.&8)"));

            u.addPln(amount);
            p.playSound(p.getLocation(), "custom.launch1", 0.25f, 1.0f);
            Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new EarnVplnPacket(p.getName(), decimalFormat.format(amount)));
        }

        double reward = getReward(u, p);
        if (OtherHelper.getChance(0.007D)) {


            ItemStack shard = ItemBuilder.from(Material.AMETHYST_SHARD)
                    .name(Component.text(GlobalHelper.fixColor("&b&lOdłamek kosmosu")))
                    .lore(
                            Component.text(GlobalHelper.fixColor("")),
                            Component.text(GlobalHelper.fixColor(" &7Jest to odłamek który możesz wpłacić &f/zbyszek")),
                            Component.text(GlobalHelper.fixColor(" &7oraz możesz ulepszyć nim kilof &aPPM &7trzymajac go!")),
                            Component.text(GlobalHelper.fixColor(""))
                    )
                    .glow()
                    .amount(1)
                    .build();

            Map<Integer, ItemStack> leftover = p.getInventory().addItem(new ItemStack[]{shard});

            if (!leftover.isEmpty()) {
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&cMasz pełne eq odłamek wypadł na ziemie"));
                for (ItemStack item : leftover.values()) {
                    p.getWorld().dropItemNaturally(p.getLocation(), item);
                }
            } else {
                p.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&fGratulacje &7trafiłeś na &d&lOdłamek kosmosu"));
            }
        }


        u.addStone(1);

        if (reward == 0.0D) {
            addXp(p, 0.07D);

            return;
        }
        reward *= u.getMnoznik();
        if (u.isTurboDrop() || TurboManager.isOnTurboDrop()) reward *= 2.0D;

        double xpToAdd = reward * 50.0D;
        addXp(p, xpToAdd);


        p.sendTitle(GlobalHelper.fixColor(" Wykopałeś &8(&a" + decimalFormat.format(reward) + " vPLN&8) &f"),
                GlobalHelper.fixColor("&fStan konta &8(&e/portfel&8) &fzarabiaj &6&lWIĘCEJ &f/rangi &8(&3+" + decimalFormat.format(xpToAdd) + " exp.&8)"));

        u.addPln(reward);
        p.playSound(p.getLocation(), "custom.launch1", 0.25F, 1.0F);
        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new EarnVplnPacket(p.getName(), decimalFormat.format(reward)));
    }


    private double getReward(UserDataModel u, Player p) {
        double baseChance, currentBalance = u.getPln();

        String itemName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
        boolean isBrush9x9mlot = itemName.equals(GlobalHelper.fixColor("&#FCFF00✦ &#FFD900&lPiekielny młot &#FFA500&l20x20 &#FCFF00✦"));
        boolean isBrush9x9 = itemName.equals(GlobalHelper.fixColor("&#FCFF00✦ &#FFD900&lBrush &#FFA500&l9x9 &#BFBFBF(Poziom V) &#FCFF00✦"));
        boolean isBrush7x7 = itemName.equals(GlobalHelper.fixColor("&#FCFF00✦ &#FFD900&lBrush &#FFA500&l7x7 &#BFBFBF(Poziom IV) &#FCFF00✦"));
        boolean isBrush5x5 = itemName.equals(GlobalHelper.fixColor("&#FCFF00✦ &#FFD900&lBrush &#FFA500&l5x5 &#BFBFBF(Poziom III) &#FCFF00✦"));

        if(isBrush9x9mlot) {
            if (currentBalance < 1.0D) {
                baseChance = 0.01D;
            } else if (currentBalance < 2.0D) {
                baseChance = 0.01D;
            } else if (currentBalance < 5.0D) {
                baseChance = 0.005D;
            } else if (currentBalance < 8.0D) {
                baseChance = 0.005D;
            } else if (currentBalance < 9.0D) {
                baseChance = 0.005D;
            } else if (currentBalance < 13.0D) {
                baseChance = 0.004D;
            } else if (currentBalance < 17.0D) {
                baseChance = 0.003D;
            } else if (currentBalance < 19.0D) {
                baseChance = 0.002D;
            } else if (currentBalance < 19.5D) {
                baseChance = 0.002D;
            } else if (currentBalance < 19.8D) {
                baseChance = 0.002D;
            } else {
                baseChance = 0.001D;
            }
        }else if (isBrush9x9) {
            if (currentBalance < 1.0D) {
                baseChance = 0.02D;
            } else if (currentBalance < 2.0D) {
                baseChance = 0.02D;
            } else if (currentBalance < 5.0D) {
                baseChance = 0.02D;
            } else if (currentBalance < 8.0D) {
                baseChance = 0.01D;
            } else if (currentBalance < 9.0D) {
                baseChance = 0.01D;
            } else if (currentBalance < 13.0D) {
                baseChance = 0.01D;
            } else if (currentBalance < 17.0D) {
                baseChance = 0.01D;
            } else if (currentBalance < 19.0D) {
                baseChance = 0.005D;
            } else if (currentBalance < 19.5D) {
                baseChance = 0.008D;
            } else if (currentBalance < 19.8D) {
                baseChance = 0.003D;
            } else {
                baseChance = 0.003D;
            }
        } else if (isBrush7x7) {
            if (currentBalance < 1.0D) {
                baseChance = 0.03D;
            } else if (currentBalance < 2.0D) {
                baseChance = 0.02D;
            } else if (currentBalance < 5.0D) {
                baseChance = 0.02D;
            } else if (currentBalance < 8.0D) {
                baseChance = 0.015D;
            } else if (currentBalance < 9.0D) {
                baseChance = 0.01D;
            } else if (currentBalance < 13.0D) {
                baseChance = 0.006D;
            } else if (currentBalance < 17.0D) {
                baseChance = 0.005D;
            } else if (currentBalance < 19.0D) {
                baseChance = 0.005D;
            } else if (currentBalance < 19.5D) {
                baseChance = 0.005D;
            } else if (currentBalance < 19.8D) {
                baseChance = 0.003D;
            } else {
                baseChance = 0.003D;
            }
        } else if (isBrush5x5) {
            if (currentBalance < 1.0D) {
                baseChance = 0.1D;
            } else if (currentBalance < 2.0D) {
                baseChance = 0.09D;
            } else if (currentBalance < 5.0D) {
                baseChance = 0.08D;
            } else if (currentBalance < 8.0D) {
                baseChance = 0.07D;
            } else if (currentBalance < 9.0D) {
                baseChance = 0.06D;
            } else if (currentBalance < 13.0D) {
                baseChance = 0.04D;
            } else if (currentBalance < 17.0D) {
                baseChance = 0.015D;
            } else if (currentBalance < 19.0D) {
                baseChance = 0.01D;
            } else if (currentBalance < 19.5D) {
                baseChance = 0.01D;
            } else if (currentBalance < 19.8D) {
                baseChance = 0.005D;
            } else {
                baseChance = 0.005D;
            }

        } else if (currentBalance < 1.0D) {
            baseChance = 0.25D;
        } else if (currentBalance < 2.0D) {
            baseChance = 0.20D;
        } else if (currentBalance < 5.0D) {
            baseChance = 0.15D;
        } else if (currentBalance < 8.0D) {
            baseChance = 0.14D;
        } else if (currentBalance < 9.0D) {
            baseChance = 0.13D;
        } else if (currentBalance < 13.0D) {
            baseChance = 0.1D;
        } else if (currentBalance < 17.0D) {
            baseChance = 0.06D;
        } else if (currentBalance < 19.0D) {
            baseChance = 0.03D;
        } else if (currentBalance < 19.5D) {
            baseChance = 0.02D;
        } else if (currentBalance < 19.8D) {
            baseChance = 0.015D;
        } else {
            baseChance = 0.02D;
        }


        double[] rewards = {0.01D, 0.05D, 0.1D};
        double[] weights = {0.96D, 0.03D, 0.01D};

        for (int i = rewards.length - 1; i >= 0; i--) {
            double chance = baseChance * weights[i];
            if (OtherHelper.getChance(chance)) {
                return rewards[i];
            }
        }

        return 0.0D;
    }


    public double addXp(Player p, double baseXp) {
        double d1;
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        int lvl = u.getUserLvl().getLvl();
        double xpToNext = 500.0D + Math.pow(lvl, 2.0D) * 50.0D;
        u.getUserLvl().setXpToLvl((int) xpToNext);


        if (u.getUserPrestiz().getMoreExp() > 0) {
            d1 = baseXp * u.getUserPrestiz().getMoreExp() * 1.25D;
        } else {
            d1 = baseXp;
        }
        String itemName = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
        boolean isBrush9x9 = itemName.equals(GlobalHelper.fixColor("&#FCFF00✦ &#FFD900&lPiekielny młot &#FFA500&l20x20 &#FCFF00✦"));

        if (isBrush9x9) {
            d1 = baseXp * 0.5D;
        }


        u.getUserLvl().addXp(d1);

        if (u.getUserLvl().getXp() >= u.getUserLvl().getXpToLvl()) {
            u.getUserLvl().addLvl(1);
            u.getUserLvl().setXp(0.0D);
            p.sendTitle(GlobalHelper.fixColor("&6&lLVL UP"), GlobalHelper.fixColor("&FAwansowałeś na poziom &e" + u.getUserLvl().getLvl()));
        }

        if (u.getUserPrestiz().isAutoPrestiz()) {
            int prestiz = u.getUserPrestiz().getPrestiz();
            int wymaganyPoziom = (prestiz + 1) * 5;
            if (u.getUserLvl().getLvl() >= wymaganyPoziom) {
                u.getUserLvl().setLvl(1);
                u.getUserPrestiz().addPrestiz(1);
                u.getUserPrestiz().addPunktyPrestizu(1);
                p.sendTitle(GlobalHelper.fixColor("&#A68EEAAAutoPrestiż"), GlobalHelper.fixColor("&FAwansowałeś na poziom &#A68EEAA" + u.getUserPrestiz().getPrestiz()));
            }
        }
        OtherHelper.syncMinecraftLevelBar(p, u);
        return d1;
    }

    private boolean isTestBrushExpired(UserDataModel user, Player player, ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;

        if (!item.getItemMeta().getDisplayName().equalsIgnoreCase(
                GlobalHelper.fixColor("&#FCFF00✦ &#FFD900&lBrush &#FFA500&l3x3 &#BFBFBF(Testowy) &#FCFF00✦"))) {
            return false;
        }
        long testBrushTime = user.getTestBrushTime();
        if (testBrushTime == 0L) return false;

        boolean expired = ((System.currentTimeMillis() - testBrushTime) / 60000L >= 60L);

        if (expired) {
            player.getInventory().removeItem(new ItemStack[]{item});
            player.sendMessage(GlobalHelper.fixColor("&8[&6&l!&8] &7Twój testowy brush wygasł i został usunięty!"));
        }

        return expired;
    }
}


