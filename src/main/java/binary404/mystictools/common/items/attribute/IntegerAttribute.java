package binary404.mystictools.common.items.attribute;

import net.minecraft.nbt.CompoundTag;

import java.util.Random;

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

    public static Generator generator() {
        return new Generator();
    }

    public static Generator.Operator of(NumberAttribute.Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator extends NumberAttribute.Generator<Integer, Generator.Operator> {
        @Override
        public Integer getDefaultValue(Random random) {
            return 0;
        }

        public static Operator of(Type type) {
            return new Operator(type);
        }

        public static class Operator extends NumberAttribute.Generator.Operator<Integer> {
            public Operator(Type type) {
                super(type);
            }

            @Override
            public Integer apply(Integer value, Integer modifier) {
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
