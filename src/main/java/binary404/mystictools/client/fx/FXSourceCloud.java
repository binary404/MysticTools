package binary404.mystictools.client.fx;

import binary404.fx_lib.fx.effects.ComplexFXBase;
import binary404.fx_lib.fx.effects.FXSource;
import binary404.fx_lib.fx.effects.FXSourceOrbital;
import binary404.fx_lib.util.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public abstract class FXSourceCloud extends FXSource {

    private double radius;
    private int count;

    public FXSourceCloud(Vector3 pos) {
        super(pos);
    }

    public double getRadius() {
        return this.radius;
    }

    public int getCount() {
        return this.count;
    }

    public FXSourceCloud setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public FXSourceCloud setCount(int count) {
        this.count = count;
        return this;
    }

    @Override
    public void tickSpawnFX() {
        if(!Minecraft.getInstance().isPaused()) {
            for (int i = 0; i < this.count; i++) {
                double x = Mth.nextDouble(FXHelper.getWorld().random, -radius, radius);
                double y = Mth.nextDouble(FXHelper.getWorld().random, -radius, radius);
                double z = Mth.nextDouble(FXHelper.getWorld().random, -radius, radius);
                Vector3 random = new Vector3(x, y, z);
                Vector3 point = pos.add(random);
                spawnParticle(point);
            }
        }
    }

    public abstract void spawnParticle(Vector3 point);
}
