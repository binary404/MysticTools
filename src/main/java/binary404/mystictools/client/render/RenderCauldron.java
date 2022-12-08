package binary404.mystictools.client.render;


import binary404.mystictools.common.tile.CauldronBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.Blocks;

public class RenderCauldron implements BlockEntityRenderer<CauldronBlockEntity> {

    public RenderCauldron(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(CauldronBlockEntity tileEntityIn, float partialTicks, PoseStack ms, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ms.pushPose();
        ms.translate(0.0, 0.75, 0.0);
        ms.pushPose();
        float s = 1F / 180F * 10F;
        float v = 1F / 16F;
        float w = -v * 2.5F;
        ms.mulPose(Vector3f.XP.rotationDegrees(90));
        ms.scale(s, s, s);
        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(Blocks.WATER.defaultBlockState()).getParticleIcon();
        VertexConsumer buffer = bufferIn.getBuffer(Sheets.translucentCullBlockSheet());
        int red = ((0xa01fcf >> 16) & 0xFF);
        int green = ((0xa01fcf >> 8) & 0xFF);
        int blue = ((0xa01fcf & 0xFF));
        Matrix4f mat = ms.last().pose();
        buffer.vertex(mat, 0, 16, 0).color(red, green, blue, 0.7F * 255F).uv(sprite.getU0(), sprite.getV1()).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 0, 1).endVertex();
        buffer.vertex(mat, 16, 16, 0).color(red, green, blue, 0.7F * 255F).uv(sprite.getU1(), sprite.getV1()).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 0, 1).endVertex();
        buffer.vertex(mat, 16, 0, 0).color(red, green, blue, 0.7F * 255F).uv(sprite.getU1(), sprite.getV0()).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 0, 1).endVertex();
        buffer.vertex(mat, 0, 0, 0).color(red, green, blue, 0.7F * 255F).uv(sprite.getU0(), sprite.getV0()).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 0, 1).endVertex();
        ms.popPose();
        ms.popPose();
    }


}
