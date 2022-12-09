package binary404.mystictools.common.core;

import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ScreenShakeHandler {

    private static int startingTicks;
    private static int screenShakeTicks = 0;
    private static float magnitude = 0.25F;
    private static float currentMagnitude;
    private static Vec3 originalPos = null;
    private static Vec3 lastPos;

    public static void startShake(int ticks) {
        startingTicks = ticks;
        screenShakeTicks = startingTicks;
        currentMagnitude = magnitude;
    }

    @SubscribeEvent
    public static void update(ViewportEvent.ComputeCameraAngles event) {
        if (screenShakeTicks > 0) {
            Camera camera = event.getCamera();
            lastPos = camera.getPosition();
            screenShakeTicks--;
            if (originalPos == null)
                originalPos = camera.getPosition();
            double pTicks = event.getPartialTick();
            double x = pTicks * Mth.randomBetween(RandomSource.create(), -1F, 1F) * currentMagnitude;
            double y = pTicks * Mth.randomBetween(RandomSource.create(), -0.7F, 0.7F) * currentMagnitude;
            camera.setPosition(new Vec3(lastPos.x + x, lastPos.y + y, lastPos.z));
            currentMagnitude = currentMagnitude * ((float) screenShakeTicks / (float) startingTicks) + 0.05f;
            if ((float) screenShakeTicks / (float) startingTicks < 0.45) {
                double percent = (double) screenShakeTicks / (double) startingTicks;
                double xLerp = Mth.lerp(percent, camera.getPosition().x, originalPos.x);
                double yLerp = Mth.lerp(percent, camera.getPosition().y, originalPos.y);
                camera.setPosition(new Vec3(xLerp, yLerp, camera.getPosition().z));
                lastPos = new Vec3(xLerp, yLerp, camera.getPosition().z);
            }
            if (screenShakeTicks == 0) {
                camera.setPosition(new Vec3(originalPos.x, originalPos.y, lastPos.z));
                originalPos = null;
            }
        }
    }

}
