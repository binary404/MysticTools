package binary404.mystictools.common.world.cabability;

import binary404.mystictools.MysticTools;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UniqueSave {

    public static class Save implements IUniqueSave {
        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("stuff", "things");
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
        }
    }

    public static class SaveProvider implements ICapabilitySerializable<INBT> {

        public static final ResourceLocation NAME = new ResourceLocation("mystictools", "unique_save");

        @CapabilityInject(IUniqueSave.class)
        public static final Capability<IUniqueSave> SAVE = null;

        private LazyOptional<IUniqueSave> instance = LazyOptional.of(SAVE::getDefaultInstance);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
            return cap == SAVE ? instance.cast() : LazyOptional.empty();
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
            return getCapability(cap, null);
        }

        @Override
        public INBT serializeNBT() {
            return SAVE.getStorage().writeNBT(SAVE, instance.orElseThrow(() -> new IllegalArgumentException("Empty Lazy Optional")), null);
        }

        @Override
        public void deserializeNBT(INBT nbt) {
            SAVE.getStorage().readNBT(SAVE, instance.orElseThrow(() -> new IllegalArgumentException("Empty Lazy Optional")), null, nbt);
        }
    }

    public static class SaveStorage implements Capability.IStorage<IUniqueSave> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IUniqueSave> capability, IUniqueSave instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IUniqueSave> capability, IUniqueSave instance, Direction side, INBT nbt) {
            if (nbt instanceof CompoundNBT)
                instance.deserializeNBT((CompoundNBT) nbt);
        }
    }

}
