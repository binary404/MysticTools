package binary404.mystictools.common.items.attribute;

import binary404.mystictools.common.loot.effects.LootEffectInstance;
import binary404.mystictools.common.loot.effects.PotionEffectInstance;
import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class LootEffectAttribute extends PooledAttribute<List<LootEffectInstance>> {

    public LootEffectAttribute() {

    }

    protected LootEffectAttribute(ItemNBTAttribute.Modifier<List<LootEffectInstance>> modifier) {
        super(modifier);
    }

    @Override
    public void write(CompoundTag nbt) {
        if (this.getBaseValue() == null) {
            return;
        }
        CompoundTag tag = new CompoundTag();
        ListTag effectList = new ListTag();

        this.getBaseValue().forEach(effect -> {
            CompoundTag effectTag = effect.serializeNBT();
            effectList.add(effectTag);
        });

        tag.put("Effects", effectList);
        nbt.put("BaseValue", tag);
    }

    @Override
    public void read(CompoundTag nbt) {
        if (!nbt.contains("BaseValue", Tag.TAG_COMPOUND)) {
            this.setBaseValue(new ArrayList<>());
            return;
        }

        CompoundTag tag = nbt.getCompound("BaseValue");
        ListTag effectList = tag.getList("Effects", Tag.TAG_COMPOUND);

        this.setBaseValue(effectList.stream()
                .map(inbt -> (CompoundTag) inbt)
                .map(LootEffectInstance::deserialize)
                .collect(Collectors.toList()));
    }

}
