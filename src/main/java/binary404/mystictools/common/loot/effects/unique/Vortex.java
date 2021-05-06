package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class Vortex implements IUniqueEffect {

    @Override
    public void hit(LivingEntity target, LivingEntity attacker, ItemStack stack) {
        List<MobEntity> entities = target.world.getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(target.getPosition()).grow(15, 15, 15));
        entities.remove(attacker);
        for (MobEntity entity : entities) {
            Vector3d current = new Vector3d(entity.getPosX(), entity.getPosY(), entity.getPosZ());
            Vector3d targetVec = new Vector3d(attacker.getPosX(), entity.getPosY(), entity.getPosZ());

            Vector3d motion = targetVec.subtract(current).mul(0.6F, 0.6F, 0.6F);
            entity.setMotion(motion);

            entity.attackEntityFrom(DamageSource.FALL, 6.0F);
        }
    }
}
