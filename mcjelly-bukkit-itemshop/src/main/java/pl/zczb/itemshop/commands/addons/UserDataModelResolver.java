package pl.zczb.itemshop.commands.addons;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;
import pl.zczb.itemshop.data.user.UserHandler;
import pl.zczb.itemshop.data.user.models.UserDataModel;
import pl.zczb.itemshop.helpers.GlobalHelper;

public class UserDataModelResolver extends ArgumentResolver<CommandSender, UserDataModel> {
    private final UserHandler userHandler;

    public UserDataModelResolver(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    protected ParseResult<UserDataModel> parse(Invocation<CommandSender> invocation, Argument<UserDataModel> argument, String s) {
        UserDataModel user = this.userHandler.getPlayerByName(s);
        if (user == null)
            return ParseResult.failure(GlobalHelper.fixColor("&8[&#FF0000&l!&8] &#FF3F3FGracz &c" + s + " &#FF3F3Fnie zostaznaleziony."));
        return ParseResult.success(user);
    }


}
