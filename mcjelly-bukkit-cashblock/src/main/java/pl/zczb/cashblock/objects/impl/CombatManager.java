package pl.zczb.cashblock.objects.impl;

import lombok.Generated;
import org.bukkit.entity.Player;
import pl.zczb.cashblock.objects.Combat;

import java.util.concurrent.ConcurrentHashMap;

public class CombatManager {
    private static final ConcurrentHashMap<Player, Combat> combats = new ConcurrentHashMap<>();

    public static Combat getCombat(Player player) {
        return combats.get(player);
    }

    public static void CreateCombat(Player player) {
        Combat combat = new Combat(player);
        combats.put(player, combat);
    }

    public static void removeCombat(Player player) {
        combats.remove(player);
    }

    public static void removeFight(Combat c) {
        if (c != null) {
            c.setLastAttactkPlayer(null);
            c.setLastAttactTime(0L);
        }
    }
}


