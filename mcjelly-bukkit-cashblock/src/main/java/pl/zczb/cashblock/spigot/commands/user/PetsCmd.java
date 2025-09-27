package pl.zczb.cashblock.spigot.commands.user;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.objects.Pet;
import pl.zczb.cashblock.objects.impl.PetManager;
import pl.zczb.cashblock.spigot.events.BoostsHandler;
import pl.zczb.helpers.GlobalHelper;

import java.util.ArrayList;
import java.util.List;

@Command(name = "pety")
public class PetsCmd {
    public PetsCmd(PetManager petHandler) {
        this.petHandler = petHandler;
    }

    private final PetManager petHandler;

    @Execute
    public void onCommand(@Context CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą używać tej komendy!");
            return;
        }

        Player player = (Player) sender;
        Gui gui = Gui.gui()
                .title(Component.text(GlobalHelper.fixColor("&8Pety")))
                .rows(5)
                .disableAllInteractions()
                .create();

        UserDataModel userData = Cashblock.getInstance().getUserHandler().getPlayer(player);

        int index = 10;

        if (userData.getUserPets().isApsik()) {
            addPetToGui(gui, "apsik", index);
            index++;
        }
        if (userData.getUserPets().isGburek() && index <= 16) {
            addPetToGui(gui, "gburek", index);
            index++;
        }
        if (userData.getUserPets().isWesolek() && index <= 16) {
            addPetToGui(gui, "wesolek", index);
            index++;
        }
        if (userData.getUserPets().isGapcio() && index <= 16) {
            addPetToGui(gui, "gapcio", index);
            index++;
        }
        if (userData.getUserPets().isAronek() && index <= 16) {
            addPetToGui(gui, "aronek", index);
            index++;
        }

        if (index > 16 && index <= 25) {
            index = 19;
        } else if (index > 25 && index <= 34) {
            index = 28;
        }

        Pet pet = petHandler.getPet(userData.getActivePet());


        if (pet != null) {


            ItemStack is = pet.getItem().getItemStack().clone();
            ItemMeta meta = is.getItemMeta();

            if (meta != null) {
                List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

                if (!lore.contains(GlobalHelper.fixColor("&aKliknij aby dezaktywować!"))) {
                    lore.add(GlobalHelper.fixColor("&aKliknij aby dezaktywować!"));
                }

                meta.setLore(lore);
                is.setItemMeta(meta);
            }

            GuiItem petitem = ItemBuilder.from(is).asGuiItem(evente -> {
                gui.close(player);
                petHandler.removePetEffects(player);
                userData.setActivePet("");
                petHandler.removePet(player);
                BoostsHandler.checkBonuses(player);
                player.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie dezaktywowano peta!"));
                player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie dezaktywowano aktywnego peta!"));

            });

            gui.setItem(4, petitem);


        } else {

            GuiItem petitem = ItemBuilder.from(Material.BARRIER)
                    .name(Component.text(GlobalHelper.fixColor("&cBrak aktywnego peta")))
                    .asGuiItem();
            gui.setItem(4, petitem);
        }


        setDecorativeItems(gui);

        gui.open(player);
    }

    private void addPetToGui(Gui gui, String petName, int index) {
        Pet pet = petHandler.getPet(petName);


        ItemStack is = pet.getItem().getItemStack().clone();
        ItemMeta meta = is.getItemMeta();

        if (meta != null) {
            List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

            if (!lore.contains(GlobalHelper.fixColor("&aKliknij aby aktywować!"))) {
                lore.add(GlobalHelper.fixColor("&aKliknij aby aktywować!"));
            }

            meta.setLore(lore);
            is.setItemMeta(meta);
        }

        GuiItem guiItem = new GuiItem(is, event -> {
            Player player = (Player) event.getWhoClicked();
            UserDataModel userData = Cashblock.getInstance().getUserHandler().getPlayer(player);
            if (userData.getActivePet() != null) {
                petHandler.removePetEffects(player);
            }
            petHandler.removePet(player);
            userData.setActivePet(petName.toLowerCase());
            petHandler.spawnPet(player);
            petHandler.applyPetEffects(player);
            gui.close(player);
            BoostsHandler.checkBonuses(player);
            player.sendTitle(GlobalHelper.fixColor("&f"), GlobalHelper.fixColor("&aPomyślnie ustawiono peta!"));
            player.sendMessage(GlobalHelper.fixColor("&8[&#20EA00&l!&8] &#6FFF58Pomyślnie ustawiono nowego aktywnego peta!"));

        });

        gui.setItem(index, guiItem);
    }

    private void setDecorativeItems(Gui gui) {
        GuiItem white = ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();
        GuiItem yellow = ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();
        GuiItem orange = ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                .name(Component.text(GlobalHelper.fixColor(" ")))
                .asGuiItem();

        int[] orangeSlots = {0, 8, 36, 44};
        int[] yellowSlots = {1, 7, 9, 17, 27, 35, 37, 43};
        int[] whiteSlots = {2, 3, 5, 6, 38, 39, 41, 42};

        for (int slot : orangeSlots) gui.setItem(slot, orange);
        for (int slot : yellowSlots) gui.setItem(slot, yellow);
        for (int slot : whiteSlots) gui.setItem(slot, white);
    }
}
