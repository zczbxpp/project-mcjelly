package pl.zczb.tools.helpers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantHelper {
    private static HashMap<String, Enchantment> enchants;
    public static final HashMap<Enchantment, String> LANG = new HashMap<>();
    public static HashMap<Enchantment, Integer> maxEnchants;

    static {
        (maxEnchants = new HashMap<>()).put(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.valueOf(4));
        maxEnchants.put(Enchantment.DAMAGE_ALL, Integer.valueOf(5));
        maxEnchants.put(Enchantment.DIG_SPEED, Integer.valueOf(5));
        maxEnchants.put(Enchantment.DURABILITY, Integer.valueOf(3));
        maxEnchants.put(Enchantment.FIRE_ASPECT, Integer.valueOf(2));
        maxEnchants.put(Enchantment.KNOCKBACK, Integer.valueOf(2));
        maxEnchants.put(Enchantment.LOOT_BONUS_BLOCKS, Integer.valueOf(3));

        (enchants = new HashMap<>()).put("alldamage", Enchantment.DAMAGE_ALL);
        enchants.put("alldmg", Enchantment.DAMAGE_ALL);
        enchants.put("sharpness", Enchantment.DAMAGE_ALL);
        enchants.put("sharp", Enchantment.DAMAGE_ALL);
        enchants.put("dal", Enchantment.DAMAGE_ALL);
        enchants.put("ardmg", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("baneofarthropod", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("arthropod", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("dar", Enchantment.DAMAGE_ARTHROPODS);
        enchants.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);
        enchants.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchants.put("du", Enchantment.DAMAGE_UNDEAD);
        enchants.put("digspeed", Enchantment.DIG_SPEED);
        enchants.put("efficiency", Enchantment.DIG_SPEED);
        enchants.put("minespeed", Enchantment.DIG_SPEED);
        enchants.put("cutspeed", Enchantment.DIG_SPEED);
        enchants.put("ds", Enchantment.DIG_SPEED);
        enchants.put("eff", Enchantment.DIG_SPEED);
        enchants.put("durability", Enchantment.DURABILITY);
        enchants.put("dura", Enchantment.DURABILITY);
        enchants.put("unbreaking", Enchantment.DURABILITY);
        enchants.put("d", Enchantment.DURABILITY);
        enchants.put("thorns", Enchantment.THORNS);
        enchants.put("highcrit", Enchantment.THORNS);
        enchants.put("thorn", Enchantment.THORNS);
        enchants.put("highercrit", Enchantment.THORNS);
        enchants.put("t", Enchantment.THORNS);
        enchants.put("fireaspect", Enchantment.FIRE_ASPECT);
        enchants.put("fire", Enchantment.FIRE_ASPECT);
        enchants.put("meleefire", Enchantment.FIRE_ASPECT);
        enchants.put("meleeflame", Enchantment.FIRE_ASPECT);
        enchants.put("fa", Enchantment.FIRE_ASPECT);
        enchants.put("knockback", Enchantment.KNOCKBACK);
        enchants.put("kback", Enchantment.KNOCKBACK);
        enchants.put("kb", Enchantment.KNOCKBACK);
        enchants.put("k", Enchantment.KNOCKBACK);
        enchants.put("blockslootbonus", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("fort", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("lbb", Enchantment.LOOT_BONUS_BLOCKS);
        enchants.put("mobslootbonus", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("mobloot", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("lbm", Enchantment.LOOT_BONUS_MOBS);
        enchants.put("oxygen", Enchantment.OXYGEN);
        enchants.put("respiration", Enchantment.OXYGEN);
        enchants.put("breathing", Enchantment.OXYGEN);
        enchants.put("breath", Enchantment.OXYGEN);
        enchants.put("o", Enchantment.OXYGEN);
        enchants.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("prot", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("protect", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("p", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchants.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("expprot", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("blastprotect", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("pe", Enchantment.PROTECTION_EXPLOSIONS);
        enchants.put("fallprotection", Enchantment.PROTECTION_FALL);
        enchants.put("fallprot", Enchantment.PROTECTION_FALL);
        enchants.put("featherfall", Enchantment.PROTECTION_FALL);
        enchants.put("featherfalling", Enchantment.PROTECTION_FALL);
        enchants.put("pfa", Enchantment.PROTECTION_FALL);
        enchants.put("fireprotection", Enchantment.PROTECTION_FIRE);
        enchants.put("flameprotection", Enchantment.PROTECTION_FIRE);
        enchants.put("fireprotect", Enchantment.PROTECTION_FIRE);
        enchants.put("flameprotect", Enchantment.PROTECTION_FIRE);
        enchants.put("fireprot", Enchantment.PROTECTION_FIRE);
        enchants.put("flameprot", Enchantment.PROTECTION_FIRE);
        enchants.put("pf", Enchantment.PROTECTION_FIRE);
        enchants.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("projprot", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("pp", Enchantment.PROTECTION_PROJECTILE);
        enchants.put("silktouch", Enchantment.SILK_TOUCH);
        enchants.put("softtouch", Enchantment.SILK_TOUCH);
        enchants.put("st", Enchantment.SILK_TOUCH);
        enchants.put("waterworker", Enchantment.WATER_WORKER);
        enchants.put("aquaaffinity", Enchantment.WATER_WORKER);
        enchants.put("watermine", Enchantment.WATER_WORKER);
        enchants.put("ww", Enchantment.WATER_WORKER);
        enchants.put("firearrow", Enchantment.ARROW_FIRE);
        enchants.put("flame", Enchantment.ARROW_FIRE);
        enchants.put("flamearrow", Enchantment.ARROW_FIRE);
        enchants.put("af", Enchantment.ARROW_FIRE);
        enchants.put("arrowdamage", Enchantment.ARROW_DAMAGE);
        enchants.put("power", Enchantment.ARROW_DAMAGE);
        enchants.put("arrowpower", Enchantment.ARROW_DAMAGE);
        enchants.put("ad", Enchantment.ARROW_DAMAGE);
        enchants.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
        enchants.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
        enchants.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchants.put("arrowpunch", Enchantment.ARROW_KNOCKBACK);
        enchants.put("ak", Enchantment.ARROW_KNOCKBACK);
        enchants.put("infinitearrows", Enchantment.ARROW_INFINITE);
        enchants.put("infarrows", Enchantment.ARROW_INFINITE);
        enchants.put("infinity", Enchantment.ARROW_INFINITE);
        enchants.put("infinite", Enchantment.ARROW_INFINITE);
        enchants.put("unlimited", Enchantment.ARROW_INFINITE);
        enchants.put("unlimitedarrows", Enchantment.ARROW_INFINITE);
        enchants.put("ai", Enchantment.ARROW_INFINITE);
        LANG.put(Enchantment.ARROW_DAMAGE, "moc");
        LANG.put(Enchantment.ARROW_FIRE, "plomien");
        LANG.put(Enchantment.ARROW_INFINITE, "nieskonczonosc");
        LANG.put(Enchantment.ARROW_KNOCKBACK, "odrzut");
        LANG.put(Enchantment.DAMAGE_ALL, "ostrosc");
        LANG.put(Enchantment.DIG_SPEED, "szybkosc");
        LANG.put(Enchantment.DURABILITY, "niezniszczalnosc");
        LANG.put(Enchantment.FIRE_ASPECT, "zaklety ogien");
        LANG.put(Enchantment.KNOCKBACK, "odrzut");
        LANG.put(Enchantment.LOOT_BONUS_BLOCKS, "szczescie");
        LANG.put(Enchantment.PROTECTION_ENVIRONMENTAL, "ochrona");
        LANG.put(Enchantment.THORNS, "ciernie");
        LANG.put(Enchantment.SILK_TOUCH, "jedwabny dotyk");
    }


    public static Enchantment get(String name) {
        return enchants.get(name.toLowerCase());
    }

    public static String getPolishName(Enchantment enchant) {
        return LANG.get(enchant);
    }

    public static String getEnchantsFromItemStack(ItemStack item) {
        List<String> list = new ArrayList<>();
        Map<Enchantment, Integer> enchants = item.getEnchantments();
        for (Enchantment e : enchants.keySet())
            list.add(getPolishName(e) + " " + getPolishName(e));
        return StringUtils.join(list, " + ");
    }

    public static HashMap<String, Enchantment> getEnchants() {
        return enchants;
    }

    public static List<Enchantment> fromStringToEnchantList(String s) {
        List<Enchantment> list = new ArrayList<>();
        if (!s.equalsIgnoreCase("")) {
            String[] array;
            int i = (array = s.split(";")).length;
            for (int ii = 0; ii < i; ii++) {
                String ss = array[ii];
                Enchantment ench = get(ss);
                list.add(ench);
            }
        }
        return list;
    }

    public static List<Integer> fromStringToIntegerList(String s) {
        List<Integer> list = new ArrayList<>();
        if (!s.equalsIgnoreCase("")) {
            String[] array;
            int i = (array = s.split(";")).length;
            for (int ii = 0; ii < i; ii++) {
                String ss = array[ii];
                int add = Integer.parseInt(ss);
                list.add(Integer.valueOf(add));
            }
        }
        return list;
    }

    public static Enchantment getEnchantment(String string) {
        String enchantmentName = string.toLowerCase();
        if (enchants.get(enchantmentName) != null) {
            return enchants.get(enchantmentName);
        }
        return null;
    }

    public static String getEnchantsLevel(Map<Enchantment, Integer> enchants) {
        List<Integer> intlist = new ArrayList<>(enchants.values());
        return intlist.toString().replace("[", "").replace("]", "").replace(",", "/").replace(" ", "");
    }
}


