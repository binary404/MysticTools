package binary404.mystictools.common.world;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.core.UniqueHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldHandler {

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerLevel) {
            UniqueSave save = UniqueSave.forWorld((ServerLevel) event.getWorld());
            if (save.uniques.isEmpty() || save.uniques.size() < ConfigHandler.COMMON.uniqueCount.get())
                UniqueHandler.generateUniqueItems((ServerLevel) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void worldSave(WorldEvent.Save event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerLevel) {
            UniqueSave save = UniqueSave.forWorld((ServerLevel) event.getWorld());
            save.setDirty();
        }
    }

}
