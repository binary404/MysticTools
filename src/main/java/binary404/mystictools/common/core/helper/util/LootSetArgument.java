package binary404.mystictools.common.core.helper.util;

import binary404.mystictools.common.loot.LootSet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class LootSetArgument implements ArgumentType<LootSetInput> {

    private static final Collection<String> EXAMPLES = Arrays.asList("sword", "pickaxe", "armor_boots");

    public static LootSetArgument set() {
        return new LootSetArgument();
    }

    @Override
    public LootSetInput parse(StringReader reader) throws CommandSyntaxException {
        LootSetParser parser = (new LootSetParser(reader)).parse();
        return new LootSetInput(parser.getType());
    }

    public static <S> LootSetInput getType(CommandContext<S> context, String name) {
        return context.getArgument(name, LootSetInput.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        LootSetParser parser = new LootSetParser(reader);

        try {
            parser.parse();
        } catch (CommandSyntaxException exception) {

        }

        return parser.fillSuggestions(builder, Arrays.asList(LootSet.LootSetType.values()));
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
