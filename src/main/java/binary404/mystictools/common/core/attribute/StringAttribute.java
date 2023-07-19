package binary404.mystictools.common.core.attribute;

import net.minecraft.nbt.CompoundTag;

public class StringAttribute extends PooledAttribute<String> {

    public StringAttribute() {

    }

    public StringAttribute(ItemNBTAttribute.Modifier<String> modifier) {
        super(modifier);
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putString("BaseValue", this.getBaseValue());
    }

    @Override
    public void read(CompoundTag nbt) {
        this.setBaseValue(nbt.getString("BaseValue"));
    }
}
