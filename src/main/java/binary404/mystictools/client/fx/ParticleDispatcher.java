package binary404.mystictools.client.fx;

import binary404.mystictools.client.fx.data.ArcData;
import net.minecraft.client.Minecraft;

public class ParticleDispatcher {

    public static void lightningFX(double x, double y, double z, double tx, double ty, double tz, float r, float g, float b) {
        ArcData data = new ArcData(tx, ty, tz, r, g, b, 0.6);
        Minecraft.getInstance().world.addParticle(data, x, y, z, 0.0, 0.0, 0.0);
    }

}
