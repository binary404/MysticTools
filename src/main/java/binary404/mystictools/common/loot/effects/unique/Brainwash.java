package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Brainwash implements IUniqueEffect {

    @Override
    public void tick(Entity entity, ItemStack stack) {
        if (entity.tickCount % 40 == 0) {
            List<Monster> mobs = entity.level.getEntitiesOfClass(Monster.class, new AABB(entity.blockPosition()).inflate(10, 10, 10));
            if (!mobs.isEmpty()) {
                Monster target = mobs.get(0);
                Monster newTarget = mobs.get(entity.level.random.nextInt(mobs.size()));
                if (newTarget != null) {
                    target.setTarget(null);

                    /*
                    for (WrappedGoal entry : ((AccessorGoalSelector) target.targetSelector).getGoals()) {
                        if (entry.getGoal() instanceof HurtByTargetGoal) {
                            target.targetSelector.removeGoal(entry.getGoal());
                            target.targetSelector.addGoal(-1, entry.getGoal());
                            break;
                        }
                    }
                     */

                    target.setLastHurtByMob(newTarget);
                    //NetworkHandler.sendToNearby(entity.world, entity, new PacketSparkle(target.getPosX() + 0.5, target.getPosY() + 0.5, target.getPosZ() + 0.5, 0.1F, 0.25F, 0.67F));

                } else {
                    target.setTarget(null);
                }
            }
        }
    }

}
