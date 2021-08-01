package binary404.mystictools.proxy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;

public interface IProxy {

    default void registerHandlers() {
    }

    void scheduleDelayed(Runnable r, int delay);

    default void init() {

    }

    default void attachEventHandlers(IEventBus bus) {

    }

    default Level getWorld() {
        return null;
    }

    default void blockFX(BlockPos pos) {

    }
}
