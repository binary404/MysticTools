package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Emerald implements IUniqueEffect {

    @Override
    public void breakBlock(BlockPos pos, Level world, Player player, ItemStack stack) {
        if (world.random.nextInt(12) == 0) {
            ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.EMERALD, 1));
            item.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            world.addFreshEntity(item);
            NetworkHandler.sendToNearby(world, player, new PacketSparkle(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.1F, 0.96F, 0.1F));
        }
    }
}
