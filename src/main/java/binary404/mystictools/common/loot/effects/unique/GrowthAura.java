package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GrowthAura implements IUniqueEffect {

    @Override
    public void tick(Entity entity, ItemStack stack) {
        if (entity.ticksExisted % 20 == 0)
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 0; y++) {
                    for (int z = -1; z <= 1; z++) {
                        World world = entity.world;
                        BlockPos pos = entity.getPosition().add(x, y, z);
                        if (world.getBlockState(pos).getBlock() instanceof IGrowable && world.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK) {
                            if (world instanceof ServerWorld && world.rand.nextInt(1) == 0) {
                                ((IGrowable) world.getBlockState(pos).getBlock()).grow((ServerWorld) world, new Random(), pos, world.getBlockState(pos));
                                return;
                            }
                        }
                    }
                }
            }
    }
}
