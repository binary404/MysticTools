package binary404.mystictools.common.world;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.core.UniqueHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldHandler {

    @SubscribeEvent
    public static void worldLoad(LevelEvent.Load event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel) {
            UniqueSave save = UniqueSave.forWorld((ServerLevel) event.getLevel());
            if (save.uniques.isEmpty() || save.uniques.size() < ConfigHandler.COMMON.uniqueCount.get())
                UniqueHandler.generateUniqueItems((ServerLevel) event.getLevel());
        }
    }

    @SubscribeEvent
    public static void worldSave(LevelEvent.Save event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel) {
            UniqueSave save = UniqueSave.forWorld((ServerLevel) event.getLevel());
            save.setDirty();
        }
    }

}
