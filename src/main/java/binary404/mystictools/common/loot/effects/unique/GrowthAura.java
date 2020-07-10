package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
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
        if (entity.ticksExisted % 80 == 0)
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 0; y++) {
                    for (int z = -1; z <= 1; z++) {
                        World world = entity.world;
                        BlockPos pos = entity.getPosition().add(x, y, z);
                        if (world.getBlockState(pos).getBlock() instanceof IGrowable && world.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK) {
                            if (world instanceof ServerWorld && world.rand.nextInt(1) == 0) {
                                if (((IGrowable) world.getBlockState(pos).getBlock()).canGrow(world, pos, world.getBlockState(pos), false)) {
                                    ((IGrowable) world.getBlockState(pos).getBlock()).grow((ServerWorld) world, new Random(), pos, world.getBlockState(pos));
                                    NetworkHandler.sendToNearby(entity.world, entity, new PacketSparkle(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.1F, 0.96F, 0.1F));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
    }
}
