package binary404.mystictools.common.core.helper.util;

import binary404.mystictools.common.loot.LootSet;

import java.util.function.Predicate;

public class LootSetInput implements Predicate<LootSet.LootSetType> {

    private final LootSet.LootSetType type;

    public LootSetInput(LootSet.LootSetType input) {
        this.type = input;
    }

    public LootSet.LootSetType getType() {
        return type;
    }

    @Override
    public boolean test(LootSet.LootSetType lootSetType) {
        return lootSetType == type;
    }


}
