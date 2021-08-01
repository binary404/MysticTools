package binary404.mystictools.common.core.util;

import binary404.mystictools.common.loot.LootSet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class LootSetParser {

    private static final BiFunction<SuggestionsBuilder, Collection<LootSet.LootSetType>, CompletableFuture<Suggestions>> DEFAULT_SUGGESTIONS_BUILDER = (p1, p2) -> {
        return p1.buildFuture();
    };

    private final StringReader reader;
    private LootSet.LootSetType type;
    private int readerCursor;
    private BiFunction<SuggestionsBuilder, Collection<LootSet.LootSetType>, CompletableFuture<Suggestions>> suggestionsBuilder = DEFAULT_SUGGESTIONS_BUILDER;

    public LootSetParser(StringReader reader) {
        this.reader = reader;
    }

    public LootSet.LootSetType getType() {
        return type;
    }

    public void readType() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        LootSet.LootSetType type = LootSet.LootSetType.read(this.reader);
        this.type = type;
    }

    public LootSetParser parse() throws CommandSyntaxException {
        this.suggestionsBuilder = this::suggestType;
        this.readType();

        return this;
    }

    private CompletableFuture<Suggestions> suggestType(SuggestionsBuilder builder, Collection<LootSet.LootSetType> list) {
        return SharedSuggestionProvider.suggest(LootSet.LootSetType.getIds(), builder);
    }

    public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder, Collection<LootSet.LootSetType> list) {
        return this.suggestionsBuilder.apply(builder.createOffset(this.reader.getCursor()), list);
    }
}
