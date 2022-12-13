package binary404.mystictools.common.tile;

import binary404.mystictools.common.ritual.Ritual;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SacrificialAltarBlockEntity extends SyncedBlockEntity {

    private final ItemStackHandler inventory;
    private Ritual currentRitual;
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
        pTag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if(pTag.contains("ritual") && hasLevel()) {
            this.currentRitual = level.registryAccess().registryOrThrow(Ritual.RITUAL_REGISTRY_KEY).get(new ResourceLocation(pTag.getString("ritual")));
        }
        inventory.deserializeNBT(pTag.getCompound("Inventory"));
    }

    public static void ritualTick(Level level, BlockPos pos, BlockState state, SacrificialAltarBlockEntity entity) {
        Optional<Ritual> ritualOptional = Ritual.findRitual(entity.getItems(), level);
        ritualOptional.ifPresent(ritual -> entity.currentRitual = ritual);
        System.out.println(entity.currentRitual);
    }

    public void setRitual(Ritual ritual) {
        this.currentRitual = ritual;
    }

    public ItemStack tryAddItem(ItemStack stack) {
        return ItemHandlerHelper.insertItemStacked(this.inventory, stack, false);
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for(int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if(!stack.isEmpty())
                items.add(stack);
        }
        return items;
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
