package binary404.mystictools.common.world.cabability;

import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UniqueSave {

    public static class Save implements IUniqueSave {
        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            if (UniqueHandler.uniques.size() <= 0) {
                UniqueHandler.generateUniqueItems();
            }
            ListNBT list = new ListNBT();
            for (UniqueHandler.UniqueInfo info : UniqueHandler.uniques) {
                CompoundNBT cmp = new CompoundNBT();
                cmp.putString("item", info.item.getRegistryName().toString());
                cmp.putString("effect", info.effect.getId());
                cmp.putBoolean("found", info.found);
                list.add(cmp);
            }
            nbt.put("list", list);
            System.out.println(nbt);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            if (!nbt.contains("list")) {
                UniqueHandler.generateUniqueItems();
                return;
            }
            List<UniqueHandler.UniqueInfo> info = new ArrayList<>();
            ListNBT list = nbt.getList("list", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundNBT cmp = list.getCompound(i);
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(cmp.getString("item")));
                UniqueEffect effect = UniqueEffect.getById(cmp.getString("effect"));
                boolean found = cmp.getBoolean("found");
                info.add(new UniqueHandler.UniqueInfo(item, effect, found));
            }
            UniqueHandler.uniques = info;
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
