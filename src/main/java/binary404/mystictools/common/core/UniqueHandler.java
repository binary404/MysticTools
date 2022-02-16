package binary404.mystictools.common.core;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import binary404.mystictools.common.world.UniqueSave;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UniqueHandler {

    public static void generateUniqueItems(ServerLevel world) {
        UniqueSave.forWorld(world).uniques.clear();
        int generated = 0;
        while (generated <= ConfigHandler.COMMON.uniqueCount.get()) {
            ItemStack stack = LootItemHelper.getRandomLoot(new Random());
            UniqueEffect effect = LootItemHelper.getRandomUnique(new Random(), LootItemHelper.getItemType(stack.getItem()));
            if (stack.getItem().getRegistryName() == null)
                stack = new ItemStack(ModItems.loot_pickaxe);
            if (effect != null) {
                UniqueSave.UniqueInfo info = new UniqueSave.UniqueInfo(stack.getItem(), effect, false);
                UniqueSave.forWorld(world).uniques.add(info);
                generated++;
            }
        }
        UniqueSave.forWorld(world).setDirty();
    }

    public static void resetUniqueItems(ServerLevel world) {
        UniqueSave.forWorld(world).uniques.clear();
        UniqueSave.forWorld(world).setDirty();
        generateUniqueItems(world);
    }

    public static ItemStack getRandomUniqueItem(ServerLevel world, Player target) {
        UniqueSave save = UniqueSave.forWorld(world);
        int randomInt = new Random().nextInt(save.uniques.size());
        UniqueSave.UniqueInfo info = save.uniques.get(randomInt);
        if (info.found) {
            save.uniques.set(randomInt, info);
            save.setDirty();
            return LootItemHelper.generateLoot(LootRarity.fromId("epic"), LootItemHelper.getItemType(info.item), new ItemStack(info.item));
        }
        LootSet.LootSetType type = LootItemHelper.getItemType(info.item);
        ItemStack loot = LootItemHelper.generateLoot(LootRarity.fromId("unique"), type, new ItemStack(info.item));

        loot.getTag().getCompound(LootTags.LOOT_TAG).put(LootTags.LOOT_TAG_UNIQUE, info.effect.getNbt());
        info.found = true;
        save.uniques.set(randomInt, info);
        save.setDirty();

        int found = 0;

        for (UniqueSave.UniqueInfo unique : save.uniques) {
            if (unique.found)
                found++;
        }

        target.displayClientMessage(new TextComponent("Unique Found! " + "(" + found + "/" + ConfigHandler.COMMON.uniqueCount.get() + ") found"), true);

        return loot;
    }

}
