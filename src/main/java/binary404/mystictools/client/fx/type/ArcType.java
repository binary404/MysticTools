package binary404.mystictools.client.fx.type;

import binary404.mystictools.client.fx.FXArc;
import binary404.mystictools.client.fx.data.ArcData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ArcType extends ParticleType<ArcData> {

    public ArcType() {
        super(false, ArcData.DESERIALIZER);
    }

    public static class Factory implements IParticleFactory<ArcData> {

        public Factory(IAnimatedSprite sprite) {
        }

        @Nullable
        @Override
        public Particle makeParticle(ArcData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FXArc(worldIn, x, y, z, typeIn.tx, typeIn.ty, typeIn.tz, typeIn.r, typeIn.g, typeIn.b, typeIn.hg);
        }
    }

}
