package binary404.mystictools.common.core;

import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UniqueHandler {

    public static List<UniqueInfo> uniques = new ArrayList<>();

    public static void generateUniqueItems() {
        for (int i = 0; i <= 5; i++) {
            ItemStack stack = LootItemHelper.getRandomLoot(new Random(), LootRarity.UNIQUE);
            UniqueEffect effect = LootItemHelper.getRandomUnique(new Random(), LootItemHelper.getItemType(stack.getItem()));
            if (stack.getItem().getRegistryName() == null)
                stack = new ItemStack(ModItems.loot_pickaxe);
            if (effect == null)
                effect = UniqueEffect.xray;
            UniqueInfo info = new UniqueInfo(stack.getItem(), effect, false);
            uniques.add(info);
        }
    }

    public static ItemStack getRandomUniqueItem() {
        int randomInt = new Random().nextInt(uniques.size());
        UniqueInfo info = uniques.get(randomInt);
        if (info.found)
            getRandomUniqueItemReturn();
        ItemStack loot = new ItemStack(info.item);
        LootSet.LootSetType type = LootItemHelper.getItemType(loot.getItem());
        LootRarity lootRarity = LootRarity.UNIQUE;
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("HideFlags", 2);
        CompoundNBT lootTag = new CompoundNBT();
        Random random = new Random();
        int model = 1 + random.nextInt(type.models);

        lootTag.putInt(LootTags.LOOT_TAG_MODEL, model);

        lootTag.putString(LootTags.LOOT_TAG_UUID, UUID.randomUUID().toString());

        lootTag.putString(LootTags.LOOT_TAG_RARITY, lootRarity.getId());
        lootTag.putInt(LootTags.LOOT_TAG_DAMAGE, lootRarity.getDamage(random));
        lootTag.putFloat(LootTags.LOOT_TAG_SPEED, lootRarity.getSpeed(random));
        lootTag.putFloat(LootTags.LOOT_TAG_EFFICIENCY, lootRarity.getEfficiency(random));
        lootTag.putInt(LootTags.LOOT_TAG_DURABILITY, lootRarity.getDurability(random));
        lootTag.putInt(LootTags.LOOT_TAG_LEVEL, 10);
        lootTag.putInt(LootTags.LOOT_TAG_UPGRADE, 0);

        int modifierCount = lootRarity.getPotionCount(random);

        if (modifierCount > 0) {
            List<PotionEffect> appliedEffects = new ArrayList<>();
            ListNBT effectList = new ListNBT();

            for (int m = 0; m < modifierCount; m++) {
                PotionEffect effect = LootItemHelper.getRandomPotionExcluding(random, type, appliedEffects);

                if (effect != null) {
                    effectList.add(effect.getNbt(random));
                    appliedEffects.add(effect);
                } else {
                }
            }
            lootTag.put(LootTags.LOOT_TAG_POTIONLIST, effectList);
        }

        lootTag.put(LootTags.LOOT_TAG_UNIQUE, info.effect.getNbt());

        tag.put(LootTags.LOOT_TAG, lootTag);
        tag.putBoolean("Unbreakable", true);

        loot.setTag(tag);

        String loot_name = LootSet.getNameForType(type, random);

        if (loot_name.length() > 0) {
            LootNbtHelper.setLootStringValue(loot, LootTags.LOOT_TAG_NAME, loot_name);
        }
        info.found = true;
        uniques.add(randomInt, info);
        System.out.println(info.found);
        System.out.println(loot.getTag());
        return loot;
    }


    public static ItemStack getRandomUniqueItemReturn() {
        UniqueInfo info = uniques.get(new Random().nextInt(uniques.size()));
        if (info.found)
            return new ItemStack(ModItems.loot_sword);
        return getRandomUniqueItem();
    }

    public static class UniqueInfo {

        public Item item;
        public UniqueEffect effect;
        public boolean found;

        public UniqueInfo(Item item, UniqueEffect effect, boolean found) {
            this.item = item;
            this.effect = effect;
            this.found = found;
        }

    }

}
