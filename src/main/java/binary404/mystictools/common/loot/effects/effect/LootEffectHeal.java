package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class LootEffectHeal implements IEffectAction {

    @Override
    public boolean hasResponseMessage(PlayerEntity player, ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> handleUse(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand) {
        if(player.isSneaking() || player.getHealth() >= player.getMaxHealth()) {
            return defaultAction;
        }
        player.heal(6.0F);
        player.getCooldownTracker().setCooldown(player.getHeldItem(hand).getItem(), 40);

        return ActionResult.resultPass(player.getHeldItem(hand));
    }
}
