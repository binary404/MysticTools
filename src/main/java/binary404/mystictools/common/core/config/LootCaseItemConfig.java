package binary404.mystictools.common.core.config;

import binary404.mystictools.common.core.config.entry.ItemEntry;
import binary404.mystictools.common.core.util.WeightedList;
import binary404.mystictools.common.items.ModItems;
import com.google.gson.annotations.Expose;

public class LootCaseItemConfig extends Config {

    @Expose
    public WeightedList<ItemEntry> ITEMS;

    @Override
    public String getName() {
        return "loot_items";
    }

    @Override
    protected void reset() {
        ITEMS = new WeightedList<>();
        ITEMS.add(new ItemEntry(ModItems.loot_sword), 5);
        ITEMS.add(new ItemEntry(ModItems.loot_axe), 4);
        ITEMS.add(new ItemEntry(ModItems.loot_pickaxe), 4);
        ITEMS.add(new ItemEntry(ModItems.loot_shovel), 4);
        ITEMS.add(new ItemEntry(ModItems.loot_bow), 3);
        ITEMS.add(new ItemEntry(ModItems.loot_boots), 5);
        ITEMS.add(new ItemEntry(ModItems.loot_leggings), 5);
        ITEMS.add(new ItemEntry(ModItems.loot_chestplate), 5);
        ITEMS.add(new ItemEntry(ModItems.loot_helmet), 5);
    }
}
