package binary404.mystictools.common.world;

import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UniqueSave extends WorldSavedData {

    public List<UniqueInfo> uniques = new ArrayList<>();

    public UniqueSave() {
        super("mystictools_unique");
    }

    @Override
    public void read(CompoundNBT nbt) {
        uniques.clear();
        List<UniqueInfo> info = new ArrayList<>();
        ListNBT list = nbt.getList("list", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT cmp = list.getCompound(i);
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(cmp.getString("item")));
            UniqueEffect effect = UniqueEffect.getById(cmp.getString("effect"));
            boolean found = cmp.getBoolean("found");
            System.out.println(found);
            info.add(new UniqueInfo(item, effect, found));
        }
        uniques = info;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (UniqueInfo info : uniques) {
            CompoundNBT cmp = new CompoundNBT();
            cmp.putString("item", info.item.getRegistryName().toString());
            cmp.putString("effect", info.effect.getId());
            cmp.putBoolean("found", info.found);
            list.add(cmp);
        }
        nbt.put("list", list);
        return nbt;
    }

    public static UniqueSave forWorld(ServerWorld world) {
        DimensionSavedDataManager storage = world.getSavedData();
        Supplier<UniqueSave> sup = () -> new UniqueSave();
        UniqueSave saver = (UniqueSave) storage.getOrCreate(sup, "mystictools_unique");

        if (saver == null) {
            saver = new UniqueSave();
            storage.set(saver);
        }
        return saver;
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
