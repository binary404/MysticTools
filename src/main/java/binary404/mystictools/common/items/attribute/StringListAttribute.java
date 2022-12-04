package binary404.mystictools.common.items.attribute;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringListAttribute extends PooledAttribute<List<String>> {

    public StringListAttribute() {
    }

    public StringListAttribute(ItemNBTAttribute.Modifier<List<String>> modifier) {
        super(modifier);
    }

    @Override
    public void write(CompoundTag nbt) {
        if(this.getBaseValue() == null)
            return;
        CompoundTag tag = new CompoundTag();
        ListTag stringList = new ListTag();

        this.getBaseValue().forEach((string -> {
            CompoundTag stringTag = new CompoundTag();
            stringTag.putString("id", string);
            stringList.add(stringTag);
        }));

        tag.put("Strings", stringList);
        nbt.put("BaseValue", tag);
    }

    @Override
    public void read(CompoundTag nbt) {
        if(!nbt.contains("BaseValue", Tag.TAG_COMPOUND)) {
            this.setBaseValue(new ArrayList<>());
            return;
        }

        CompoundTag tag = nbt.getCompound("BaseValue");
        ListTag stringsList = tag.getList("Strings", Tag.TAG_COMPOUND);

        this.setBaseValue(stringsList.stream()
                .map(inbt -> (CompoundTag) inbt)
                .map(StringListAttribute::deserialize)
                .collect(Collectors.toList()));
    }

    private static String deserialize(CompoundTag nbt) {
        return nbt.getString("id");
    }
}
