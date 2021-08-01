package binary404.mystictools.common.tile;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InventoryStackHandler extends ItemStackHandler {

    private final BlockEntity inventoryTile;
    private boolean allowInput, allowOutput;

    public InventoryStackHandler(BlockEntity tile, boolean allowInput, boolean allowOutput, int size) {
        super(size);
        this.inventoryTile = tile;
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (this.allowInput)
            return super.insertItem(slot, stack, simulate);
        else
            return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.allowOutput)
            return super.extractItem(slot, amount, simulate);
        else return ItemStack.EMPTY;
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.inventoryTile.setChanged();
    }
}
