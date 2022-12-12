package binary404.mystictools.common.tile;

import binary404.mystictools.common.ritual.Ritual;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class SacrificialAltarBlockEntity extends SyncedBlockEntity {

    private final ItemStackHandler inventory;
    public Ritual currentRitual;
    public int ritualTicks = 0;

    public SacrificialAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModTiles.CAULDRON.get(), pos, state);
        this.inventory = createHandler();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if(currentRitual != null && hasLevel())
            pTag.putString("ritual", Ritual.getRegistryId(currentRitual, level).toString());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if(pTag.contains("ritual") && hasLevel()) {
            this.currentRitual = level.registryAccess().registryOrThrow(Ritual.RITUAL_REGISTRY_KEY).get(new ResourceLocation(pTag.getString("ritual")));
        }
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
