package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.client.fx.FXHelper;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketFX;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LootEffectShockwave implements IEffectAction {

    @Override
    public void handleArmorHit(ItemStack stack, LivingEntity wearer, LivingEntity attacker) {
        NetworkHandler.sendToNearby(wearer.level, wearer, new PacketFX(wearer.getX(), wearer.getY(), wearer.getZ(), 1));
        List<LivingEntity> entities = wearer.level.getEntitiesOfClass(LivingEntity.class, new AABB(wearer.blockPosition()).inflate(20.0D, 20.0D, 20.0D));
        for (LivingEntity entity : entities) {
            if (wearer == entity) {
                break;
            }
            Vec3 playerPos = new Vec3(wearer.getX(), wearer.getY() + 2.5, wearer.getZ());
            Vec3 entityPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());

            Vec3 motion = playerPos.subtract(entityPos).multiply(-1.5F, -1.5F, -1.5F);

            entity.setDeltaMovement(motion);
            entity.hurt(new DamageSource("shockwave").bypassArmor().setMagic(), 8F);
        }
    }
}
