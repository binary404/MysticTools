package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.client.fx.FXHelper;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketFX;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class LootEffectShockwave implements IEffectAction {

    @Override
    public void handleArmorHit(ItemStack stack, LivingEntity wearer, LivingEntity attacker) {
        NetworkHandler.sendToNearby(wearer.world, wearer, new PacketFX(wearer.getPosX(), wearer.getPosY(), wearer.getPosZ(), 1));
        List<LivingEntity> entities = wearer.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(wearer.getPosition()).grow(20.0D, 20.0D, 20.0D));
        for (LivingEntity entity : entities) {
            if (wearer == entity) {
                break;
            }
            Vector3d playerPos = new Vector3d(wearer.getPosX(), wearer.getPosY() + 2.5, wearer.getPosZ());
            Vector3d entityPos = new Vector3d(entity.getPosX(), entity.getPosY(), entity.getPosZ());

            Vector3d motion = playerPos.subtract(entityPos).mul(-1.5F, -1.5F, -1.5F);

            entity.setMotion(motion);
            entity.attackEntityFrom(new DamageSource("shockwave").setDamageBypassesArmor().setMagicDamage(), 8F);
        }
    }
}
