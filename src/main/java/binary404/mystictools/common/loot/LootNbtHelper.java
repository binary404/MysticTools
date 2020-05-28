package binary404.mystictools.common.loot;

import net.minecraft.item.ItemStack;

public class LootNbtHelper {

    public static int getLootIntValue(ItemStack stack, String key) {
        int value = 0;

        if (stack.hasTag() && stack.getTag().contains(LootTags.LOOT_TAG)) {
            if (stack.getTag().getCompound(LootTags.LOOT_TAG).contains(key)) {
                value = stack.getTag().getCompound(LootTags.LOOT_TAG).getInt(key);
            }
        }

        return value;
    }

    public static float getLootFloatValue(ItemStack stack, String key) {
        float value = 0;

        if (stack.hasTag() && stack.getTag().contains(LootTags.LOOT_TAG)) {
            if (stack.getTag().getCompound(LootTags.LOOT_TAG).contains(key)) {
                value = stack.getTag().getCompound(LootTags.LOOT_TAG).getFloat(key);
            }
        }
        return value;
    }
}
