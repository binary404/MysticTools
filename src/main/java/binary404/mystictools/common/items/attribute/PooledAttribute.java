package binary404.mystictools.common.items.attribute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;
import java.util.stream.IntStream;

public abstract class PooledAttribute<T> extends ItemNBTAttribute.Instance<T> {

    protected PooledAttribute() {

    }

    protected PooledAttribute(ItemNBTAttribute.Modifier<T> modifier) {
        super(modifier);
    }

}
