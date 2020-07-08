package binary404.mystictools.client.fx.type;

import binary404.mystictools.client.fx.FXBlock;
import binary404.mystictools.client.fx.data.BlockData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockType extends ParticleType<BlockData> {

    public BlockType() {
        super(false, BlockData.DESERIALIZER);
    }

    public static class Factory implements IParticleFactory<BlockData> {

        public Factory(IAnimatedSprite sprite) {
        }

        @Nullable
        @Override
        public Particle makeParticle(BlockData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FXBlock(worldIn, x, y, z, typeIn.depth, typeIn.maxAge, typeIn.block);
        }
    }

}
