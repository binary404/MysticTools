package binary404.mystictools.common.items.attribute;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import net.minecraft.nbt.CompoundTag;

import java.util.Optional;
import java.util.Random;

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
