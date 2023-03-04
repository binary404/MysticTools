package binary404.mystictools.common.tile;

import binary404.mystictools.common.core.ModRecipes;
import binary404.mystictools.common.ritual.RitualContext;
import binary404.mystictools.common.ritual.RitualRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SacrificialAltarBlockEntity extends SyncedBlockEntity {

    private final ItemStackHandler inventory;
    private RitualContext currentRitual = RitualContext.EMPTY;

    public SacrificialAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModTiles.ALTAR.get(), pos, state);
        this.inventory = createHandler();
    }

    public static void ritualTick(Level level, BlockPos pos, BlockState state, SacrificialAltarBlockEntity entity) {
        if (entity.currentRitual == RitualContext.EMPTY) {
            Optional<RitualRecipe> recipe = findRitual(level, entity);
            if (recipe.isPresent()) {
                entity.currentRitual = new RitualContext(recipe.get().getRitual(), pos, level);
            } else {
                return;
            }
        }
        System.out.println(entity.currentRitual.getRitual());
    }

    public static Optional<RitualRecipe> findRitual(Level level, SacrificialAltarBlockEntity entity) {
        Optional<RitualRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipes.RITUAL_TYPE.get(), new RecipeWrapper(entity.inventory), level);
        return recipe;
    }

    public RitualContext getCurrentRitual() {
        return currentRitual;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        inventory.deserializeNBT(pTag.getCompound("Inventory"));
    }

    public ItemStack tryAddItem(ItemStack stack) {
        return ItemHandlerHelper.insertItemStacked(this.inventory, stack, false);
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty())
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
