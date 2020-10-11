package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class LootEffectLightning implements IEffectAction {

    @Override
    public void handleHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        LightningBoltEntity entity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, target.world);
        entity.setPosition(target.getPosX(), target.getPosY(), target.getPosZ());
        entity.setEffectOnly(true);
        target.world.addEntity(entity);
        target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 10);
        target.setFire(15);
    }
}
