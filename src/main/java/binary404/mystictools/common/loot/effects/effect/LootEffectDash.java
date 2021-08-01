package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.core.util.MathUtils;
import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LootEffectDash implements IEffectAction {

    @Override
    public boolean hasResponseMessage(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> handleUse(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return defaultAction;
        }

        Vec3 lookVector = player.getLookAngle();

        double magnitude = 18 * 0.15;
        double extraPitch = 10;

        Vec3 dashVector = new Vec3(
                lookVector.x(),
                lookVector.y(),
                lookVector.z());

        float initialYaw = (float) MathUtils.extractYaw(dashVector);

        dashVector = MathUtils.rotateYaw(dashVector, initialYaw);

        double dashPitch = Math.toDegrees(MathUtils.extractPitch(dashVector));

        if (dashPitch + extraPitch > 90) {
            dashVector = new Vec3(0, 1, 0);
            dashPitch = 90;
        } else {
            dashVector = MathUtils.rotateRoll(dashVector, (float) Math.toRadians(-extraPitch));
            dashVector = MathUtils.rotateYaw(dashVector, -initialYaw);
            dashVector = dashVector.normalize();
        }

        double coef = 1.6 - MathUtils.map(Math.abs(dashPitch),
                0.0D, 90.0D,
                0.6D, 1.0D);

        dashVector = dashVector.scale(magnitude * coef);

        player.push(dashVector.x(), dashVector.y(), dashVector.z());

        player.hurtMarked = true;

        player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 70);

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
