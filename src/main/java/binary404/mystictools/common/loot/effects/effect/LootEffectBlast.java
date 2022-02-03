package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class LootEffectBlast implements IEffectAction {

    @Override
    public InteractionResultHolder<ItemStack> handleUse(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(player.blockPosition()).inflate(12), livingEntity -> true);
        entities.removeIf(e -> e == player);

        for(LivingEntity entity : entities) {
            double xDiff = player.getX() - entity.getX();
            double zDiff = player.getZ() - entity.getZ();
            if(xDiff * xDiff + zDiff * zDiff < 1.0E-4D) {
                xDiff = (Math.random() - Math.random()) * 0.01D;
                zDiff = (Math.random() - Math.random()) * 0.01D;
            }
            entity.knockback(0.4F * 10.2F, xDiff, zDiff);
            entity.hurt(DamageSource.mobAttack(player), 4.0f);
        }
        player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 80);
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public boolean hasResponseMessage(Player player, ItemStack stack) {
        return false;
    }
}
