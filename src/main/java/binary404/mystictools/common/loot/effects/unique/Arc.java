package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.core.helper.util.Utils;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.effects.IUniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketArc;
import com.google.common.collect.Lists;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import java.util.List;

public class Arc implements IUniqueEffect {

    @Override
    public void hit(LivingEntity target, LivingEntity attacker, ItemStack stack, double damage) {
        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            new ArcEffect(player.level, player, target, stack, damage).start();
        }
    }

    protected LogicalSide getSide(Entity entity) {
        return entity.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER;
    }

    static class ArcEffect {
        private final Level world;
        private final Player player;
        private final LivingEntity target;
        private final ItemStack sword;
        private final double damageDealt;

        public ArcEffect(Level world, Player player, LivingEntity target, ItemStack sword, double damage) {
            this.world = world;
            this.player = player;
            this.target = target;
            this.sword = sword;
            this.damageDealt = damage;
        }

        void start() {
            if (!player.isAlive()) {
                return;
            }

            int times = 10;
            List<LivingEntity> targetedEntities = Lists.newArrayList();
            LivingEntity start = target;

            AABB box = new AABB(-20, -20, -20, 20, 20, 20);

            LivingEntity last = null;
            LivingEntity entity = (LivingEntity) start;
            while (entity != null && times > 0) {
                targetedEntities.add(entity);
                times--;

                if (last != null) {
                    Vec3 from = new Vec3(entity.getX(), entity.getY(), entity.getZ()).add(0.5, 0.5, 0.5);
                    Vec3 to = new Vec3(last.getX(), last.getY(), last.getZ()).add(0.5, 0.5, 0.5);
                    NetworkHandler.sendToNearby(world, entity, new PacketArc(from.x, from.y, from.z, to.x, to.y, to.z));
                }
                List<LivingEntity> entities = entity.level.getEntitiesOfClass(LivingEntity.class, box.move(entity.position()), Utils.selectEntities(LivingEntity.class));
                entities.remove(entity);
                if (last != null) {
                    entities.remove(last);
                }
                entities.remove(player);
                entities.removeAll(targetedEntities);

                if (!entities.isEmpty()) {
                    LivingEntity tmpEntity = entity;
                    LivingEntity closest = Utils.selectClosest(entities, (e) -> (double) e.distanceTo(tmpEntity));
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
                    e.hurt(DamageSource.playerAttack(player), (float) (damageDealt / 3.5D));
                    LootItemHelper.handleHit(sword, e, player);
                    LootItemHelper.handlePotionEffects(sword, e, player);
                });
            }
        }
    }
}
