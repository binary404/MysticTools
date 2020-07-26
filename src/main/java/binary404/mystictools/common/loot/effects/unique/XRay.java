package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.client.fx.data.BlockData;
import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class XRay implements IUniqueEffect {

    public static int id = 0;

    @Override
    public void rightClick(LivingEntity entity, ItemStack stack) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!player.getCooldownTracker().hasCooldown(stack.getItem())) {
                Thread tr = new Thread(() -> {
                    List<BlockPos> blocks = startSearch(entity.world, entity.getPosition(), 32);
                    for (BlockPos pos : blocks) {
                        BlockData data = new BlockData(true, 600, entity.getEntityWorld().getBlockState(pos).getBlock());
                        entity.world.addParticle(data, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
                    }
                });
                tr.setName("ORE SCANNER " + id);
                id++;
                tr.start();
            }
        }
    }

    public List<BlockPos> startSearch(World world, BlockPos pos, int xzrange) {
        BlockPos orgin = pos;
        List<BlockPos> successful = new ArrayList<>();
        BlockPos.PooledMutable pooled = BlockPos.PooledMutable.retain();
        try {
            for (int xx = -xzrange; xx <= xzrange; xx++) {
                for (int zz = -xzrange; zz <= xzrange; zz++) {
                    pooled.setPos(orgin.getX() + xx, 0, orgin.getZ() + zz);
                    Chunk c = world.getChunkAt(pooled);
                    int highest = (c.getTopFilledSegment() + 1) * 16;
                    for (int y = 0; y < highest; y++) {
                        pooled.setY(y);
                        BlockState at = c.getBlockState(pooled);
                        if (at.getBlock() instanceof OreBlock) {
                            successful.add(new BlockPos(pooled));
                        }
                    }
                }
            }
        } finally {
            pooled.close();
        }
        return successful;
    }
}
