package binary404.mystictools.proxy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;

public interface IProxy {

    default void registerHandlers() {
    }

    default void scheduleDelayed(Runnable r, int delay) {

    }

    default void attachEventHandlers(IEventBus bus) {

    }

    default Level getWorld() {
        return null;
    }

    default void blockFX(BlockPos pos) {

    }

    default Player getPlayer() {
        return null;
    }
}
