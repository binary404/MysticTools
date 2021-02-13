package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketFX;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemSelectTypeCase extends Item {

    Item item;

    public ItemSelectTypeCase(Item type) {
        super(new Properties().group(MysticTools.tab));
        this.item = type;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);
        ServerWorld serverWorld = (ServerWorld) worldIn;
        ItemStack stack = playerIn.getHeldItem(handIn);

        LootRarity rarity = LootRarity.generateRandomRarity(serverWorld.rand, playerIn);

        if (rarity == LootRarity.UNIQUE) {
            rarity = LootRarity.EPIC;
        }

        ItemStack loot;

        if (rarity != null) {
            loot = new ItemStack(item);

            LootSet.LootSetType type = LootItemHelper.getItemType(item);

            loot = LootItemHelper.generateLoot(rarity, type, loot);

            playerIn.dropItem(loot, false, true);
            stack.shrink(1);
            NetworkHandler.sendToNearby(worldIn, playerIn, new PacketFX(playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), 0));
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
