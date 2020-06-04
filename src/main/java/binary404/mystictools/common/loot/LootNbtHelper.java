package binary404.mystictools.common.loot;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

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

    public static boolean getLootBooleanValue(ItemStack stack, String key)
    {
        boolean value = false;

        if (stack.hasTag() && stack.getTag().contains(LootTags.LOOT_TAG))
        {
            if (stack.getTag().getCompound(LootTags.LOOT_TAG).contains(key))
            {
                value = stack.getTag().getCompound(LootTags.LOOT_TAG).getBoolean(key);
            }
        }

        return value;
    }

    public static void setLootBooleanValue(ItemStack stack, String key, boolean value)
    {
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());

        if (!stack.getTag().contains(LootTags.LOOT_TAG))
            stack.getTag().put(LootTags.LOOT_TAG, new CompoundNBT());

        stack.getTag().getCompound(LootTags.LOOT_TAG).putBoolean(key, value);
    }

    public static void setLootIntValue(ItemStack stack, String key, int value) {
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());

        if (!stack.getTag().contains(LootTags.LOOT_TAG))
            stack.getTag().put(LootTags.LOOT_TAG, new CompoundNBT());

        stack.getTag().getCompound(LootTags.LOOT_TAG).putInt(key, value);
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

    public static ListNBT getLootTagList(ItemStack stack, String key) {
        ListNBT list = new ListNBT();

        if (stack.hasTag() && stack.getTag().contains(LootTags.LOOT_TAG)) {
            if (stack.getTag().getCompound(LootTags.LOOT_TAG).contains(key)) {
                list = stack.getTag().getCompound(LootTags.LOOT_TAG).getList(key, 10);
            }
        }

        return list;
    }

    public static CompoundNBT getLootCompound(ItemStack stack, String key) {
        CompoundNBT compound = new CompoundNBT();

        if (stack.hasTag() && stack.getTag().contains(LootTags.LOOT_TAG)) {
            if (stack.getTag().getCompound(LootTags.LOOT_TAG).contains(key)) {
                compound = stack.getTag().getCompound(LootTags.LOOT_TAG).getCompound(key);
            }
        }

        return compound;
    }

    public static void setLootTagList(ItemStack stack, String key, ListNBT value) {
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());

        if (!stack.getTag().contains(LootTags.LOOT_TAG))
            stack.getTag().put(LootTags.LOOT_TAG, new CompoundNBT());

        stack.getTag().getCompound(LootTags.LOOT_TAG).put(key, value);
    }

    public static String getLootStringValue(ItemStack stack, String key) {
        String value = "";

        if (stack.hasTag() && stack.getTag().contains(LootTags.LOOT_TAG)) {
            if (stack.getTag().getCompound(LootTags.LOOT_TAG).contains(key)) {
                value = stack.getTag().getCompound(LootTags.LOOT_TAG).getString(key);
            }
        }

        return value;
    }

    public static void setLootStringValue(ItemStack stack, String key, String value) {
        if (!stack.hasTag())
            stack.setTag(new CompoundNBT());

        if (!stack.getTag().contains(LootTags.LOOT_TAG))
            stack.getTag().put(LootTags.LOOT_TAG, new CompoundNBT());

        stack.getTag().getCompound(LootTags.LOOT_TAG).putString(key, value);
    }
}
