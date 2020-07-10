package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketOpenCrateFX;
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
        super(new Properties().group(MysticTools.tab));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        ServerWorld serverWorld = ServerLifecycleHooks.getCurrentServer().getWorld(worldIn.dimension.getType());
        ItemStack stack = playerIn.getHeldItem(handIn);

        int rarity = random.nextInt(100) + 1;
        LootRarity lootRarity;
        if (rarity <= 50)
            lootRarity = LootRarity.COMMON;
        else if (rarity > 45 && rarity <= 70)
            lootRarity = LootRarity.UNCOMMON;
        else if (rarity > 70 && rarity <= 82)
            lootRarity = LootRarity.RARE;
        else if (rarity > 82 && rarity <= 92)
            lootRarity = LootRarity.EPIC;
        else if (rarity > 92 && random.nextInt(10) <= 6)
            lootRarity = LootRarity.UNIQUE;
        else
            lootRarity = LootRarity.COMMON;

        ItemStack loot;
        if (lootRarity.getId().equals("Unique")) {
            loot = UniqueHandler.getRandomUniqueItem(serverWorld);
            playerIn.dropItem(loot, true, true);
            stack.shrink(1);
            NetworkHandler.sendToNearby(worldIn, playerIn.getPosition(), new PacketOpenCrateFX(playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ()));
            return super.onItemRightClick(worldIn, playerIn, handIn);
        } else {
            loot = LootItemHelper.getRandomLoot(random, lootRarity);

            LootSet.LootSetType type = LootItemHelper.getItemType(loot.getItem());

            if (type == null)
                type = LootSet.LootSetType.SWORD;

            loot = LootItemHelper.generateLoot(lootRarity, type, loot);
            playerIn.dropItem(loot, false, true);
            stack.shrink(1);
            NetworkHandler.sendToNearby(worldIn, playerIn.getPosition(), new PacketOpenCrateFX(playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ()));
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }
}
