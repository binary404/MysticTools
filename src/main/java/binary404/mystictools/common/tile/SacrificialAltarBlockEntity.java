package binary404.mystictools.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class SacrificialAltarBlockEntity extends SyncedBlockEntity {

    private final ItemStackHandler inventory;

    public SacrificialAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModTiles.CAULDRON.get(), pos, state);
        this.inventory = createHandler();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }
}
