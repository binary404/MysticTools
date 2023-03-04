package binary404.mystictools.common.core.config;

import binary404.mystictools.common.core.helper.util.WeightedList;
import binary404.mystictools.common.loot.effects.LootEffect;
import com.google.gson.annotations.Expose;

public class LootEffectConfig extends Config {

    @Expose
    public WeightedList<LootEffect> EFFECTS;

    @Override
    public String getName() {
        return "loot_effects";
    }

    @Override
    protected void reset() {
        EFFECTS = new WeightedList<>();
        EFFECTS.add(LootEffect.INSIGHT, 2);
        EFFECTS.add(LootEffect.POTION_CLOUD, 2);
        EFFECTS.add(LootEffect.BLAST, 2);
        EFFECTS.add(LootEffect.REACH, 2);
        EFFECTS.add(LootEffect.AREA_MINER, 2);
        EFFECTS.add(LootEffect.VOID, 2);
        EFFECTS.add(LootEffect.AUTO_SMELT, 2);
        EFFECTS.add(LootEffect.DASH, 2);
        EFFECTS.add(LootEffect.HEAL, 2);
        EFFECTS.add(LootEffect.JUMP, 2);
        EFFECTS.add(LootEffect.LEECH, 2);
        EFFECTS.add(LootEffect.LIGHTNING, 2);
        EFFECTS.add(LootEffect.MULTI, 2);
        EFFECTS.add(LootEffect.PARRY, 2);
        EFFECTS.add(LootEffect.REFLECT, 2);
        EFFECTS.add(LootEffect.SHOCKWAVE, 2);
        EFFECTS.add(LootEffect.SLEEP, 2);
        EFFECTS.add(LootEffect.STUN, 2);
        EFFECTS.add(LootEffect.HEALTH, 2);
        EFFECTS.add(LootEffect.KNOCKBACK_RESISTANCE, 2);
        EFFECTS.add(LootEffect.SILKY, 2);
        EFFECTS.add(LootEffect.LUCKY, 2);
        EFFECTS.add(LootEffect.DIRECT, 2);
    }

    public void uploadEffectsToRegistry() {
        for(WeightedList.Entry<LootEffect> effectEntry : EFFECTS) {
            LootEffect effect = effectEntry.value;
            LootEffect.REGISTRY.put(effect.getId(), effect);
        }
    }

}
