package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.util.Utils;
import binary404.mystictools.common.loot.effects.IUniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketArc;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import sun.rmi.runtime.Log;

import java.util.List;

public class Arc implements IUniqueEffect {

    @Override
    public void hit(LivingEntity target, LivingEntity attacker, ItemStack stack) {
        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            new ArcEffect(player.world, player, target).start();
        }
    }

    protected LogicalSide getSide(Entity entity) {
        return entity.getEntityWorld().isRemote() ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }

    static class ArcEffect {
        private final World world;
        private final PlayerEntity player;
        private final LivingEntity target;

        public ArcEffect(World world, PlayerEntity player, LivingEntity target) {
            this.world = world;
            this.player = player;
            this.target = target;
        }

        void start() {
            if (!player.isAlive()) {
                return;
            }

            int times = 10;
            List<LivingEntity> targetedEntities = Lists.newArrayList();
            LivingEntity start = target;

            AxisAlignedBB box = new AxisAlignedBB(-20, -20, -20, 20, 20, 20);

            LivingEntity last = null;
            LivingEntity entity = (LivingEntity) start;
            while (entity != null && times > 0) {
                targetedEntities.add(entity);
                times--;

                if (last != null) {
                    Vector3d from = new Vector3d(entity.getPosX(), entity.getPosY(), entity.getPosZ()).add(0.5, 0.5, 0.5);
                    Vector3d to = new Vector3d(last.getPosX(), last.getPosY(), last.getPosZ()).add(0.5, 0.5, 0.5);
                    NetworkHandler.sendToNearby(world, entity, new PacketArc(from.x, from.y, from.z, to.x, to.y, to.z));
                }
                List<LivingEntity> entities = entity.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, box.offset(entity.getPositionVec()), Utils.selectEntities(LivingEntity.class));
                entities.remove(entity);
                if (last != null) {
                    entities.remove(last);
                }
                entities.remove(player);
                entities.removeAll(targetedEntities);

                if (!entities.isEmpty()) {
                    LivingEntity tmpEntity = entity;
                    LivingEntity closest = Utils.selectClosest(entities, (e) -> (double) e.getDistance(tmpEntity));
                    if (closest != null && closest.isAlive()) {
                        last = entity;
                        entity = closest;
                    } else {
                        entity = null;
                    }
                } else {
                    entity = null;
                }
            }

            if (targetedEntities.size() > 1) {
                targetedEntities.forEach((e) -> {
                    e.attackEntityFrom(DamageSource.causePlayerDamage(player), 10.0F);
                });
            }
        }
    }
}
