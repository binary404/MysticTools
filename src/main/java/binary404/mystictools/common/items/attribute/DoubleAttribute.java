package binary404.mystictools.common.items.attribute;

import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class DoubleAttribute extends NumberAttribute<Double> {

    public DoubleAttribute() {

    }

    public DoubleAttribute(ItemNBTAttribute.Modifier<Double> modifier) {
        super(modifier);
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putDouble("BaseValue", this.getBaseValue());
    }

    @Override
    public void read(CompoundTag nbt) {
        this.setBaseValue(nbt.getDouble("BaseValue"));
    }

}
