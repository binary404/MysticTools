package binary404.mystictools.common.items.attribute;

import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class FloatAttribute extends NumberAttribute<Float> {

    public FloatAttribute() {

    }

    public FloatAttribute(ItemNBTAttribute.Modifier<Float> modifier) {
        super(modifier);
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putFloat("BaseValue", this.getBaseValue());
    }

    @Override
    public void read(CompoundTag nbt) {
        this.setBaseValue(nbt.getFloat("BaseValue"));
    }

}
