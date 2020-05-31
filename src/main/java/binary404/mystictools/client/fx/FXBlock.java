package binary404.mystictools.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.TexturedParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class FXBlock extends TexturedParticle {

    boolean depthIgnoring;
    TextureAtlasSprite sprite;

    public FXBlock(World world, double x, double y, double z, boolean depthIgnore, int maxAge, Block block) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.depthIgnoring = depthIgnore;
        this.setMaxAge(maxAge);
        this.particleScale = 0.5F;
        this.sprite = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(block.getDefaultState()).getQuads(block.getDefaultState(), Direction.UP, new Random()).get(0).func_187508_a();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return depthIgnoring ? DI_RENDER : NORMAL_RENDER;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        }
    }

    @Override
    protected float getMinU() {
        return this.sprite.getMinU();
    }

    @Override
    protected float getMaxU() {
        return this.sprite.getMaxU();
    }

    @Override
    protected float getMinV() {
        return this.sprite.getMinV();
    }

    @Override
    protected float getMaxV() {
        return this.sprite.getMaxV();
    }

    public static void beginNormalRender(BufferBuilder bufferBuilder, TextureManager manager) {
        RenderSystem.depthMask(false);

        RenderSystem.disableBlend();
        RenderSystem.disableLighting();

        manager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    }

    private static final IParticleRenderType NORMAL_RENDER = new IParticleRenderType() {
        @Override
        public void beginRender(BufferBuilder p_217600_1_, TextureManager p_217600_2_) {
            beginNormalRender(p_217600_1_, p_217600_2_);
        }

        @Override
        public void finishRender(Tessellator p_217599_1_) {
            p_217599_1_.draw();
            RenderSystem.depthMask(true);
        }
    };

    private static final IParticleRenderType DI_RENDER = new IParticleRenderType() {
        @Override
        public void beginRender(BufferBuilder p_217600_1_, TextureManager p_217600_2_) {
            beginNormalRender(p_217600_1_, p_217600_2_);
            RenderSystem.disableDepthTest();
        }

        @Override
        public void finishRender(Tessellator p_217599_1_) {
            p_217599_1_.draw();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(true);
        }
    };
}
