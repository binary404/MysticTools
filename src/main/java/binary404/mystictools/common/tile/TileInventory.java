package binary404.mystictools.common.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileInventory extends TileMod implements ISidedInventory, ITickableTileEntity {

    private NonNullList<ItemStack> stacks;
    protected int[] syncedSlots;
    private NonNullList<ItemStack> syncedStacks;
    private int[] faceSlots;
    boolean initial;

    IItemHandler handlerBottom;
    private final LazyOptional<IItemHandler> bottomHandler;
    IItemHandler handlerUp;
    private final LazyOptional<IItemHandler> upHandler;
    IItemHandler handlerWest;
    private final LazyOptional<IItemHandler> westHandler;
    IItemHandler handlerEast;
    private final LazyOptional<IItemHandler> eastHandler;
    IItemHandler handlerNorth;
    private final LazyOptional<IItemHandler> northHandler;
    IItemHandler handlerSouth;
    private final LazyOptional<IItemHandler> southHandler;

    public TileInventory(TileEntityType type, int size) {
        super(type);

        this.initial = true;

        this.handlerBottom = new SidedInvWrapper(this, Direction.DOWN);
        this.bottomHandler = LazyOptional.of(() -> handlerBottom);
        this.handlerUp = new SidedInvWrapper(this, Direction.UP);
        this.upHandler = LazyOptional.of(() -> handlerUp);
        this.handlerWest = new SidedInvWrapper(this, Direction.WEST);
        this.westHandler = LazyOptional.of(() -> handlerWest);
        this.handlerEast = new SidedInvWrapper(this, Direction.EAST);
        this.eastHandler = LazyOptional.of(() -> handlerEast);
        this.handlerNorth = new SidedInvWrapper(this, Direction.NORTH);
        this.northHandler = LazyOptional.of(() -> handlerNorth);
        this.handlerSouth = new SidedInvWrapper(this, Direction.SOUTH);
        this.southHandler = LazyOptional.of(() -> handlerSouth);

        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.syncedStacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.faceSlots = new int[size];
        for (int a = 0; a < size; ) {
            this.faceSlots[a] = a;
            a++;
        }
    }

    @Override
    public int getSizeInventory() {
        return stacks.size();
    }

    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    public ItemStack getSyncedStackInSlot(int index) {
        return this.syncedStacks.get(index);
    }

    public ItemStack getStackInSlot(int index) {
        return getItems().get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(getItems(), index, count);
        if (!stack.isEmpty() && isSyncedSlot(index))
            syncSlots(null);
        markDirty();
        return stack;
    }

    public ItemStack removeStackFromSlot(int index) {
        ItemStack s = ItemStackHelper.getAndRemove(getItems(), index);
        if (isSyncedSlot(index))
            syncSlots(null);
        markDirty();
        return s;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        getItems().set(index, stack);
        if (stack.getCount() > getInventoryStackLimit())
            stack.setCount(getInventoryStackLimit());
        markDirty();
        if (isSyncedSlot(index))
            syncSlots(null);
    }

    private boolean isSyncedSlot(int slot) {
        for (int s : this.syncedSlots) {
            if (s == slot) return true;
        }
        return false;
    }

    protected void syncSlots(ServerPlayerEntity player) {
        if (this.syncedSlots.length > 0) {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT nbttaglist = new ListNBT();
            for (int i = 0; i < this.stacks.size(); i++) {
                if (!((ItemStack) this.stacks.get(i)).isEmpty() && isSyncedSlot(i)) {
                    CompoundNBT nbttagcompound1 = new CompoundNBT();
                    nbttagcompound1.putByte("Slot", (byte) i);
                    ((ItemStack) this.stacks.get(i)).write(nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }
            nbt.put("ItemsSynced", nbttaglist);
            sendMessageToClient(nbt, player);
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.DOWN)
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, bottomHandler);
            if (side == Direction.UP)
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, upHandler);
            if (side == Direction.WEST)
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, westHandler);
            if (side == Direction.EAST)
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, eastHandler);
            if (side == Direction.NORTH)
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, northHandler);
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, southHandler);
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void read(CompoundNBT par1nbtTagCompound) {
        super.read(par1nbtTagCompound);
        this.stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(par1nbtTagCompound, this.stacks);
    }


    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT par1nbtTagCompound) {
        super.write(par1nbtTagCompound);
        ItemStackHelper.saveAllItems(par1nbtTagCompound, this.stacks);
        return par1nbtTagCompound;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUsableByPlayer(PlayerEntity par1EntityPlayer) {
        return (this.world.getTileEntity(getPos()) != this) ? false : ((par1EntityPlayer.getDistanceSq(getPos().getX(), getPos().getY(), getPos().getZ()) <= 64.0D));
    }

    public boolean isItemValidForSlot(int par1, ItemStack stack2) {
        return true;
    }

    public int[] getSlotsForFace(Direction par1) {
        return this.faceSlots;
    }

    public void openInventory(PlayerEntity player) {
    }

    public void closeInventory(PlayerEntity player) {
    }

    public void clear() {
    }

    public boolean canInsertItem(int par1, ItemStack stack2, Direction par3) {
        return isItemValidForSlot(par1, stack2);
    }

    public boolean canExtractItem(int par1, ItemStack stack2, Direction par3) {
        return true;
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks) {
            if (!itemstack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void tick() {
        if (this.initial) {
            this.initial = false;
            if (!this.world.isRemote) {
                syncSlots(null);
            } else {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putBoolean("requestSync", true);
                sendMessageToServer(nbt);
            }
        }
    }

    @Override
    public void messageFromClient(CompoundNBT nbt, ServerPlayerEntity player) {
        super.messageFromClient(nbt, player);
        if (nbt.contains("requestSync"))
            syncSlots(player);
    }

    @Override
    public void messageFromServer(CompoundNBT nbt) {
        super.messageFromServer(nbt);
        if (nbt.contains("ItemsSynced")) {
            this.syncedStacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
            ListNBT list = nbt.getList("ItemsSynced", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT tag = list.getCompound(i);
                byte b0 = tag.getByte("Slot");
                if (isSyncedSlot(b0))
                    this.syncedStacks.set(b0, ItemStack.read(tag));
            }
        }
    }

}
