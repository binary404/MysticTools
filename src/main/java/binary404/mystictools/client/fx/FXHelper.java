package binary404.mystictools.client.fx;

import binary404.fx_lib.fx.ParticleDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Random;

public class FXHelper {

    public static Random rand = new Random();

    public static World getWorld() {
        return Minecraft.getInstance().world;
    }

    public static void sparkle(double x, double y, double z, double mx, double my, double mz, float r, float g, float b, float scale, float grav, int age) {
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
