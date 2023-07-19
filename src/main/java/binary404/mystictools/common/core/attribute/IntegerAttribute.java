package binary404.mystictools.common.core.attribute;

import net.minecraft.nbt.CompoundTag;

public class IntegerAttribute extends NumberAttribute<Integer> {

    public IntegerAttribute() {

    }

    public IntegerAttribute(ItemNBTAttribute.Modifier<Integer> modifier) {
        super(modifier);
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putInt("BaseValue", this.getBaseValue());
    }

    @Override
    public void read(CompoundTag nbt) {
        this.setBaseValue(nbt.getInt("BaseValue"));
    }
}
