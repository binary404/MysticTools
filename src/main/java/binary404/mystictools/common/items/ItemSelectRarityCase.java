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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class ItemSelectRarityCase extends Item {

    final LootRarity rarity;

    public ItemSelectRarityCase(@Nonnull LootRarity item) {
        super(new Properties().group(MysticTools.tab));
        this.rarity = item;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isRemote)
            return super.onItemRightClick(worldIn, playerIn, handIn);

        ServerWorld world = (ServerWorld) worldIn;
        ItemStack stack = playerIn.getHeldItem(handIn);

        ItemStack loot;

        if (this.rarity == LootRarity.UNIQUE) {
            loot = UniqueHandler.getRandomUniqueItem(world, playerIn);
        } else {
            loot = LootItemHelper.getRandomLoot(random);

            LootSet.LootSetType type = LootItemHelper.getItemType(loot.getItem());

            if (type == null)
                type = LootSet.LootSetType.SWORD;

            loot = LootItemHelper.generateLoot(this.rarity, type, loot);
        }
        playerIn.dropItem(loot, false, true);
        stack.shrink(1);
        NetworkHandler.sendToNearby(worldIn, playerIn, new PacketFX(playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), 0));
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
