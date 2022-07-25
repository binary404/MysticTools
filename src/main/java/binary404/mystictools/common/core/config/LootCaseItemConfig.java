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
        ITEMS.add(new ItemEntry(ModItems.loot_sword.get()), 5);
        ITEMS.add(new ItemEntry(ModItems.loot_axe.get()), 4);
        ITEMS.add(new ItemEntry(ModItems.loot_pickaxe.get()), 4);
        ITEMS.add(new ItemEntry(ModItems.loot_shovel.get()), 4);
        ITEMS.add(new ItemEntry(ModItems.loot_bow.get()), 3);
        ITEMS.add(new ItemEntry(ModItems.loot_boots.get()), 5);
        ITEMS.add(new ItemEntry(ModItems.loot_leggings.get()), 5);
        ITEMS.add(new ItemEntry(ModItems.loot_chestplate.get()), 5);
        ITEMS.add(new ItemEntry(ModItems.loot_helmet.get()), 5);
    }
}
