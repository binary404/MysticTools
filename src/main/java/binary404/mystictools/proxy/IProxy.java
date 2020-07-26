package binary404.mystictools.proxy;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IProxy {

    default void registerHandlers() {
    }

    default void scheduleDelayed(Runnable r) {
    }

    default void scheduleDelayed(Runnable r, int delay) {
    }

    default void init() {

    }

    default void attachEventHandlers(IEventBus bus) {

    }
}
