package pl.zczb.cashblock.spigot.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.database.user.models.UserDataModel;
import pl.zczb.cashblock.objects.impl.PetManager;
import pl.zczb.cashblock.spigot.commands.admin.TurboKasaCmd;
import pl.zczb.packets.GameCreateAccountPacket;
import pl.zczb.redis.packet.Packet;

public class QuitHandler implements Listener {
    private final PetManager petHandler;

    public QuitHandler(PetManager petHandler) {
        this.petHandler = petHandler;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player p = event.getPlayer();
        UserDataModel u = Cashblock.getInstance().getUserHandler().getPlayer(p);
        TurboKasaCmd.bossBar.removePlayer(p);
        this.petHandler.removePet(p);

        Controller.getInstance().getRedis().publish("CH|cashblock_tryb", (Packet) new GameCreateAccountPacket(p.getUniqueId().toString(), Cashblock.getCashblockConfig().getSector_name(), u.serialize()));
    }
}


