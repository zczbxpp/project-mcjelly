package pl.zczb.tools.spigot.commands.addons;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.command.CommandSender;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.sectors.tasks.SectorUpdateTask;
import pl.zczb.tools.database.user.UserHandler;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.Set;
import java.util.stream.Collector;


public class UserDataModelResolver extends ArgumentResolver<CommandSender, UserDataModel> {
    private final UserHandler userHandler;

    public UserDataModelResolver(UserHandler userHandler) {
        this.userHandler = userHandler;
    }


    protected ParseResult<UserDataModel> parse(Invocation<CommandSender> invocation, Argument<UserDataModel> argument, String s) {
        UserDataModel user = this.userHandler.getPlayerByName(s);

        if (user == null) {
            return ParseResult.failure(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FGracz &c" + s + " &#FF3F3Fnie został znaleziony."));
        }

        return ParseResult.success(user);
    }

    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<UserDataModel> argument, SuggestionContext context) {
        Set<String> playerNames = SectorUpdateTask.players;
        if (playerNames == null || playerNames.isEmpty()) {
            return SuggestionResult.empty();
        }
        String currentArgument = context.getCurrent().multilevel();
        return (SuggestionResult) playerNames.stream()
                .filter(name -> name.toLowerCase().startsWith(currentArgument.toLowerCase()))
                .collect((Collector) SuggestionResult.collector());
    }
}


