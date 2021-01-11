package binary404.mystictools.common.items;

import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import net.minecraft.item.ItemStack;

public interface ILootItem {

    default float getModel(ItemStack stack) {
        return LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_MODEL);
    }

    default float getArmorModel(ItemStack stack) {
        return LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_LOOTSET);
    }

}
