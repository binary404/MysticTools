package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

public class Emerald implements IUniqueEffect {

    @Override
    public void breakBlock(BlockPos pos, World world, PlayerEntity player, ItemStack stack) {
        if (world.rand.nextInt(12) == 0) {
            ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.EMERALD, 1));
            item.setPortal(pos);
            world.addEntity(item);
        }
    }
}
