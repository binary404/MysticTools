package binary404.mystictools.client.fx;

import binary404.mystictools.client.fx.data.ArcData;
import binary404.mystictools.client.fx.data.GenericParticleData;
import binary404.mystictools.client.fx.type.GenericParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.util.Random;

public class ParticleDispatcher {

    public static World world = Minecraft.getInstance().world;

    public static void lightningFX(double x, double y, double z, double tx, double ty, double tz, float r, float g, float b) {
        ArcData data = new ArcData(tx, ty, tz, r, g, b, 0.6);
        Minecraft.getInstance().world.addParticle(data, x, y, z, 0.0, 0.0, 0.0);
    }

    public static void poof(double x, double y, double z) {
        GenericParticleData poof = new GenericParticleData(5.0F, 0.8F, 0.2F, 0.8F, 16, 40, 123, 5, 1, false);
        poof.setAlpha(1.0F, 0.0F);
        poof.setRotation(new Random().nextFloat(), new Random().nextBoolean() ? -1.0F : 1.0F);
        world.addParticle(poof, x, y, z, 0.0F, 0.0F, 0.F);
        poof = new GenericParticleData(10.0F, 1.0F, 0.9F, 1.0F, 16, 15, 77, 1, 1, false);
        poof.setAlpha(1.0F, 0.0f);
        poof.setRotation(world.rand.nextFloat(), (float) world.rand.nextGaussian());
        world.addParticle(poof, x, y, z, 0.0D, 0.0D, 0.0D);
    }

}
