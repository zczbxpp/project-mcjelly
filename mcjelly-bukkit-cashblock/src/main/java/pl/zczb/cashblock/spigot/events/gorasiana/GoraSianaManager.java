package pl.zczb.cashblock.spigot.events.gorasiana;


import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.helpers.OtherHelper;
import pl.zczb.cashblock.objects.impl.GoraSianaBlockManager;
import pl.zczb.cashblock.objects.impl.RegionManager;
import pl.zczb.helpers.GlobalHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GoraSianaManager {

    private final Cashblock plugin;

    private final List<GoraSianaLocation> locations = new ArrayList<>();

    private static GoraSianaLocation currentLocation = null;

    public static int timeRemainingSeconds = 0;
    private BukkitRunnable eventTimerTask;
    public static BossBar bossBar = Bukkit.createBossBar(GlobalHelper.fixColor("&#00FF79⛏ | &fAktualnie trwa event &#00FF79&lGóra Siana &8(&#A3FFCFx: 0, y: 0, z: 0&8) &8(&#00FF790s&8)"), BarColor.GREEN, BarStyle.SOLID);

    public GoraSianaManager(Cashblock plugin) {
        this.plugin = plugin;
        initLocations();
    }

    private void initLocations() {
        locations.add(new GoraSianaLocation("gora_event_break1", new Location(Bukkit.getWorld("world"), 2574, 105, 2072), "gora.schem", new Location(Bukkit.getWorld("world"), 2499, 105, 1995)));
        locations.add(new GoraSianaLocation("gora_event_break2", new Location(Bukkit.getWorld("world"), -1999, 95, -1847), "gora.schem", new Location(Bukkit.getWorld("world"), -2075, 95, -1924)));
        locations.add(new GoraSianaLocation("gora_event_break3", new Location(Bukkit.getWorld("world"), -406, 87, 558), "gora.schem", new Location(Bukkit.getWorld("world"), -482, 87, 481)));
    }

    public List<GoraSianaLocation> getLocations() {
        return locations;
    }

    public static GoraSianaLocation getCurrentLocation() {
        return currentLocation;
    }

    public int getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public void startEvent(int locationIndex) {
        if (locationIndex < 0 || locationIndex >= locations.size()) {
            Bukkit.getLogger().warning("Nieprawidłowy index lokalizacji eventu!");
            return;
        }

        if (eventTimerTask != null) {
            eventTimerTask.cancel();
        }

        currentLocation = locations.get(locationIndex);
        timeRemainingSeconds = 3600;


        for (Player po : Bukkit.getOnlinePlayers()) {
            bossBar.setTitle(GlobalHelper.fixColor("&#00FF79⛏ | &fAktualnie trwa event &#00FF79&lGóra Siana &8(&#A3FFCFx: " + currentLocation.getPasteLocation().getX() + ", y: " + currentLocation.getPasteLocation().getY() + ", z: " + currentLocation.getPasteLocation().getZ() + "&8) &8(&#00FF79&l1h&8)"));
            bossBar.addPlayer(po);
        }

        RegionManager.allowBlockBreak(currentLocation.getRegionId(), "world");

        generateSpecialBlocksFromWorldCentered(currentLocation.getCenterLocation(), 50, 120);

        eventTimerTask = new BukkitRunnable() {
            @Override
            public void run() {
                timeRemainingSeconds--;
                if (timeRemainingSeconds <= 0) {
                    resetEvent();
                    cancel();
                }
            }
        };
        eventTimerTask.runTaskTimer(plugin, 20L, 20L);
    }

    public void resetEvent() {
        if (currentLocation == null) return;

        Clipboard clipboard = loadSchematic(currentLocation.getSchematicFileName());
        if (clipboard == null) {
            Bukkit.getLogger().warning("Nie udało się załadować schematów do resetu eventu!");
            return;
        }

        RegionManager.denyBlockBreak(currentLocation.getRegionId(), "world");

        for (Player po : Bukkit.getOnlinePlayers()) {
            OtherHelper.teleportMaxY(po);
            po.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&CEvent sie skonczył!"));
        }

        pasteSchematic(clipboard, currentLocation.getPasteLocation());

        GoraSianaBlockManager.clearAll();

        currentLocation = null;
        timeRemainingSeconds = 0;
    }

    private Clipboard loadSchematic(String filename) {
        try {
            File file = new File(plugin.getDataFolder(), filename);
            if (!file.exists()) {
                Bukkit.getLogger().warning("Schemat " + filename + " nie istnieje w folderze pluginu!");
                return null;
            }
            var format = ClipboardFormats.findByFile(file);
            try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                return reader.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void pasteSchematic(Clipboard clipboard, Location location) {
        if (clipboard == null || location == null || location.getWorld() == null) return;

        var worldEditWorld = BukkitAdapter.adapt(location.getWorld());
        BlockVector3 to = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(worldEditWorld)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(to)
                    .ignoreAirBlocks(false)
                    .build();

            Operations.complete(operation);
            editSession.flushSession();

        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Błąd podczas wklejania schematu!");
        }
    }


    public void generateSpecialBlocksFromWorldCentered(Location center, int radiusHorizontal, int heightUp) {
        List<Location> stonePositions = new ArrayList<>();
        for (int x = -radiusHorizontal; x <= radiusHorizontal; x++) {
            for (int y = 0; y <= heightUp; y++) {
                for (int z = -radiusHorizontal; z <= radiusHorizontal; z++) {
                    Location checkLoc = center.clone().add(x, y, z);
                    if (checkLoc.getBlock().getType().name().equalsIgnoreCase("STONE")) {
                        stonePositions.add(checkLoc);
                    }
                }
            }
        }

        if (stonePositions.isEmpty()) return;

        Collections.shuffle(stonePositions);
        Random random = new Random();
        double[] allowedValues = {0.25, 0.5, 1.0};
        int count = Math.min(20, stonePositions.size());

        for (int i = 0; i < count; i++) {
            Location loc = stonePositions.get(i);
            double value = allowedValues[random.nextInt(allowedValues.length)];
            GoraSianaBlockManager.addBlock(loc, value);
        }
    }


}
