package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LootEffectHeal implements IEffectAction {

    @Override
    public boolean hasResponseMessage(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> handleUse(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        if(player.isShiftKeyDown() || player.getHealth() >= player.getMaxHealth()) {
            return defaultAction;
        }
        player.heal(6.0F);
        player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 40);

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
