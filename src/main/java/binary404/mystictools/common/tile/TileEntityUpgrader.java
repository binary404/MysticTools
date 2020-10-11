package binary404.mystictools.common.tile;

import binary404.mystictools.common.items.ILootItem;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.*;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.PotionEffect;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;


public class TileEntityUpgrader extends TileEntity implements INamedContainerProvider {

    protected ItemStackHandler output;
    private final LazyOptional<IItemHandler> outputHolder = LazyOptional.of(() -> output);

    protected ItemStackHandler storage;
    private final LazyOptional<IItemHandler> storageHolder = LazyOptional.of(() -> storage);

    public TileEntityUpgrader() {
        super(ModTiles.UPGRADER);
        storage = new StackHandler(this, 2, true);
        output = new StackHandler(this, 1, false);
    }

    public void upgrade() {
        ItemStack loot = this.storage.getStackInSlot(0);
        ItemStack modifier = this.storage.getStackInSlot(1);

        int upgrades = LootNbtHelper.getLootIntValue(loot, LootTags.LOOT_TAG_UPGRADE);

        if (loot.getItem() instanceof ILootItem && modifier.getItem() == ModItems.charm && modifier.getCount() >= 1 && upgrades >= 1) {
            List<LootEffect> effects = LootEffect.getEffectList(loot);
            List<PotionEffect> potionEffects = PotionEffect.getPotionlist(loot);
            LootSet.LootSetType type = ItemTypeRegistry.getItemType(loot.getItem());
            if (world.rand.nextBoolean()) {
                LootEffect effect = LootItemHelper.getRandomEffectExcluding(world.rand, type, effects);
                if (effect != null) {
                    ListNBT effectList = LootNbtHelper.getLootTagList(loot, LootTags.LOOT_TAG_EFFECTLIST);
                    effectList.add(effect.getNbt(this.world.rand));
                    LootNbtHelper.setLootTagList(loot, LootTags.LOOT_TAG_EFFECTLIST, effectList);
                    --upgrades;
                    modifier.shrink(1);
                    LootNbtHelper.setLootIntValue(loot, LootTags.LOOT_TAG_UPGRADE, upgrades);
                }
            } else {
                PotionEffect effect = LootItemHelper.getRandomPotionExcluding(this.world.rand, type, potionEffects);
                if (effect != null) {
                    ListNBT potionList = LootNbtHelper.getLootTagList(loot, LootTags.LOOT_TAG_POTIONLIST);
                    potionList.add(effect.getNbt(world.rand));
                    LootNbtHelper.setLootTagList(loot, LootTags.LOOT_TAG_POTIONLIST, potionList);
                    --upgrades;
                    modifier.shrink(1);
                    LootNbtHelper.setLootIntValue(loot, LootTags.LOOT_TAG_UPGRADE, upgrades);
                }
            }
            this.output.setStackInSlot(0, loot);
            this.storage.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    public void reroll() {
        ItemStack loot = this.storage.getStackInSlot(0);
        ItemStack dice = this.storage.getStackInSlot(1);
        if (loot.getItem() instanceof ILootItem && dice.getItem() == ModItems.dice && dice.getCount() >= 1) {
            LootItemHelper.rerollStats(loot);
            this.output.setStackInSlot(0, loot);
            this.storage.setStackInSlot(0, ItemStack.EMPTY);
            dice.shrink(1);
            this.markDirty();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            this.markDirty();
            if (side == Direction.UP)
                return storageHolder.cast();
            if (side == Direction.EAST)
                return storageHolder.cast();
            if (side == Direction.NORTH)
                return storageHolder.cast();
            if (side == Direction.WEST)
                return storageHolder.cast();
            if (side == Direction.SOUTH)
                return storageHolder.cast();
            if (side == Direction.DOWN)
                return outputHolder.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.mystictools.upgrader");
    }

    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new UpgraderContainer(p_createMenu_1_, p_createMenu_2_, new CombinedInvWrapper(this.storage, this.output), this);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT c = super.write(compound);
        c.put("storage", storage.serializeNBT());

        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbtIn) {
        super.read(state, nbtIn);
        storage.deserializeNBT(nbtIn.getCompound("storage"));
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), 42, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(null, pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return nbt;
    }

    public static class StackHandler extends ItemStackHandler {
        TileEntity tileEntity;

        public boolean allowInput;

        public StackHandler(TileEntity tile, int size, boolean allowInput) {
            super(size);
            this.tileEntity = tile;
            this.allowInput = allowInput;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (this.allowInput) {
                this.tileEntity.markDirty();
                return super.insertItem(slot, stack, simulate);
            } else
                return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            this.tileEntity.markDirty();
            return super.extractItem(slot, amount, simulate);
        }


    }
}
