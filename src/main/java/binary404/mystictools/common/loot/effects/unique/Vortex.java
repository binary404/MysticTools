package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class Vortex implements IUniqueEffect {

    @Override
    public void hit(LivingEntity target, LivingEntity attacker, ItemStack stack) {
        List<LivingEntity> entities = target.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(target.func_233580_cy_()).grow(15, 15, 15));
        entities.remove(attacker);
        for (LivingEntity entity : entities) {
            Vector3d current = new Vector3d(entity.getPosX(), entity.getPosY(), entity.getPosZ());
            Vector3d targetVec = new Vector3d(attacker.getPosX(), entity.getPosY(), entity.getPosZ());

            Vector3d motion = targetVec.subtract(current).mul(0.6F, 0.6F, 0.6F);
            entity.setMotion(motion);

            entity.attackEntityFrom(DamageSource.FALL, 6.0F);
        }
    }
}
