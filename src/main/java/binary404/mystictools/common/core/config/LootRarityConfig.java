package binary404.mystictools.common.core.config;

import binary404.mystictools.common.core.helper.util.WeightedList;
import binary404.mystictools.common.loot.LootRarity;
import com.google.gson.annotations.Expose;

public class LootRarityConfig extends Config {

    @Expose
    public WeightedList<LootRarity> RARITIES;

    @Override
    public String getName() {
        return "loot_rarity";
    }

    @Override
    protected void reset() {
        this.RARITIES = new WeightedList<>();
        this.RARITIES.add(LootRarity.COMMON, 40);
        this.RARITIES.add(LootRarity.UNCOMMON, 30);
        this.RARITIES.add(LootRarity.RARE, 20);
        this.RARITIES.add(LootRarity.EPIC, 8);
        this.RARITIES.add(LootRarity.UNIQUE, 2);
    }

    public void uploadRaritiesToRegistry() {
        for(WeightedList.Entry<LootRarity> rarityEntry : RARITIES) {
            LootRarity rarity = rarityEntry.value;
            LootRarity.REGISTRY.put(rarity.getId(), rarity);
        }
    }
}
