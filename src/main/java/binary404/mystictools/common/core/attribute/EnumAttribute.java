package binary404.mystictools.common.core.attribute;

import net.minecraft.nbt.CompoundTag;

public class EnumAttribute<E extends Enum<E>> extends PooledAttribute<E> {

    private final Class<E> enumClass;

    public EnumAttribute(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    public EnumAttribute(Class<E> enumClass, ItemNBTAttribute.Modifier<E> modifier) {
        super(modifier);
        this.enumClass = enumClass;
    }

    public Class<E> getEnumClass() {
        return this.enumClass;
    }

    @Override
    public void write(CompoundTag nbt) {
        nbt.putString("BaseValue", this.getBaseValue().name());
    }

    @Override
    public void read(CompoundTag nbt) {
        this.setBaseValue(this.getEnumConstant(nbt.getString("BaseValue")));
    }

    public E getEnumConstant(String value) {
        try {
            return Enum.valueOf(this.getEnumClass(), value);
        } catch (Exception e) {
            E[] enumConstants = this.getEnumClass().getEnumConstants();
            System.out.println("Tried to get enum constant " + e);
            return enumConstants.length == 0 ? null : enumConstants[0];
        }
    }
}
