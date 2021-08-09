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

    public static PotionEffectAttribute.Generator generator() {
        return new PotionEffectAttribute.Generator();
    }

    public static PotionEffectAttribute.Generator.Operator of(PotionEffectAttribute.Type type) {
        return new Generator.Operator(type);
    }

    public static class Generator extends PooledAttribute.Generator<List<PotionEffectInstance>, Generator.Operator> {
        @Override
        public List<PotionEffectInstance> getDefaultValue(Random random) {
            return new ArrayList<>();
        }

        public static class Operator extends PooledAttribute.Generator.Operator<List<PotionEffectInstance>> {
            @Expose
            protected String type;

            public Operator(Type type) {
                this.type = type.name();
            }

            public Type getType() {
                return Type.getByName(this.type).orElseThrow(() -> new IllegalStateException("Unknown type \"" + this.type + "\""));
            }

            @Override
            public List<PotionEffectInstance> apply(List<PotionEffectInstance> value, List<PotionEffectInstance> modifier) {
                if (this.getType() == Type.SET) {
                    return modifier;
                } else if (this.getType() == Type.MERGE) {
                    List<PotionEffectInstance> res = new ArrayList<>(value);
                    res.addAll(modifier);
                    return res;
                }

                return value;
            }
        }
    }

    public enum Type {
        SET, MERGE;

        public static Optional<Type> getByName(String name) {
            for (Type value : Type.values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return Optional.of(value);
                }
            }

            return Optional.empty();
        }
    }
}
