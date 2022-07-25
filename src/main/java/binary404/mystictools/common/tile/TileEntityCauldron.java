package binary404.mystictools.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityCauldron extends BlockEntity {

    public TileEntityCauldron(BlockPos pos, BlockState state) {
        super(ModTiles.CAULDRON.get(), pos, state);
    }

}
