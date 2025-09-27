package pl.zczb.lobby.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatHandler implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (!e.getMessage().startsWith("/login") && !e.getMessage().startsWith("/l") && !e.getMessage().startsWith("/register") && !e.getMessage().startsWith("/reg") && !e.getMessage().startsWith("/ch") && !e.getPlayer().isOp())
            e.setCancelled(true);
    }
}


