package pl.zczb.tools.spigot.events.autoevents.chatquiz;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import pl.zczb.Tools;
import pl.zczb.helpers.GlobalHelper;

public class ChatQuizHandler implements Listener {
    public ChatQuizHandler(QuizManager quizManager) {
        this.quizManager = quizManager;
    }

    private QuizManager quizManager;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String msg = event.getMessage().trim().toLowerCase();

        if (this.quizManager.isSolved() || this.quizManager.getCurrentQuestion() == null)
            return;
        if (this.quizManager.tryAnswer(msg)) {
            event.setCancelled(true);
            String winner = event.getPlayer().getName();

            Bukkit.getScheduler().runTask((Plugin) Tools.getInstance(), () -> {
                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), Tools.getSectorConfig().getQuiz_command().replace("{PLAYER}", winner));
                String typeString = (this.quizManager.getCurrentType() == QuizType.MATH) ? "&#F3CCFF&lMATEMATYK &8>>" : "&#D5FFCC&lPOLIGLOTA &8>>";
                Bukkit.broadcastMessage(GlobalHelper.fixColor(typeString + " &7Gracz &f" + typeString + " &7poprawnie odpowiedział: &f" + winner));
            });
        }
    }
}


