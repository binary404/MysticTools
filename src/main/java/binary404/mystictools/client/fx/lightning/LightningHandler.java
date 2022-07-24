package binary404.mystictools.client.fx.lightning;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Deque;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LightningHandler {

    private LightningHandler() {
    }

    private static final int BATCH_THRESHOLD = 200;
    private static final ResourceLocation outsideResource = new ResourceLocation("mystictools", "textures/misc/wisp_large.png");
    private static final ResourceLocation insideResource = new ResourceLocation("mystictools", "textures/misc/wisp_small.png");
    public static final Deque<FXLightning> queuedLightningBolts = new ArrayDeque<>();

    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelLastEvent event) {
        PoseStack ms = event.getPoseStack();

        float frame = event.getPartialTick();
        Entity entity = Minecraft.getInstance().player;
        TextureManager render = Minecraft.getInstance().textureManager;

        double interpPosX = entity.xOld + (entity.getX() - entity.xOld) * frame;
        double interpPosY = entity.yOld + (entity.getY() - entity.yOld) * frame;
        double interpPosZ = entity.zOld + (entity.getZ() - entity.zOld) * frame;

        ms.pushPose();
        ms.translate(-interpPosX, -interpPosY, -interpPosZ);

        Tesselator tessellator = Tesselator.getInstance();

        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderSystem.setShaderTexture(0, outsideResource);
        int counter = 0;

        tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
        for (FXLightning bolt : queuedLightningBolts) {
            bolt.renderBolt(ms, tessellator.getBuilder(), 0, false);
            if (counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
                tessellator.end();
                tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
            }
            counter++;
        }
        tessellator.end();

        RenderSystem.setShaderTexture(0, insideResource);
        counter = 0;

        tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
        for (FXLightning bolt : queuedLightningBolts) {
            bolt.renderBolt(ms, tessellator.getBuilder(), 1, true);
            if (counter % BATCH_THRESHOLD == BATCH_THRESHOLD - 1) {
                tessellator.end();
                tessellator.getBuilder().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
            }
            counter++;
        }
        tessellator.end();

        queuedLightningBolts.clear();

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);

        ms.popPose();
    }

}
