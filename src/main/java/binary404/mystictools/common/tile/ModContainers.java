package binary404.mystictools.common.tile;

import binary404.mystictools.MysticTools;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.mystictools.common.core.RegistryHelper.register;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {

    @ObjectHolder("mystictools:upgrader")
    public static ContainerType<UpgraderContainer> UPGRADER;

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> r = event.getRegistry();
        register(r, IForgeContainerType.create(UpgraderContainer::new), "upgrader");
    }

}
