package binary404.mystictools.client.fx.lightning;

import binary404.mystictools.common.core.helper.util.Vector3;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import java.util.List;

public class FXLightning extends Particle {

    private static final int fadetime = 20;
    private final int expandTime;
    private final int colorOuter;
    private final int colorInner;

    private final List<FXLightningSegment> segments;
    private final int segmentCount;

    public FXLightning(ClientLevel world, double x, double y, double z, double tx, double ty, double tz, float speed, long seed, int colorOuter, int colorInner) {
        this(world, new Vector3(x, y, z), new Vector3(tx, ty, tz), speed, seed, colorOuter, colorInner);
    }

    public FXLightning(ClientLevel world, Vector3 sourcevec, Vector3 targetvec, float speed, long seed, int colorOuter, int colorInner) {
        super(world, sourcevec.x, sourcevec.y, sourcevec.z);
        this.colorOuter = colorOuter;
        this.colorInner = colorInner;
        double length = targetvec.subtract(sourcevec).mag();
        lifetime = fadetime + world.random.nextInt(fadetime) - fadetime / 2;
        expandTime = (int) (length * speed);
        age = -(int) (length * speed);

        LightningSegmentGenerator gen = new LightningSegmentGenerator(seed);
        Pair<Integer, List<FXLightningSegment>> res = gen.compute(sourcevec, targetvec, length);
        segmentCount = res.getFirst();
        segments = res.getSecond();
    }

    @Override
    public void render(VertexConsumer buffer, Camera info, float partialTicks) {
        LightningHandler.queuedLightningBolts.offer(this);
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return LAYER;
    }

    public void renderBolt(PoseStack ms, VertexConsumer wr, int pass, boolean inner) {
        Matrix4f mat = ms.last().pose();

        float boltAge = age < 0 ? 0 : (float) age / (float) lifetime;
        float mainAlpha;
        if (pass == 0) {
            mainAlpha = (1 - boltAge) * 0.4F;
        } else {
            mainAlpha = 1 - boltAge * 0.5F;
        }

        int renderstart = (int) ((expandTime / 2 - lifetime + age) / (float) (expandTime / 2) * segmentCount);
        int renderend = (int) ((age + expandTime) / (float) expandTime * segmentCount);

        for (FXLightningSegment rendersegment : segments) {
            if (rendersegment.segmentNo < renderstart || rendersegment.segmentNo > renderend) {
                continue;
            }

            Vector3 playerVec = getRelativeViewVector(rendersegment.startPoint.point).multiply(-1);

            double width = 0.015F * (playerVec.mag() / 5 + 1) * (1 + rendersegment.light) * 0.5F;

            Vector3 diff1 = playerVec.crossProduct(rendersegment.prevDiff).normalize().multiply(width / rendersegment.sinPrev);
            Vector3 diff2 = playerVec.crossProduct(rendersegment.nextDiff).normalize().multiply(width / rendersegment.sinNext);

            Vector3 startvec = rendersegment.startPoint.point;
            Vector3 endvec = rendersegment.endPoint.point;

            int color = inner ? colorInner : colorOuter;
            int r = (color & 0xFF0000) >> 16;
            int g = (color & 0xFF00) >> 8;
            int b = color & 0xFF;
            int a = (int) (mainAlpha * rendersegment.light * 0xFF);
            int fullbright = 0xF000F0;

            endvec.subtract(diff2).vertex(mat, wr);
            wr.color(r, g, b, a).uv(0.5F, 0).uv2(fullbright).endVertex();
            startvec.subtract(diff1).vertex(mat, wr);
            wr.color(r, g, b, a).uv(0.5F, 0).uv2(fullbright).endVertex();
            startvec.add(diff1).vertex(mat, wr);
            wr.color(r, g, b, a).uv(0.5F, 1).uv2(fullbright).endVertex();
            endvec.add(diff2).vertex(mat, wr);
            wr.color(r, g, b, a).uv(0.5F, 1).uv2(fullbright).endVertex();

            if (rendersegment.next == null) {
                Vector3 roundend = rendersegment.endPoint.point.add(rendersegment.diff.normalize().multiply(width));

                roundend.subtract(diff2).vertex(mat, wr);
                wr.color(r, g, b, a).uv(0, 0).uv2(fullbright).endVertex();
                endvec.subtract(diff2).vertex(mat, wr);
                wr.color(r, g, b, a).uv(0.5F, 0).uv2(fullbright).endVertex();
                endvec.add(diff2).vertex(mat, wr);
                wr.color(r, g, b, a).uv(0.5F, 1).uv2(fullbright).endVertex();
                roundend.add(diff2).vertex(mat, wr);
                wr.color(r, g, b, a).uv(0, 1).uv2(fullbright).endVertex();
            }

            if (rendersegment.prev == null) {
                Vector3 roundend = rendersegment.startPoint.point.subtract(rendersegment.diff.normalize().multiply(width));

                startvec.subtract(diff1).vertex(mat, wr);
                wr.color(r, g, b, a).uv(0.5F, 0).uv2(fullbright).endVertex();
                roundend.subtract(diff1).vertex(mat, wr);
                wr.color(r, g, b, a).uv(0, 0).uv2(fullbright).endVertex();
                roundend.add(diff1).vertex(mat, wr);
                wr.color(r, g, b, a).uv(0, 1).uv2(fullbright).endVertex();
                startvec.add(diff1).vertex(mat, wr);
                wr.color(r, g, b, a).uv(0.5F, 1).uv2(fullbright).endVertex();
            }
        }
    }

    private static Vector3 getRelativeViewVector(Vector3 pos) {
        Entity renderEntity = Minecraft.getInstance().getCameraEntity();
        return new Vector3((float) renderEntity.getX() - pos.x, (float) renderEntity.getY() - pos.y, (float) renderEntity.getZ() - pos.z);
    }

    private static final ParticleRenderType LAYER = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableTexture();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
        }

        @Override
        public void end(Tesselator tess) {
            tess.end();
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    };

}
