package binary404.mystictools.client.fx.type;

import binary404.mystictools.client.fx.FXGeneric;
import binary404.mystictools.client.fx.data.GenericParticleData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GenericParticleType extends ParticleType<GenericParticleData> {

    public GenericParticleType() {
        super(false, GenericParticleData.DESERIALIZER);
    }

    public static class Factory implements IParticleFactory<GenericParticleData> {

        public Factory(IAnimatedSprite sprite) {

        }

        @Nullable
        @Override
        public Particle makeParticle(GenericParticleData genericParticleData, World world, double v, double v1, double v2, double v3, double v4, double v5) {
            FXGeneric particle = new FXGeneric(world, v, v1, v2, v3, v4, v5);
            particle.setColor(genericParticleData.r, genericParticleData.g, genericParticleData.b);
            particle.setScale(genericParticleData.size);
            particle.setMaxAge(genericParticleData.maxAge);
            particle.setGridSize(genericParticleData.gridSize);
            particle.setParticles(genericParticleData.startPart, genericParticleData.numPart, genericParticleData.partInc);
            particle.setAlphaFA(genericParticleData.alpha);
            particle.setRotationSpeed(genericParticleData.rotation, genericParticleData.rot2);
            particle.loop = genericParticleData.loop;
            return particle;
        }
    }

}
