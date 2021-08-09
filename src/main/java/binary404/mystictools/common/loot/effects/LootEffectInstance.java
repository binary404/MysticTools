package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.items.attribute.LootEffectAttribute;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Random;

public class LootEffectInstance implements INBTSerializable<CompoundTag> {

    private LootEffect effect;
    private int amplifier;

    public LootEffectInstance(LootEffect effect) {
        Random random = new Random();
        this.effect = effect;
        this.amplifier = effect.getAmplifier(random);
    }

    public LootEffectInstance() {

    }

    public LootEffect getEffect() {
        return effect;
    }

    public String getId() {
        return effect.getId();
    }

    public int getAmplifier() {
        return amplifier;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", effect.getId());
        tag.putInt("amplifier", amplifier);
        return tag;
    }

    public static LootEffectInstance deserialize(CompoundTag nbt) {
        LootEffectInstance instance = new LootEffectInstance();
        instance.deserializeNBT(nbt);
        return instance;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.effect = LootEffect.getById(nbt.getString("id"));
        this.amplifier = nbt.getInt("amplifier");
    }
}
