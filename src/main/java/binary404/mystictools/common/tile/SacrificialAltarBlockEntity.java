package binary404.mystictools.common.tile;

import binary404.mystictools.common.blocks.BlockRitualAltar;
import binary404.mystictools.common.core.ModRecipes;
import binary404.mystictools.common.core.helper.util.RecipeUtils;
import binary404.mystictools.common.ritual.Ritual;
import binary404.mystictools.common.ritual.RitualContext;
import binary404.mystictools.common.ritual.RitualRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

    public static boolean attemptStartRitual(Level level, BlockPos pos, BlockState state, SacrificialAltarBlockEntity entity) {
        if (!state.getValue(BlockRitualAltar.ACTIVATED)) {
            Optional<RitualRecipe> recipe = findRitual(level, entity);
            if (recipe.isPresent()) {
                entity.currentRitual = new RitualContext(recipe.get().getRitual(), pos, level);
                RecipeUtils.consume(recipe.get().getIngredients(), new RecipeWrapper(entity.inventory), null);
                level.setBlockAndUpdate(pos, state.setValue(BlockRitualAltar.ACTIVATED, true));
                return true;
            }
        }
        state.setValue(BlockRitualAltar.ACTIVATED, false);
        return false;
    }

    public static void ritualTick(Level level, BlockPos pos, BlockState state, SacrificialAltarBlockEntity entity) {

    }

    public static Optional<RitualRecipe> findRitual(Level level, SacrificialAltarBlockEntity entity) {
        return level.getRecipeManager().getRecipeFor(ModRecipes.RITUAL_TYPE.get(), new RecipeWrapper(entity.inventory), level);
    }

    public RitualContext getCurrentRitual() {
        return currentRitual;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Inventory", inventory.serializeNBT());
        if (currentRitual != RitualContext.EMPTY) {
            pTag.put("ritualContext", currentRitual.serialize());
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        inventory.deserializeNBT(pTag.getCompound("Inventory"));
        if (this.hasLevel()) {
            currentRitual = RitualContext.deserialize(pTag, this.level);
        }
    }

    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    public void setStackInSlot(int slot, ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
    }

    public ItemStack tryAddItem(ItemStack stack) {
        return ItemHandlerHelper.insertItemStacked(this.inventory, stack, false);
    }

    public int getFirstNonEmptySlot() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                return i;
            }
        }
        return -1;
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
        return new ItemStackHandler(16) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }
}
