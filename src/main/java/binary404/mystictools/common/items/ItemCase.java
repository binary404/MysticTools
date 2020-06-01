package binary404.mystictools.common.items;

import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemCase extends Item {

    public ItemCase() {
        super(new Properties());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        ServerWorld serverWorld = ServerLifecycleHooks.getCurrentServer().getWorld(worldIn.dimension.getType());
        ItemStack stack = playerIn.getHeldItem(handIn);

        int rarity = random.nextInt(100) + 1;
        LootRarity lootRarity;
        if (rarity <= 55)
            lootRarity = LootRarity.UNIQUE;
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
        System.out.println(lootRarity);

        ItemStack loot;
        if (lootRarity.getId().equals("Unique")) {
            loot = UniqueHandler.getRandomUniqueItem(serverWorld);
            playerIn.dropItem(loot, false, true);
            stack.shrink(1);
            return super.onItemRightClick(worldIn, playerIn, handIn);
        } else {
            loot = LootItemHelper.getRandomLoot(random, lootRarity);

            LootSet.LootSetType type = LootItemHelper.getItemType(loot.getItem());

            if (type == null)
                type = LootSet.LootSetType.SWORD;

            loot = LootItemHelper.generateLoot(lootRarity, type, loot);
            playerIn.dropItem(loot, false, true);
            stack.shrink(1);
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }
}
