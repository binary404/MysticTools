package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.core.util.MathUtils;
import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class LootEffectDash implements IEffectAction {

    @Override
    public boolean hasResponseMessage(PlayerEntity player, ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> handleUse(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand) {
        if (player.isSneaking()) {
            return defaultAction;
        }

        Vector3d lookVector = player.getLookVec();

        double magnitude = 18 * 0.15;
        double extraPitch = 10;

        Vector3d dashVector = new Vector3d(
                lookVector.getX(),
                lookVector.getY(),
                lookVector.getZ());

        float initialYaw = (float) MathUtils.extractYaw(dashVector);

        dashVector = MathUtils.rotateYaw(dashVector, initialYaw);

        double dashPitch = Math.toDegrees(MathUtils.extractPitch(dashVector));

        if (dashPitch + extraPitch > 90) {
            dashVector = new Vector3d(0, 1, 0);
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

        player.addVelocity(dashVector.getX(), dashVector.getY(), dashVector.getZ());

        player.velocityChanged = true;

        player.getCooldownTracker().setCooldown(player.getHeldItem(hand).getItem(), 70);

        return ActionResult.resultPass(player.getHeldItem(hand));
    }
}
