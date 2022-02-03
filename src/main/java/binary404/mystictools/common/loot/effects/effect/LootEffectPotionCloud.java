package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.effects.PotionEffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LootEffectPotionCloud implements IEffectAction {

    @Override
    public void handleHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        System.out.println("on Hit");
        if(target.getRandom().nextInt(10) > 3) {
            List<PotionEffectInstance> potionEffects = PotionEffect.getPotionlist(stack);
            PotionEffectInstance instance = potionEffects.get(target.getRandom().nextInt(potionEffects.size()));
            AreaEffectCloud cloud = new AreaEffectCloud(target.level, target.getX(), target.getY(), target.getZ());
            cloud.addEffect(new MobEffectInstance(instance.getEffect(), instance.getDuration(), instance.getAmplifier()));
            cloud.setOwner(attacker);
            cloud.setPos(target.getX(), target.getY(), target.getZ());
            target.level.addFreshEntity(cloud);
        }
    }

    @Override
    public boolean hasResponseMessage(Player player, ItemStack stack) {
        return false;
    }
}
