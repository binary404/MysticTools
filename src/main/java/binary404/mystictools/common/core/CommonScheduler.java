package binary404.mystictools.common.core;

import binary404.mystictools.common.core.util.Counter;
import net.minecraft.util.Tuple;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.LinkedList;

@Mod.EventBusSubscriber
public class CommonScheduler {

    private static final Object lock = new Object();

    private static boolean inTick = false;
    private static final LinkedList<Tuple<Runnable, Counter>> queue = new LinkedList<>();
    private static final LinkedList<Tuple<Runnable, Integer>> waiting = new LinkedList<>();

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            inTick = true;
            synchronized (lock) {
                inTick = true;
                Iterator<Tuple<Runnable, Counter>> iterator = queue.iterator();
                while(iterator.hasNext()) {
                    Tuple<Runnable, Counter> r = iterator.next();
                    r.getB().decrement();
                    if(r.getB().getValue() <= 0) {
                        System.out.println("Running");
                        r.getA().run();
                        iterator.remove();
                    }
                }
                inTick = false;
                for(Tuple<Runnable, Integer> wait : waiting) {
                    queue.addLast(new Tuple<>(wait.getA(), new Counter(wait.getB())));
                }
            }
            waiting.clear();
        }
    }

    public static void addRunnable(Runnable r, int tickDelay) {
        System.out.println("Adding Runnable " + r);
        synchronized (lock) {
            if(inTick) {
                waiting.addLast(new Tuple<>(r, tickDelay));
            }else {
                queue.addLast(new Tuple<>(r, new Counter(tickDelay)));
            }
        }
    }

}
