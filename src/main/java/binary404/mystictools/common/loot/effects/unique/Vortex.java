package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class Vortex implements IUniqueEffect {

    @Override
    public void hit(LivingEntity target, LivingEntity attacker, ItemStack stack) {
        List<LivingEntity> entities = target.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(target.getPosition()).expand(15, 15, 15));
        entities.remove(attacker);
        for (LivingEntity entity : entities) {
            Vec3d current = new Vec3d(entity.getPosition());
            Vec3d targetVec = new Vec3d(attacker.getPosition());

            Vec3d motion = targetVec.subtract(current).mul(0.6F, 0.6F, 0.6F);
            entity.setMotion(motion);

            entity.attackEntityFrom(DamageSource.FALL, 6.0F);
        }
    }
}
