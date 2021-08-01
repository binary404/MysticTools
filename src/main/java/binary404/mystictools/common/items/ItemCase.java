package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketFX;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;


public class ItemCase extends Item {

    public ItemCase() {
        super(new Properties().tab(MysticTools.tab));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (worldIn.isClientSide)
            return super.use(worldIn, playerIn, handIn);
        ServerLevel serverWorld = ServerLifecycleHooks.getCurrentServer().getLevel(worldIn.dimension());
        ItemStack stack = playerIn.getItemInHand(handIn);

        LootRarity lootRarity = LootRarity.generateRandomRarity(worldIn.random, playerIn);

        ItemStack loot;
        if (lootRarity != null) {
            if (lootRarity == LootRarity.UNIQUE) {
                loot = UniqueHandler.getRandomUniqueItem(serverWorld, playerIn);
            } else {
                loot = LootItemHelper.getRandomLoot(worldIn.random);

                LootSet.LootSetType type = LootItemHelper.getItemType(loot.getItem());

                if (type == null)
                    type = LootSet.LootSetType.SWORD;

                loot = LootItemHelper.generateLoot(lootRarity, type, loot);
            }
            playerIn.drop(loot, false, true);
            stack.shrink(1);
            NetworkHandler.sendToNearby(worldIn, playerIn.blockPosition(), new PacketFX(playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0));
            return super.use(worldIn, playerIn, handIn);
        }
        return super.use(worldIn, playerIn, handIn);
    }
}
