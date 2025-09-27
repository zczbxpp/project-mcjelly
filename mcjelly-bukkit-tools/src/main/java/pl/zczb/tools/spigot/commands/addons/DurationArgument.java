package pl.zczb.tools.spigot.commands.addons;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.command.CommandSender;
import dev.rollczi.litecommands.suggestion.Suggestion;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DurationArgument extends ArgumentResolver<CommandSender, Duration> {



    private long parseDurationString(String input) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("(\\d+)([smhd])");
        Matcher matcher = pattern.matcher(input.toLowerCase());
        long totalMillis = 0L;

        boolean foundMatch = false;
        while (matcher.find()) {
            foundMatch = true;
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    totalMillis += (long) value * 1000L;
                    break;
                case "m":
                    totalMillis += (long) value * 60 * 1000L;
                    break;
                case "h":
                    totalMillis += (long) value * 60 * 60 * 1000L;
                    break;
                case "d":
                    totalMillis += (long) value * 24 * 60 * 60 * 1000L;
                    break;
            }
        }

        if (!foundMatch || totalMillis <= 0L) {
            throw new IllegalArgumentException("Nieprawidłowy format czasu.");
        }
        return totalMillis;
    }

    @Override
    protected ParseResult<Duration> parse(Invocation<CommandSender> invocation, Argument<Duration> argument, String s) {
        try {
            long millis = parseDurationString(s);
            return ParseResult.success(Duration.ofMillis(millis));
        } catch (IllegalArgumentException e) {
            return ParseResult.failure(e.getMessage());
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Duration> argument, SuggestionContext context) {
        String currentArgument = context.getCurrent().multilevel();

        List<String> rawSuggestions = Arrays.asList(
                "1s", "30s", "1m", "5m", "30m", "1h", "6h", "1d", "7d", "14d", "30d"
        );

        List<String> filteredStrings = rawSuggestions.stream()
                .filter(s -> s.toLowerCase().startsWith(currentArgument.toLowerCase()))
                .collect(Collectors.toList());


        return SuggestionResult.of(filteredStrings);
    }
}