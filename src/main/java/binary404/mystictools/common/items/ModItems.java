package binary404.mystictools.common.items;

import binary404.mystictools.common.core.RegistryHelper;
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

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        RegistryHelper.register(r, new ItemLootSword(), "loot_sword");
    }

}
