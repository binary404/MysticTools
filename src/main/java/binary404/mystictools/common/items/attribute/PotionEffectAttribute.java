package binary404.mystictools.common.items.attribute;

import binary404.mystictools.common.loot.effects.PotionEffectInstance;
import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class PotionEffectAttribute extends PooledAttribute<List<PotionEffectInstance>> {

    public PotionEffectAttribute() {

    }

    public PotionEffectAttribute(ItemNBTAttribute.Modifier<List<PotionEffectInstance>> modifier) {
        super(modifier);
    }

    @Override
    public void write(CompoundTag nbt) {
        if (this.getBaseValue() == null)
            return;
        CompoundTag tag = new CompoundTag();
        ListTag effectsList = new ListTag();

        this.getBaseValue().forEach(effect -> {
            CompoundTag effectTag = effect.serializeNBT();
            effectsList.add(effectTag);
        });

        tag.put("Effects", effectsList);
        nbt.put("BaseValue", tag);
    }

    @Override
    public void read(CompoundTag nbt) {
        if (!nbt.contains("BaseValue", Constants.NBT.TAG_COMPOUND)) {
            this.setBaseValue(new ArrayList<>());
            return;
        }

        CompoundTag tag = nbt.getCompound("BaseValue");
        ListTag effectsList = tag.getList("Effects", Constants.NBT.TAG_COMPOUND);

        this.setBaseValue(effectsList.stream()
                .map(inbt -> (CompoundTag) inbt)
                .map(PotionEffectInstance::deserialize)
                .collect(Collectors.toList()));
    }
}
