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

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(NumberAttribute.Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator extends NumberAttribute.Generator<Double, Generator.Operator> {
        @Override
        public Double getDefaultValue(Random random) {
            return 0.0D;
        }

        public static class Operator extends NumberAttribute.Generator.Operator<Double> {
            public Operator(Type type) {
                super(type);
            }

            @Override
            public Double apply(Double value, Double modifier) {
                if (this.getType() == Type.SET) {
                    return modifier;
                } else if (this.getType() == Type.ADD) {
                    return value + modifier;
                } else if (this.getType() == Type.MULTIPLY) {
                    return value * modifier;
                }

                return value;
            }
        }
    }

}
