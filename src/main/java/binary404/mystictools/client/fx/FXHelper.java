package binary404.mystictools.client.fx;

import binary404.fx_lib.fx.ParticleGenerator;
import binary404.fx_lib.fx.effects.FXGen;
import binary404.fx_lib.fx.effects.FXSourceOrbital;
import binary404.fx_lib.fx.effects.ParticleOrbitalController;
import binary404.fx_lib.fx.effects.function.FXMotionController;
import binary404.fx_lib.util.Vector3;
import binary404.mystictools.client.fx.lightning.FXLightning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.Random;

public class FXHelper {

    public static Random rand = new Random();

    public static ClientLevel getWorld() {
        return Minecraft.getInstance().level;
    }

    public static boolean fxlibLoaded() {
        return ModList.get().isLoaded("fx_lib");
    }

    public static ParticleGenerator sparkle;

    static {
        if(fxlibLoaded()) {
            sparkle = new ParticleGenerator(new ResourceLocation("mystictools", "textures/misc/particles.png"), 10)
                    .setGrid(32)
                    .setParticle(32, 16, 1)
                    .setLoop(true)
                    .setAlpha(0.2F, 0.8F, 0.0F);
        }
    }

    public static void sparkle(double x, double y, double z, double mx, double my, double mz, float r, float g, float b, float r2, float g2, float b2, float scale, float grav, int age) {
        if (fxlibLoaded()) {
            boolean sp = (rand.nextFloat() < 0.2D);
            sparkle.copy()
                    .setScale(scale, scale * 1.3f, scale * 0.2f)
                    .setColor(new float[] {r, r2}, new float[]{g, g2}, new float[] {b, b2})
                    .setParticle(sp ? 0 : 32, 16, 1)
                    .setGrav(grav)
                    .setAge(age)
                    .addFX(x, y, z, mx, my, mz);
        }
    }

    public static void shockwave(double x, double y, double z, double radius) {
        if (fxlibLoaded()) {
            for(double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 36) {
                final double xa = radius * Math.cos(angle);
                final double za = radius * Math.sin(angle);

                Vector3 pos = new Vector3(x, y, z).add(xa, 0, za);
                sparkle.copy()
                        .setScale(0.2f, 0.01f)
                        .setColor(new float[]{0.27f,  0.65f}, new float[] {0.52f, 0.87f}, new float[] {0.65f, 0.98f})
                        .setAge(30)
                        .setGrav(0)
                        .addFX(pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
            }
        }
    }

    public static void arc(double x, double y, double z, double tx, double ty, double tz) {
        FXLightning lightning = new FXLightning(getWorld(), tx, ty, tz, x, y, z, 1.5f, System.currentTimeMillis(), 0xc5dae6, 0xdfe6eb);
        Minecraft.getInstance().particleEngine.add(lightning);
    }

    public static FXSourceOrbital spiralGenerator(BlockPos pos, Vector3 axis) {
        ParticleGenerator fx = sparkle.copy()
                .setScale(0.3f)
                .setColor(new float[] {0.2f}, new float[] {0.5f, 0.8f}, new float[]{0.2f})
                .setAge(10)
                .setGrav(0);
        FXGen generator = new FXGen(new FXGen.ParticleComponent(fx, 1, (mot, ppos, sourcePos, taxis) -> {
            return mot;
        }, (ppos, sourcePos, velocity, motion) -> {
            return FXMotionController.STATIC;
        }));
        FXSourceOrbital orbital = new ParticleOrbitalController(new Vector3(pos).add(0.5, 0.5, 0.5), generator)
                .setOrbitAxis(axis)
                .setOrbitRadius(1.5)
                .setBranches(2).setTicksPerRotation(55);
        return orbital;
    }

}
