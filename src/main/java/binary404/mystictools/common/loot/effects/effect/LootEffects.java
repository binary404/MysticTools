package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.effects.IEffectAction;

public enum LootEffects {
    SLEEP(new LootEffectActionSleep()),
    AREA_MINER(new LootEffectAreaMiner()),
    AUTOSMELT(new LootEffectAutoSmelt()),
    DASH(new LootEffectDash()),
    HEAL(new LootEffectHeal()),
    LIGHTNING(new LootEffectLightning()),
    SHOCKWAVE(new LootEffectShockwave()),
    STUN(new LootEffectStun()),
    VOID(new LootEffectVoid()),
    POTION_CLOUD(new LootEffectPotionCloud()),
    BLAST(new LootEffectBlast()),
    SILKY(new LootEffectSilky());

    public IEffectAction action;

    LootEffects(IEffectAction action) {
        this.action = action;
    }

}
