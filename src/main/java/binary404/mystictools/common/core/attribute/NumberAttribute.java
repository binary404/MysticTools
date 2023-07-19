package binary404.mystictools.common.core.attribute;

public abstract class NumberAttribute<T> extends PooledAttribute<T> {

    protected NumberAttribute() {

    }

    protected NumberAttribute(ItemNBTAttribute.Modifier<T> modifier) {
        super(modifier);
    }

}
