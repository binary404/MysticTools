package binary404.mystictools.common.items;

import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.loot.effects.BasicEffect;
import binary404.mystictools.common.loot.effects.IEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemCase extends Item {

    public ItemCase() {
        super(new Properties());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);


        for (int i = 0; i < 1; ++i) {
            int rarity = random.nextInt(100) + 1;
            System.out.println(rarity);
            LootRarity lootRarity;
            if (rarity <= 55)
                lootRarity = LootRarity.COMMON;
            else if (rarity > 55 && rarity <= 80)
                lootRarity = LootRarity.UNCOMMON;
            else if (rarity > 80 && rarity <= 95)
                lootRarity = LootRarity.RARE;
            else if (rarity > 95 && rarity <= 99)
                lootRarity = LootRarity.EPIC;
            else if (rarity > 99 && random.nextInt(10) <= 6)
                lootRarity = LootRarity.UNIQUE;
            else
                lootRarity = LootRarity.COMMON;

            //Random Tool TODO
            ItemStack loot = new ItemStack(ModItems.loot_sword);

            LootSet.LootSetType type = LootSet.LootSetType.SWORD;

            CompoundNBT tag = new CompoundNBT();
            tag.putInt("HideFlags", 2);
            CompoundNBT lootTag = new CompoundNBT();

            int model = 1 + random.nextInt(type.models);

            lootTag.putInt(LootTags.LOOT_TAG_MODEL, model);

            lootTag.putString(LootTags.LOOT_TAG_UUID, UUID.randomUUID().toString());

            lootTag.putString(LootTags.LOOT_TAG_RARITY, lootRarity.getId());
            lootTag.putInt(LootTags.LOOT_TAG_DAMAGE, lootRarity.getDamage(random));
            lootTag.putFloat(LootTags.LOOT_TAG_SPEED, lootRarity.getSpeed(random));
            lootTag.putFloat(LootTags.LOOT_TAG_EFFICIENCY, lootRarity.getEfficiency(random));
            lootTag.putInt(LootTags.LOOT_TAG_DURABILITY, lootRarity.getDurability(random));
            lootTag.putInt(LootTags.LOOT_TAG_UPGRADES, lootRarity.getUpgrades(random));

            int modifierCount = lootRarity.getModifierCount(random);
            System.out.println(modifierCount);

            boolean unbreakable = false;

            if (lootRarity == LootRarity.UNIQUE)
                unbreakable = true;

            if (modifierCount > 0) {
                List<BasicEffect> appliedEffects = new ArrayList<>();
                ListNBT effectList = new ListNBT();

                for (int m = 0; m < modifierCount; m++) {
                    System.out.println("Iterating");
                    BasicEffect effect = LootItemHelper.getRandomExcluding(random, type, appliedEffects);

                    if (effect != null) {
                        System.out.println("Effect is not null");
                        effectList.add(effect.getNbt(random));
                        appliedEffects.add(effect);
                    }else {
                        System.out.println("Effect is NULL!");
                    }
                }
                if (lootRarity != LootRarity.COMMON)
                    if (random.nextInt(100) > 90)
                        unbreakable = true;
                lootTag.put(LootTags.LOOT_TAG_EFFECTLIST, effectList);
            }

            tag.put(LootTags.LOOT_TAG, lootTag);

            if (unbreakable) {
                tag.putBoolean("Unbreakable", true);
            }

            loot.setTag(tag);

            String loot_name = LootSet.getNameForType(type, random);

            if (loot_name.length() > 0) {
                LootNbtHelper.setLootStringValue(loot, LootTags.LOOT_TAG_NAME, loot_name);
            }

            if (loot != null) {
                playerIn.dropItem(loot, false, true);
            }

            stack.shrink(1);

        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
