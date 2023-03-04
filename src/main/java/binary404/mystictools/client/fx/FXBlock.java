package binary404.mystictools.client.fx;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class FXBlock extends TextureSheetParticle {
    boolean depthIgnoring;
    Block block;

    public FXBlock(ClientLevel world, double x, double y, double z, boolean depthIgnore, int maxAge, Block block) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.depthIgnoring = depthIgnore;
        this.setLifetime(maxAge);
        this.quadSize = 0.6F;
        this.block = block;
        this.sprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(block.defaultBlockState()).getParticleIcon();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return DI_RENDER;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
        if (this.level.getBlockState(new BlockPos(this.x, this.y, this.z)).getBlock() != this.block) {
            this.remove();
        }
    }

    public static void beginNormalRender(BufferBuilder bufferBuilder, TextureManager manager) {
        RenderSystem.depthMask(false);
        RenderSystem.disableBlend();

        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15 << 20;
    }

    private static final ParticleRenderType NORMAL_RENDER = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder p_217600_1_, TextureManager p_217600_2_) {
            beginNormalRender(p_217600_1_, p_217600_2_);
        }

        @Override
        public void end(Tesselator p_217599_1_) {
            p_217599_1_.end();
            RenderSystem.depthMask(true);
        }
    };

    private static final ParticleRenderType DI_RENDER = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder p_217600_1_, TextureManager p_217600_2_) {
            beginNormalRender(p_217600_1_, p_217600_2_);
            RenderSystem.disableDepthTest();
        }

        @Override
        public void end(Tesselator p_217599_1_) {
            p_217599_1_.end();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    };
}
