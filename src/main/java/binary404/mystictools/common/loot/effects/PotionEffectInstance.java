package binary404.mystictools.common.loot.effects;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Random;

public class PotionEffectInstance implements INBTSerializable<CompoundTag> {

    private PotionEffect effect;
    private int duration;
    private int amplifier;

    public PotionEffectInstance(PotionEffect effect) {
        Random random = new Random();
        this.effect = effect;
        this.duration = effect.getDuration(random);
        this.amplifier = effect.getAmplifier(random);
    }

    private PotionEffectInstance() {

    }

    public PotionEffect getEffect() {
        return effect;
    }

    public String getId() {
        return effect.getId();
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", effect.getId());
        tag.putInt("duration", duration);
        tag.putInt("amplifier", amplifier);
        return tag;
    }

    public static PotionEffectInstance deserialize(CompoundTag nbt) {
        PotionEffectInstance instance = new PotionEffectInstance();
        instance.deserializeNBT(nbt);
        return instance;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.effect = PotionEffect.getById(nbt.getString("id"));
        this.duration = nbt.getInt("duration");
        this.amplifier = nbt.getInt("amplifier");
    }
}
