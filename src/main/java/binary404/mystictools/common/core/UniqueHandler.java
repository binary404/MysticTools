package binary404.mystictools.common.core;

import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import binary404.mystictools.common.world.UniqueSave;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UniqueHandler {

    public static void generateUniqueItems(ServerWorld world) {
        UniqueSave.forWorld(world).uniques.clear();
        for (int i = 0; i < 5; i++) {
            ItemStack stack = LootItemHelper.getRandomLoot(new Random(), LootRarity.UNIQUE);
            UniqueEffect effect = LootItemHelper.getRandomUnique(new Random(), LootItemHelper.getItemType(stack.getItem()));
            if (stack.getItem().getRegistryName() == null)
                stack = new ItemStack(ModItems.loot_pickaxe);
            UniqueSave.UniqueInfo info = new UniqueSave.UniqueInfo(stack.getItem(), effect, false);
            UniqueSave.forWorld(world).uniques.add(info);
        }
        UniqueSave.forWorld(world).markDirty();
    }

    public static void resetUniqueItems(ServerWorld world) {
        UniqueSave.forWorld(world).uniques.clear();
        UniqueSave.forWorld(world).markDirty();
        generateUniqueItems(world);
    }

    public static ItemStack getRandomUniqueItem(ServerWorld world) {
        UniqueSave save = UniqueSave.forWorld(world);
        int randomInt = new Random().nextInt(save.uniques.size());
        UniqueSave.UniqueInfo info = save.uniques.get(randomInt);
        if (info.found) {
            save.uniques.set(randomInt, info);
            save.markDirty();
            return LootItemHelper.generateLoot(LootRarity.EPIC, LootItemHelper.getItemType(info.item), new ItemStack(info.item));
        }
        LootSet.LootSetType type = LootItemHelper.getItemType(info.item);
        ItemStack loot = LootItemHelper.generateLoot(LootRarity.UNIQUE, type, new ItemStack(info.item));

        loot.getTag().getCompound(LootTags.LOOT_TAG).put(LootTags.LOOT_TAG_UNIQUE, info.effect.getNbt());
        info.found = true;
        save.uniques.set(randomInt, info);
        save.markDirty();
        System.out.println(save.uniques);
        return loot;
    }

}
