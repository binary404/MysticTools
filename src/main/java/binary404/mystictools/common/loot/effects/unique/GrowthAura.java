package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;


import java.util.Random;

public class GrowthAura implements IUniqueEffect {

    @Override
    public void tick(Entity entity, ItemStack stack) {
        if (entity.tickCount % 80 == 0)
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 0; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Level world = entity.level;
                        BlockPos pos = entity.blockPosition().offset(x, y, z);
                        if ((world.getBlockState(pos).getBlock() instanceof BonemealableBlock) && world.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK) {
                            if (world instanceof ServerLevel && world.random.nextInt(1) == 0) {
                                if (((BonemealableBlock) world.getBlockState(pos).getBlock()).isValidBonemealTarget(world, pos, world.getBlockState(pos), false)) {
                                    ((BonemealableBlock) world.getBlockState(pos).getBlock()).performBonemeal((ServerLevel) world, new Random(), pos, world.getBlockState(pos));
                                    NetworkHandler.sendToNearby(entity.level, entity, new PacketSparkle(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.1F, 0.96F, 0.1F));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
    }
}
