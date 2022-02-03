package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketFX;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemSelectTypeCase extends Item {

    Item item;

    public ItemSelectTypeCase(Item type) {
        super(new Properties().tab(MysticTools.tab));
        this.item = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (worldIn.isClientSide)
            return super.use(worldIn, playerIn, handIn);
        ServerLevel serverWorld = (ServerLevel) worldIn;
        ItemStack stack = playerIn.getItemInHand(handIn);

        LootRarity rarity = LootRarity.generateRandomRarity(serverWorld.random, playerIn);

        ItemStack loot;

        if (rarity != null) {
            loot = new ItemStack(item);

            LootSet.LootSetType type = LootItemHelper.getItemType(item);

            loot = LootItemHelper.generateLoot(rarity, type, loot);

            playerIn.drop(loot, false, true);
            stack.shrink(1);
            NetworkHandler.sendToNearby(worldIn, playerIn, new PacketFX(playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0));
            return super.use(worldIn, playerIn, handIn);
        }
        return super.use(worldIn, playerIn, handIn);
    }
}
