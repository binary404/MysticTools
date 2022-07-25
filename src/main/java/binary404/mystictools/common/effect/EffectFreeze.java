package binary404.mystictools.common.effect;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = "mystictools")
public class EffectFreeze extends MobEffect {

    protected EffectFreeze() {
        super(MobEffectCategory.HARMFUL, Color.CYAN.getRGB());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (shouldFreeze(event.getEntity())) {
            handleImportantTicks(event.getEntity());
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
        if (shouldFreeze(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDestroy(LivingDestroyBlockEvent event) {
        if (shouldFreeze(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    static void handleImportantTicks(LivingEntity entity) {
        if (entity.hurtTime > 0) {
            --entity.hurtTime;
        }
        if (entity.invulnerableTime > 0) {
            --entity.invulnerableTime;
        }
        entity.xo = entity.getX();
        entity.yo = entity.getY();
        entity.zo = entity.getZ();
        entity.animationSpeedOld = entity.animationSpeed;
        entity.yBodyRotO = entity.yBodyRot;
        entity.xRotO = entity.getXRot();
        entity.yRotO = entity.getYRot();
        entity.yHeadRotO = entity.yHeadRot;
        entity.oAttackAnim = entity.attackAnim;
        entity.walkDistO = entity.walkDist;

        if (entity.hasEffect(ModPotions.FREEZE.get())) {
            MobEffectInstance e = entity.getEffect(ModPotions.FREEZE.get());
            if (!e.tick(entity, () -> {
            })) {
                entity.removeEffect(ModPotions.FREEZE.get());
            }
        }
    }

    public static boolean shouldFreeze(LivingEntity entity) {
        return entity.hasEffect(ModPotions.FREEZE.get());
    }
}
