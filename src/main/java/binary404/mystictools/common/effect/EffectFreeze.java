package binary404.mystictools.common.effect;

import binary404.mystictools.MysticTools;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = "mystictools")
public class EffectFreeze extends Effect {

    protected EffectFreeze() {
        super(EffectType.HARMFUL, Color.CYAN.getRGB());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if (shouldFreeze(event.getEntityLiving())) {
            event.setCanceled(true);
            handleImportantTicks(event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
        if (shouldFreeze(event.getEntityLiving())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDestroy(LivingDestroyBlockEvent event) {
        if (shouldFreeze(event.getEntityLiving())) {
            event.setCanceled(true);
        }
    }

    static void handleImportantTicks(LivingEntity entity) {
        if (entity.hurtTime > 0) {
            entity.hurtTime--;
        }

        if (entity.hurtResistantTime > 0) {
            entity.hurtResistantTime--;
        }

        entity.prevPosX = entity.getPosX();
        entity.prevPosY = entity.getPosY();
        entity.prevPosZ = entity.getPosZ();
        entity.prevLimbSwingAmount = entity.limbSwingAmount;
        entity.prevRenderYawOffset = entity.renderYawOffset;
        entity.prevRotationPitch = entity.rotationPitch;
        entity.prevRotationYaw = entity.rotationYawHead;
        entity.prevSwingProgress = entity.swingProgress;
        entity.prevDistanceWalkedModified = entity.distanceWalkedModified;

        if(entity.isPotionActive(ModPotions.FREEZE)) {
            EffectInstance e = entity.getActivePotionEffect(ModPotions.FREEZE);
            if(!e.tick(entity, () -> {})) {
                if(!entity.world.isRemote) {
                    entity.removePotionEffect(ModPotions.FREEZE);
                }
            }
        }
    }

    public static boolean shouldFreeze(LivingEntity entity) {
        return entity.isPotionActive(ModPotions.FREEZE);
    }
}
