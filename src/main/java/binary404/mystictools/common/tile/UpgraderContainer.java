package binary404.mystictools.common.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;



public class UpgraderContainer extends Container {

    protected final int invenSize;

    public TileEntityUpgrader tileEntity;

    public UpgraderContainer(int windowId, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(windowId, playerInventory, new ItemStackHandler(3), (TileEntityUpgrader) Minecraft.getInstance().world.getTileEntity(buffer.readBlockPos()));
    }

    public UpgraderContainer(int windowId, PlayerInventory inventory, IItemHandler handler, TileEntityUpgrader upgrader) {
        super(ModContainers.UPGRADER, windowId);
        invenSize = 3;

        bindPlayerInventory(inventory);
        addSlot(new SlotItemHandler(handler, 0, 12, 16));
        addSlot(new SlotItemHandler(handler, 1, 70, 16));
        addSlot(new SlotOutput(handler, 2, 42, 57));
        this.tileEntity = upgrader;
    }

    public class SlotOutput extends SlotItemHandler {
        public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    protected void bindPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 6 + j * 16, 86 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 6 + k * 16, 144));
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < invenSize) {
                if (!this.mergeItemStack(itemstack1, invenSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, invenSize, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
