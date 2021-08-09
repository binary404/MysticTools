package binary404.mystictools.common.items.attribute;

import binary404.mystictools.common.loot.effects.LootEffectInstance;
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
        if (!nbt.contains("BaseValue", Constants.NBT.TAG_COMPOUND)) {
            this.setBaseValue(new ArrayList<>());
            return;
        }

        CompoundTag tag = nbt.getCompound("BaseValue");
        ListTag effectList = tag.getList("Effects", Constants.NBT.TAG_COMPOUND);

        this.setBaseValue(effectList.stream()
                .map(inbt -> (CompoundTag) inbt)
                .map(LootEffectInstance::deserialize)
                .collect(Collectors.toList()));
    }

    public static LootEffectAttribute.Generator generator() {
        return new LootEffectAttribute.Generator();
    }

    public static LootEffectAttribute.Generator.Operator of(LootEffectAttribute.Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator extends PooledAttribute.Generator<List<LootEffectInstance>, LootEffectAttribute.Generator.Operator> {
        @Override
        public List<LootEffectInstance> getDefaultValue(Random random) {
            return new ArrayList<>();
        }

        public static class Operator extends PooledAttribute.Generator.Operator<List<LootEffectInstance>> {
            @Expose
            protected String type;

            public Operator(LootEffectAttribute.Type type) {
                this.type = type.name();
            }

            public LootEffectAttribute.Type getType() {
                return LootEffectAttribute.Type.getByName(this.type).orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            @Override
            public List<LootEffectInstance> apply(List<LootEffectInstance> value, List<LootEffectInstance> modifier) {
                if (this.getType() == LootEffectAttribute.Type.SET) {
                    return modifier;
                } else if (this.getType() == LootEffectAttribute.Type.MERGE) {
                    List<LootEffectInstance> res = new ArrayList<>(value);
                    res.addAll(modifier);
                    return res;
                }

                return value;
            }
        }
    }

    public enum Type {
        SET, MERGE;

        public static Optional<LootEffectAttribute.Type> getByName(String name) {
            for (LootEffectAttribute.Type value : LootEffectAttribute.Type.values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return Optional.of(value);
                }
            }

            return Optional.empty();
        }
    }
}
