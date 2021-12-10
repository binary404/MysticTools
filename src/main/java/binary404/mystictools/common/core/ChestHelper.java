package binary404.mystictools.common.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
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
        } else if (name.startsWith("entities/") && ConfigHandler.COMMON.enableMobDropShards.get()) {
            String file = "entity";
            event.getTable().addPool(getInjectPool(file));
        }

        prefix = "minecraft:chests/";
        name = event.getName().toString();

        if (name.startsWith(prefix)) {
            String type = name.substring(name.indexOf(prefix) + prefix.length());
            String[] common = new String[]{"abandoned_mineshaft", "jungle_temple", "spawn_bonus_chest", "village_blacksmith"};
            String[] uncommon = new String[]{"desert_temple", "simple_dungeon", "stronghold_corridor", "abandoned_mineshaft"};
            String[] rare = new String[]{"simple_dungeon", "stronghold_corridor", "nether_bridge"};
            String[] epic = new String[]{"nether_bridge", "stronghold_library"};
            String[] unique = new String[]{"end_city_treasure"};
            if (type.equals(common[0]) || type.equals(common[1]) || type.equals(common[2]) || type.equals(common[3])) {
                event.getTable().addPool(getInjectPool("common"));
            }
            if (type.equals(uncommon[0]) || type.equals(uncommon[1]) || type.equals(uncommon[2]) || type.equals(uncommon[3])) {
                event.getTable().addPool(getInjectPool("uncommon"));
            }
            if (type.equals(rare[0]) || type.equals(rare[1]) || type.equals(rare[2])) {
                event.getTable().addPool(getInjectPool("rare"));
            }
            if (type.equals(epic[0]) || type.equals(epic[1])) {
                event.getTable().addPool(getInjectPool("epic"));
            }
            if (type.equals(unique[0])) {
                event.getTable().addPool(getInjectPool("unique"));
            }
        }
    }

    public static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool()
                .add(getInjectEntry(entryName, ConfigHandler.COMMON.lootCrateWeight.get()))
                .name("mystictools_inject" + entryName)
                .build();
    }

    private static LootPoolEntryContainer.Builder getInjectEntry(String name, int weight) {
        ResourceLocation table = new ResourceLocation("mystictools", "inject/" + name);
        return LootTableReference.lootTableReference(table).setWeight(weight);
    }

}
