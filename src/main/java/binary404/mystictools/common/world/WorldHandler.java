package binary404.mystictools.common.world;

import binary404.mystictools.common.core.UniqueHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WorldHandler {

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld) {
            UniqueSave save = UniqueSave.forWorld((ServerWorld) event.getWorld());
            if (save.uniques.isEmpty())
                UniqueHandler.generateUniqueItems((ServerWorld) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void worldSave(WorldEvent.Save event) {
        if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld) {
            UniqueSave save = UniqueSave.forWorld((ServerWorld) event.getWorld());
            save.markDirty();
        }
    }

}
