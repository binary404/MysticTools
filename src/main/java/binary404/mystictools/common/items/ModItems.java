package binary404.mystictools.common.items;

import binary404.mystictools.common.core.RegistryHelper;
import binary404.mystictools.common.loot.ItemTypeRegistry;
import binary404.mystictools.common.loot.LootSet;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    @ObjectHolder("mystictools:loot_sword")
    public static Item loot_sword;

    @ObjectHolder("mystictools:loot_axe")
    public static Item loot_axe;

    @ObjectHolder("mystictools:loot_pickaxe")
    public static Item loot_pickaxe;

    @ObjectHolder("mystictools:loot_case")
    public static Item loot_case;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        RegistryHelper.register(r, new ItemLootSword(), "loot_sword");
        RegistryHelper.register(r, new ItemLootAxe(), "loot_axe");
        RegistryHelper.register(r, new ItemLootPickaxe(), "loot_pickaxe");
        RegistryHelper.register(r, new ItemCase(), "loot_case");
    }

}
