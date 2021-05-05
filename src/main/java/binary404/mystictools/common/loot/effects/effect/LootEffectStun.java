package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.effect.ModPotions;
import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class LootEffectStun implements IEffectAction {

    @Override
    public boolean hasResponseMessage(PlayerEntity player, ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> handleUse(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand) {
        List<LivingEntity> livingEntities = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(player.getPosition()).grow(5.0D, 5.0D, 5.0D));
        livingEntities.remove(player);
        for(LivingEntity entity : livingEntities) {
            entity.addPotionEffect(new EffectInstance(ModPotions.FREEZE, 10));
        }
        player.getCooldownTracker().setCooldown(player.getHeldItem(hand).getItem(), 100);
        return ActionResult.resultPass(player.getHeldItem(hand));
    }
}
