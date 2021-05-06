package binary404.mystictools.common.core.util;

import binary404.mystictools.common.loot.LootRarity;

import java.util.function.Predicate;

public class RarityInput implements Predicate<LootRarity> {

    private final LootRarity rarity;

    public RarityInput(LootRarity input) {
        this.rarity = input;
    }

    public LootRarity getRarity() {
        return rarity;
    }

    @Override
    public boolean test(LootRarity lootRarity) {
        return lootRarity == this.rarity;
    }

    public String serialize() {
        StringBuilder builder = new StringBuilder(this.rarity.getId());
        return builder.toString();
    }
}
