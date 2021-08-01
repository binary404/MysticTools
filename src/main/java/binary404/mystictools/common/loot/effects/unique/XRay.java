package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.MysticTools;
import binary404.mystictools.client.fx.FXBlock;
import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class XRay implements IUniqueEffect {

    public static int id = 0;

    @Override
    public void rightClick(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player) {
            Thread tr = new Thread(() -> {
                List<BlockPos> blocks = startSearch(entity.level, entity.blockPosition(), 32);
                for (BlockPos pos : blocks) {
                    MysticTools.proxy.blockFX(pos);
                }
            });
            tr.setName("ORE SCANNER " + id);
            id++;
            tr.start();
        }
    }

    public List<BlockPos> startSearch(Level world, BlockPos pos, int xzrange) {
        BlockPos orgin = pos;
        List<BlockPos> successful = new ArrayList<>();
        BlockPos.MutableBlockPos pooled = new BlockPos.MutableBlockPos(orgin.getX(), orgin.getY(), orgin.getZ());
        for (int xx = -xzrange; xx <= xzrange; xx++) {
            for (int zz = -xzrange; zz <= xzrange; zz++) {
                pooled.set(orgin.getX() + xx, 0, orgin.getZ() + zz);
                LevelChunk c = world.getChunkAt(pooled);
                int highest = (c.getHighestSectionPosition() + 1) * 16;
                for (int y = 0; y < highest; y++) {
                    pooled.setY(y);
                    BlockState at = c.getBlockState(pooled);
                    if (at.getBlock() instanceof OreBlock || at.getBlock() == Blocks.ANCIENT_DEBRIS || Tags.Blocks.ORES.contains(at.getBlock())) {
                        successful.add(new BlockPos(pooled));
                    }
                }
            }
        }
        return successful;
    }
}
