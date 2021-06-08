package binary404.mystictools.client.fx;

import binary404.fx_lib.fx.EffectRegistry;
import binary404.fx_lib.fx.effects.function.ColorFunction;
import binary404.fx_lib.fx.effects.handler.EffectHelper;
import binary404.fx_lib.fx.particles.ParticleDispatcher;
import binary404.fx_lib.util.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import java.util.Random;

public class FXHelper {

    public static Random rand = new Random();

    public static World getWorld() {
        return Minecraft.getInstance().world;
    }

    public static boolean fxlibLoaded() {
        return ModList.get().isLoaded("fx_lib");
    }

    public static void sparkle(double x, double y, double z, double mx, double my, double mz, float r, float g, float b, float scale, float grav, int age) {
        if (fxlibLoaded()) {
            ParticleDispatcher.GenPart part = new ParticleDispatcher.GenPart();
            int finalAge = age * 4 + rand.nextInt(age);
            boolean sp = (rand.nextFloat() < 0.2D);
            part.scale = new float[]{scale, scale * 2.0F};
            part.r = r;
            part.g = g;
            part.b = b;
            part.grid = 32;
            part.age = finalAge;
            part.partStart = sp ? 0 : 32;
            part.partNum = 16;
            part.partInc = 1;
            part.loop = true;
            part.location = new ResourceLocation("mystictools", "textures/misc/particles.png");
            part.alpha = new float[]{0.2F, 1.0F};
            part.grav = grav;
            ParticleDispatcher.genericFx(x, y, z, mx, my, mz, part);
        }
    }

    public static void shockwave(double x, double y, double z) {
        if (fxlibLoaded()) {
            EffectHelper.createEffect(EffectRegistry.SPRITE_PLANE)
                    .spawn(new Vector3(x, y + 0.2F, z))
                    .setAxis(Vector3.RotAxis.Y_AXIS)
                    .setSprite(ModEffects.sheet_shockwave)
                    .setNoRotation(0)
                    .setScaleMultiplier(12F);
        }
    }

    public static void arc(double x, double y, double z, double tx, double ty, double tz) {
        if (fxlibLoaded()) {
            EffectHelper.createEffect(EffectRegistry.LIGHTBEAM)
                    .spawn(new Vector3(x, y, z))
                    .setup(new Vector3(tx, ty, tz), 0.8F, 0.8F)
                    .color(ColorFunction.WHITE)
                    .setMaxAge(10);
        }
    }

}
