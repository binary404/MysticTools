package binary404.mystictools.common.core.helper.util;

import binary404.mystictools.common.loot.LootRarity;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RarityArgument implements ArgumentType<RarityInput> {

    private static final Collection<String> EXAMPLES = Arrays.asList("common", "rare", "uncommon");

    public static RarityArgument rarity() {
        return new RarityArgument();
    }

    @Override
    public RarityInput parse(StringReader reader) throws CommandSyntaxException {
        RarityParser parser = (new RarityParser(reader)).parse();
        return new RarityInput(parser.getRarity());
    }

    public static <S> RarityInput getRarity(CommandContext<S> context, String name) {
        return context.getArgument(name, RarityInput.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        RarityParser parser = new RarityParser(reader);

        try {
            parser.parse();
        }catch (CommandSyntaxException exception) {

        }

        return parser.fillSuggestions(builder, LootRarity.REGISTRY.values());
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
