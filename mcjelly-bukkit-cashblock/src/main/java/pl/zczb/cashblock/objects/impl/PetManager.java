package pl.zczb.cashblock.objects.impl;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.objects.Pet;
import pl.zczb.helpers.GlobalHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PetManager {
    private final Map<String, Pet> pets = new HashMap<>();

    private final HashMap<UUID, ArmorStand> activepets = new HashMap<>();

    private final Map<UUID, BukkitRunnable> petTasks = new HashMap<>();

    private static final String PLAYER_SKIN_BASE64_1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGI2MTUyMTI1OWM2YzM0MDJmOWNhZTNiNjg2N2RkYjQ4MWZkN2E4MzQ2NzRmMjZiZjRmY2YxMWM5NzhlMDA1MSJ9fX0=";
    private static final String PLAYER_SKIN_BASE64_2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAyZTcxMjk1YjE3YTQxNjBiNWJmOThhMDMwMWY3YmNkMGJlYTIwNzNmYThkMmM4OTQxODE0YTc5MmU2OTQyYyJ9fX0=";
    private static final String PLAYER_SKIN_BASE64_3 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg4OTY2MDVlNDFhMWY0ZTJjM2M5MmE5NjRmMzkxZjRlNjEzOTBjYjEwYWYyYzBmYWI2MTVhNWQzNGU2MTA3NCJ9fX0=";
    private static final String PLAYER_SKIN_BASE64_4 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ1YjYyY2Y1NGY0YjQwY2RiYzRhNTI5MTM2OTM2MjIzNzc2YzlkM2ViYWQ4NTljYzFjZDU4MjIyYmZiNGMxZiJ9fX0=";

    private static final String PLAYER_SKIN_BASE64_5 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzYxNmIwODlkOGZhOGZiZjE3ZGUxNDA1ZjgxNDlmNmM5NjhkYmYzMmMyMzg4ZDM1M2YwZGJlMTY5YjVjNDU2OSJ9fX0=";

    public PetManager() {
        registerPet(new Pet("apsik", "Apsik", createGuiItem("Apsik", PLAYER_SKIN_BASE64_1, List.of("&7ᴘᴏѕᴘᴏʟɪᴛʏ", " ", "&7Statystyki:", " &2ᴠᴘʟɴ: +x0.10", " ")), 0, 0.10));
        registerPet(new Pet("gburek", "Gburek", createGuiItem("Gburek", PLAYER_SKIN_BASE64_2, List.of("&7ᴘᴏѕᴘᴏʟɪᴛʏ", " ", "&7Statystyki:", " &2ᴠᴘʟɴ: +x0.15", " &3ᴇxᴘ: +x0.25", " ")), 0, 0.15));
        registerPet(new Pet("wesolek", "Wesolek", createGuiItem("Wesolek", PLAYER_SKIN_BASE64_3, List.of("&7ᴘᴏѕᴘᴏʟɪᴛʏ", " ", "&7Statystyki:", " &2ᴠᴘʟɴ: +x0.15", " &bѕᴘᴇᴇᴅ: I", " ")), 1, 0.15));
        registerPet(new Pet("gapcio", "Gapcio", createGuiItem("Gapcio", PLAYER_SKIN_BASE64_4, List.of("&5ᴇᴘɪᴄᴋɪ", " ", "&7Statystyki:", " &2ᴠᴘʟɴ: +x0.20", " &3ᴇxᴘ: +x0.25", " &bѕᴘᴇᴇᴅ: II", " ")), 2, 0.20));
        registerPet(new Pet("aronek", "Aronek", createGuiItem("Aronek", PLAYER_SKIN_BASE64_5, List.of("&6ʟᴇɢᴇɴᴅᴀʀɴʏ", " ", "&7Statystyki:", " &2ᴠᴘʟɴ: +x0.25", " &3ᴇxᴘ: +x0.5", " &bѕᴘᴇᴇᴅ: II", " ")), 2, 0.25));
    }

    public void registerPet(Pet pet) {
        pets.put(pet.getName(), pet);
    }

    public Pet getPet(String name) {
        return pets.get(name);
    }

    private static GuiItem createGuiItem(String name, String skinBase64, List<String> lores) {

        return ItemBuilder.skull().texture(skinBase64)
                .name(Component.text(GlobalHelper.fixColor("&7" + name)))
                .lore(lores.stream().map(GlobalHelper::fixColor).map(Component::text).toArray(Component[]::new))
                .asGuiItem();
    }

    public void removePet(Player player) {
        UUID playerId = player.getUniqueId();

        if (!activepets.containsKey(playerId)) {
            return;
        }

        if (petTasks.containsKey(playerId)) {
            petTasks.get(playerId).cancel();
            petTasks.remove(playerId);
        }

        ArmorStand pet = activepets.get(playerId);
        pet.remove();
        activepets.remove(playerId);
    }


    public void spawnPet(Player player) {
        UserDataModel userData = Cashblock.getInstance().getUserHandler().getPlayer(player);
        String activePetName = userData.getActivePet();

        if (activePetName == null || !pets.containsKey(activePetName)) return;
        if (activepets.containsKey(player.getUniqueId())) return;

        Location petLocation = getPetLocation(player);

        ArmorStand pet = player.getWorld().spawn(petLocation, ArmorStand.class);
        pet.setInvisible(true);
        pet.setGravity(false);
        pet.setInvulnerable(true);
        pet.setMarker(true);
        pet.setCustomNameVisible(false);

        pet.getEquipment().setHelmet(pets.get(activePetName).getItem().getItemStack());

        activepets.put(player.getUniqueId(), pet);

        startPetTask(player, pet);
    }

    private void startPetTask(Player player, ArmorStand pet) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || pet.isDead()) {
                    petTasks.remove(player.getUniqueId());
                    activepets.remove(player.getUniqueId());
                    pet.remove();
                    cancel();
                    return;
                }
                pet.teleport(getPetLocation(player));
            }
        };

        task.runTaskTimer(Cashblock.getInstance(), 0, 2);
        petTasks.put(player.getUniqueId(), task);
    }

    public void removeAllPets() {

        for (BukkitRunnable task : petTasks.values()) {
            task.cancel();
        }
        petTasks.clear();

        for (ArmorStand pet : activepets.values()) {
            pet.remove();
        }
        activepets.clear();
    }

    private Location getPetLocation(Player player) {
        Location playerLoc = player.getLocation();
        org.bukkit.util.Vector direction = playerLoc.getDirection();


        direction.setY(0).normalize();
        org.bukkit.util.Vector offset = direction.clone().multiply(-1.0);
        org.bukkit.util.Vector right = new org.bukkit.util.Vector(-direction.getZ(), 0, direction.getX()).normalize().multiply(0.4);

        return playerLoc.add(offset).add(right).add(0, 0.3, 0);
    }


    public void applyPetEffects(Player p) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        if (u == null) return;

        Pet pet = getPet(u.getActivePet());
        if (pet == null) return;


        int speedLevel = pet.getSpeed();

        if (speedLevel > 0) {
            p.addPotionEffect(new PotionEffect(
                    PotionEffectType.SPEED,
                    Integer.MAX_VALUE,
                    speedLevel - 1,
                    false,
                    false
            ));
        }
    }

    public void removePetEffects(Player p) {
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);

        if (u == null) return;

        Pet pet = getPet(u.getActivePet());
        if (pet == null) return;


        if (pet.getSpeed() > 0) {
            p.removePotionEffect(PotionEffectType.SPEED);
        }

    }

}
