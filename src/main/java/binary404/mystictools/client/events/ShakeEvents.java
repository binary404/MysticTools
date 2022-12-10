package binary404.mystictools.client.events;

import binary404.mystictools.common.effect.ModPotions;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ShakeEvents {

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        if (Minecraft.getInstance().player.getEffect(ModPotions.SHAKE.get()) != null && !Minecraft.getInstance().isPaused()) {
            int duration = Minecraft.getInstance().player.getEffect(ModPotions.SHAKE.get()).getDuration();
            float f = (Math.min(10, duration) + Minecraft.getInstance().getFrameTime()) * 0.1F;
            double intensity = f * Minecraft.getInstance().options.screenEffectScale().get();
            RandomSource rng = Minecraft.getInstance().player.getRandom();
            event.getCamera().move(rng.nextFloat() * 0.1F * intensity, rng.nextFloat() * 0.2F * intensity, rng.nextFloat() * 0.4F * intensity);
        }
    }

}
