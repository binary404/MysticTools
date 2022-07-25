package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.effect.ModPotions;
import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class LootEffectStun implements IEffectAction {

    @Override
    public boolean hasResponseMessage(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> handleUse(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        List<LivingEntity> livingEntities = world.getEntitiesOfClass(LivingEntity.class, new AABB(player.blockPosition()).inflate(5.0D, 5.0D, 5.0D));
        livingEntities.remove(player);
        for(LivingEntity entity : livingEntities) {
            entity.addEffect(new MobEffectInstance(ModPotions.FREEZE.get(), 50));
        }
        player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 200);
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
