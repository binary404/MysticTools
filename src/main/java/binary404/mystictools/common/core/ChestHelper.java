package binary404.mystictools.common.core;

import binary404.mystictools.MysticTools;
import net.minecraft.item.Item;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mystictools")
public class ChestHelper {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        String prefix = "chests/";
        String name = event.getName().getPath();
        if (name.startsWith(prefix)) {
            String file = "inject";
            event.getTable().addPool(getInjectPool(file));
        } else if (name.startsWith("minecraft:entities/") && ConfigHandler.COMMON.enableMobDropCrate.get()) {
            String file = "entity";
            event.getTable().addPool(getInjectPool(file));
        }
    }

    public static LootPool getInjectPool(String entryName) {
        return LootPool.builder()
                .addEntry(getInjectEntry(entryName, ConfigHandler.COMMON.lootCrateWeight.get()))
                .bonusRolls(ConfigHandler.COMMON.lootCrateMinRolls.get(), ConfigHandler.COMMON.lootCrateMaxRools.get())
                .name("mystictools_inject")
                .build();
    }

    private static LootEntry.Builder getInjectEntry(String name, int weight) {
        ResourceLocation table = new ResourceLocation("mystictools", "inject/" + name);
        return TableLootEntry.builder(table)
                .weight(weight);
    }

}
