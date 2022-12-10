package binary404.mystictools.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectShake extends MobEffect {

    public EffectShake() {
        super(MobEffectCategory.HARMFUL, 0XF0E9E1);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "mystictools.potion.shake";
    }
}
