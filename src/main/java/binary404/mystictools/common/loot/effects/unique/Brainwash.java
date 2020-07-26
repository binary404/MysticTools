package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class Brainwash implements IUniqueEffect {

    @Override
    public void tick(Entity entity, ItemStack stack) {
        if (entity.ticksExisted % 40 == 0) {
            List<MobEntity> mobs = entity.world.getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(entity.getPosition()).grow(10, 10, 10));
            if (!mobs.isEmpty()) {
                MobEntity target = mobs.get(0);
                MobEntity newTarget = mobs.get(entity.world.rand.nextInt(mobs.size()));
                if (newTarget != null) {
                    target.setAttackTarget(null);

                    for (PrioritizedGoal entry : target.targetSelector.goals) {
                        if (entry.getGoal() instanceof HurtByTargetGoal) {
                            target.targetSelector.removeGoal(entry.getGoal());
                            target.targetSelector.addGoal(-1, entry.getGoal());
                            break;
                        }
                    }

                    target.setRevengeTarget(newTarget);
                    NetworkHandler.sendToNearby(entity.world, entity, new PacketSparkle(target.getPosX() + 0.5, target.getPosY() + 0.5, target.getPosZ() + 0.5, 0.1F, 0.25F, 0.67F));

                }else {
                    target.setAttackTarget(null);
                }
            }
        }
    }

}
