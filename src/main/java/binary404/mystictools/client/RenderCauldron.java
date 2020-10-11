package binary404.mystictools.client;

import binary404.mystictools.common.tile.TileEntityCauldron;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class RenderCauldron extends TileEntityRenderer<TileEntityCauldron> {

    public RenderCauldron(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityCauldron tileEntityIn, float partialTicks, MatrixStack ms, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ms.push();
        ms.translate(0.0, 0.75, 0.0);
        ms.push();
        float s = 1F / 180F * 10F;
        float v = 1F / 16F;
        float w = -v * 2.5F;
        ms.rotate(Vector3f.XP.rotationDegrees(90));
        ms.scale(s, s, s);
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getBlockModelShapes().getModel(Blocks.WATER.getDefaultState()).getParticleTexture();
        IVertexBuilder buffer = bufferIn.getBuffer(Atlases.getTranslucentCullBlockType());
        int red = ((0xa01fcf >> 16) & 0xFF);
        int green = ((0xa01fcf >> 8) & 0xFF);
        int blue = ((0xa01fcf & 0xFF));
        Matrix4f mat = ms.getLast().getMatrix();
        buffer.pos(mat, 0, 16, 0).color(red, green, blue, 0.7F * 255F).tex(sprite.getMinU(), sprite.getMaxV()).overlay(combinedOverlayIn).lightmap(combinedLightIn).normal(0, 0, 1).endVertex();
        buffer.pos(mat, 16, 16, 0).color(red, green, blue, 0.7F * 255F).tex(sprite.getMaxU(), sprite.getMaxV()).overlay(combinedOverlayIn).lightmap(combinedLightIn).normal(0, 0, 1).endVertex();
        buffer.pos(mat, 16, 0, 0).color(red, green, blue, 0.7F * 255F).tex(sprite.getMaxU(), sprite.getMinV()).overlay(combinedOverlayIn).lightmap(combinedLightIn).normal(0, 0, 1).endVertex();
        buffer.pos(mat, 0, 0, 0).color(red, green, blue, 0.7F * 255F).tex(sprite.getMinU(), sprite.getMinV()).overlay(combinedOverlayIn).lightmap(combinedLightIn).normal(0, 0, 1).endVertex();
        ms.pop();
        ms.pop();
    }


}
