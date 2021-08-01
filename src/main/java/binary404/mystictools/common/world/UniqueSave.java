package binary404.mystictools.common.world;

import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UniqueSave extends SavedData {

    public List<UniqueInfo> uniques = new ArrayList<>();

    public UniqueSave() {
    }

    public UniqueSave load(CompoundTag nbt) {
        uniques.clear();
        List<UniqueInfo> info = new ArrayList<>();
        ListTag list = nbt.getList("list", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag cmp = list.getCompound(i);
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(cmp.getString("item")));
            UniqueEffect effect = UniqueEffect.getById(cmp.getString("effect"));
            boolean found = cmp.getBoolean("found");
            info.add(new UniqueInfo(item, effect, found));
        }
        this.uniques = info;
        return this;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        for (UniqueInfo info : uniques) {
            CompoundTag cmp = new CompoundTag();
            cmp.putString("item", info.item.getRegistryName().toString());
            cmp.putString("effect", info.effect.getId());
            cmp.putBoolean("found", info.found);
            list.add(cmp);
        }
        nbt.put("list", list);
        return nbt;
    }

    public static UniqueSave forWorld(ServerLevel world) {
        DimensionDataStorage storage = world.getDataStorage();
        UniqueSave save = new UniqueSave();
        save = (UniqueSave) storage.computeIfAbsent(save::createData, save::createData, "mystictools_unique");

        if (save == null) {
            save = new UniqueSave();
            storage.set("mystictools_unique", save);
        }
        return save;
    }

    public UniqueSave createData() {
        return new UniqueSave();
    }

    public UniqueSave createData(CompoundTag tag) {
        return createData().load(tag);
    }

    public static class UniqueInfo {

        public Item item;
        public UniqueEffect effect;
        public boolean found;

        public UniqueInfo(Item item, UniqueEffect effect, boolean found) {
            this.item = item;
            this.effect = effect;
            this.found = found;
        }

        @Override
        public String toString() {
            return item.getRegistryName() + " " + effect.getId() + " " + found;
        }
    }

}
