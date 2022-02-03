package binary404.mystictools.common.core.config;

import binary404.mystictools.common.loot.LootRarity;

public class ModConfigs {

    public static LootRarityConfig RARITIES;
    public static LootEffectConfig EFFECTS;
    public static LootCaseItemConfig ITEMS;

    public static void register() {
        RARITIES = (LootRarityConfig) new LootRarityConfig().readConfig();
        EFFECTS = (LootEffectConfig) new LootEffectConfig().readConfig();
        ITEMS = (LootCaseItemConfig) new LootCaseItemConfig().readConfig();
    }

}
