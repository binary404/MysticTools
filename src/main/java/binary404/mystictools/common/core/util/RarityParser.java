package binary404.mystictools.common.core.util;

import binary404.mystictools.common.loot.LootRarity;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class RarityParser {

    private static final BiFunction<SuggestionsBuilder, Collection<LootRarity>, CompletableFuture<Suggestions>> DEFAULT_SUGGESTIONS_BUILDER = (p1, p2) -> {
        return p1.buildFuture();
    };

    private final StringReader reader;
    private LootRarity rarity;
    private int readerCursor;
    private BiFunction<SuggestionsBuilder, Collection<LootRarity>, CompletableFuture<Suggestions>> suggestionsBuilder = DEFAULT_SUGGESTIONS_BUILDER;

    public RarityParser(StringReader reader) {
        this.reader = reader;
    }

    public LootRarity getRarity() {
        return this.rarity;
    }

    public void readRarity() throws CommandSyntaxException {
        int i = this.reader.getCursor();
        LootRarity rarity = LootRarity.read(this.reader);
        this.rarity = rarity;
    }

    public RarityParser parse() throws CommandSyntaxException {
        this.suggestionsBuilder = this::suggestRarity;
        this.readRarity();

        return this;
    }

    private CompletableFuture<Suggestions> suggestRarity(SuggestionsBuilder builder, Collection<LootRarity> list) {
        return SharedSuggestionProvider.suggest(LootRarity.getRegistry().keySet(), builder);
    }

    public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder, Collection<LootRarity> list) {
        return this.suggestionsBuilder.apply(builder.createOffset(this.reader.getCursor()), list);
    }
}
