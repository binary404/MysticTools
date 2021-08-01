package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Vortex implements IUniqueEffect {

    @Override
    public void hit(LivingEntity target, LivingEntity attacker, ItemStack stack) {
        List<Mob> entities = target.level.getEntitiesOfClass(Mob.class, new AABB(target.blockPosition()).inflate(15, 15, 15));
        entities.remove(attacker);
        for (Mob entity : entities) {
            Vec3 current = new Vec3(entity.getX(), entity.getY(), entity.getZ());
            Vec3 targetVec = new Vec3(attacker.getX(), entity.getY(), entity.getZ());

            Vec3 motion = targetVec.subtract(current).multiply(0.6F, 0.6F, 0.6F);
            entity.setDeltaMovement(motion);

            entity.hurt(DamageSource.FALL, 6.0F);
        }
    }
}
