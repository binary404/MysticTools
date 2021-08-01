package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public class LootEffectLightning implements IEffectAction {

    @Override
    public void handleHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        LightningBolt entity = new LightningBolt(EntityType.LIGHTNING_BOLT, target.level);
        entity.setPos(target.getX(), target.getY(), target.getZ());
        entity.setVisualOnly(true);
        target.level.addFreshEntity(entity);
        target.setSecondsOnFire(8);
        target.hurt(DamageSource.LIGHTNING_BOLT, 10);
    }

    @Override
    public boolean hasResponseMessage(Player player, ItemStack stack) {
        return false;
    }
}
