package binary404.mystictools.proxy;

import binary404.mystictools.common.core.CommonScheduler;
import net.minecraftforge.eventbus.api.IEventBus;

public class ServerProxy implements IProxy {

    @Override
    public void registerHandlers() {

    }

    public void attachEventHandlers(IEventBus eventBus) {
    }

    public void init() {
    }

    @Override
    public void scheduleDelayed(Runnable r, int delay) {
        CommonScheduler.addRunnable(r, delay);
    }
}
