package binary404.mystictools.common.items.attribute;

import com.google.gson.annotations.Expose;

import java.util.Optional;

public abstract class NumberAttribute<T> extends PooledAttribute<T> {

    protected NumberAttribute() {

    }

    protected NumberAttribute(ItemNBTAttribute.Modifier<T> modifier) {
        super(modifier);
    }

}
