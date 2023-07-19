package binary404.mystictools.common.core.attribute;

public abstract class PooledAttribute<T> extends ItemNBTAttribute.Instance<T> {

    protected PooledAttribute() {

    }

    protected PooledAttribute(ItemNBTAttribute.Modifier<T> modifier) {
        super(modifier);
    }

}
