package binary404.mystictools.common.loot.effects;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Random;

public class PotionEffectInstance implements INBTSerializable<CompoundTag> {

    private MobEffect effect;
    private int duration;
    private int amplifier;

    public PotionEffectInstance(MobEffect effect, int duration, int amplifier) {
        Random random = new Random();
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    private PotionEffectInstance() {

    }

    public MobEffect getEffect() {
        return effect;
    }

    public String getId() {
        return effect.getRegistryName().toString();
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
        tag.putString("id", getId());
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
        this.effect = Registry.MOB_EFFECT.getOptional(new ResourceLocation(nbt.getString("id"))).orElse(MobEffects.MOVEMENT_SPEED);
        this.duration = nbt.getInt("duration");
        this.amplifier = nbt.getInt("amplifier");
    }
}
