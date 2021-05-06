package binary404.mystictools.common.core.util;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class Counter {

    public int value;

    public Counter(int value) {
        this.value = value;
    }

    public void decrement() {
        value--;
    }

    public void increment() {
        value++;
    }

    public int getValue() {
        return value;
    }
}
