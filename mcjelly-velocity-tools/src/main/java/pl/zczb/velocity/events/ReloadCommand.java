package pl.zczb.velocity.events;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import pl.zczb.Velocity;

public class ReloadCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        try {

            Velocity.getInstance().loadConfig();
            source.sendMessage(Component.text("§aConfig został przeładowany!"));
        } catch (Exception e) {
            source.sendMessage(Component.text("§cWystąpił błąd przy przeładowywaniu configu."));
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("zczb.root");
    }
}
