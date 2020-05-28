package binary404.mystictools.client.fx;

import binary404.mystictools.common.core.Utils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class FXArc extends Particle {

    public int particle;
    ArrayList<Vec3d> points;
    private double tX;
    private double tY;
    private double tZ;
    public static ResourceLocation texture = new ResourceLocation("mystictools", "textures/misc/beamh.png");
    public int blendmode;
    public float length;

    public FXArc(World world, double x, double y, double z, double tx, double ty, double tz, float red, float green, float blue, double hg) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particle = 16;
        this.points = new ArrayList<Vec3d>();
        this.blendmode = 1;
        this.length = 1.0F;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.setSize(0.02F, 0.02F);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.tX = tx - x;
        this.tY = ty - y;
        this.tZ = tz - z;
        this.maxAge = 3;
        double xx = 0.0;
        double yy = 0.0;
        double zz = 0.0;
        double gravity = 0.115;
        double noise = 0.25;
        Vec3d vs = new Vec3d(xx, yy, zz);
        Vec3d ve = new Vec3d(this.tX, this.tY, this.tZ);
        Vec3d vc = new Vec3d(xx, yy, zz);
        this.length = (float) ve.length();
        Vec3d vv = Utils.calculateVelocity(vs, ve, hg, gravity);
        double l = Utils.distanceSquared3d(new Vec3d(0.0, 0.0, 0.0), vv);
        this.points.add(vs);
        for (int c = 0; Utils.distanceSquared3d(ve, vc) > l && c < 50; ++c) {
            Vec3d vt = vc.add(vv.x, vv.y, vv.z);
            vc = new Vec3d(vt.x, vt.y, vt.z);
            vt = vt.add((this.rand.nextDouble() - this.rand.nextDouble()) * noise, (this.rand.nextDouble() - this.rand.nextDouble()) * noise, (this.rand.nextDouble() - this.rand.nextDouble()) * noise);
            this.points.add(vt);
            vv = vv.subtract(0.0, gravity / 1.9, 0.0);
        }
        this.points.add(ve);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return NORMAL_RENDER;
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
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        Vec3d vec3d = renderInfo.getProjectedView();
        double ePX = this.prevPosX + (this.posX - this.prevPosX) * partialTicks - vec3d.x;
        double ePY = this.prevPosY + (this.posY - this.prevPosY) * partialTicks - vec3d.y;
        double ePZ = this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - vec3d.z;
        RenderSystem.translated(ePX, ePY, ePZ);
        float size = 0.125f;
        int i = 220;
        int j = i >> 16 & 0xFFFF;
        int k = i & 0xFFFF;
        float alpha = 1.0f - (this.age + partialTicks) / this.maxAge;
        float f6 = 0.0f;
        float f7 = 1.0f;

        for (int c = 0; c < this.points.size(); ++c) {
            Vec3d v = this.points.get(c);
            float f8 = c / this.length;
            double dx = v.x;
            double dy = v.y;
            double dz = v.z;
            buffer.pos(dx, dy - size, dz).tex(f8, f7).lightmap(j, k).color(this.particleRed, this.particleGreen, this.particleBlue, alpha).endVertex();
            buffer.pos(dx, dy + size, dz).tex(f8, f6).lightmap(j, k).color(this.particleRed, this.particleGreen, this.particleBlue, alpha).endVertex();
        }
        Tessellator.getInstance().draw();
        Tessellator.getInstance().getBuffer().begin(5, DefaultVertexFormats.POSITION_TEX_LIGHTMAP_COLOR);
        for (int c = 0; c < this.points.size(); ++c) {
            Vec3d v = this.points.get(c);
            float f8 = c / this.length;
            double dx = v.x;
            double dy = v.z;
            double dz = v.z;
            buffer.pos(dx - size, dy, dz - size).tex(f8, f7).lightmap(j, k).color(this.particleRed, this.particleGreen, this.particleBlue, alpha).endVertex();
            buffer.pos(dx + size, dy, dz + size).tex(f8, f6).lightmap(j, k).color(this.particleRed, this.particleGreen, this.particleBlue, alpha).endVertex();
        }
    }

    private static final IParticleRenderType NORMAL_RENDER = new IParticleRenderType() {
        @Override
        public void beginRender(BufferBuilder p_217600_1_, TextureManager p_217600_2_) {
            p_217600_2_.bindTexture(texture);
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(770, 1);
            RenderSystem.disableLighting();
            p_217600_1_.begin(5, DefaultVertexFormats.POSITION_TEX_LIGHTMAP_COLOR);
        }

        @Override
        public void finishRender(Tessellator p_217599_1_) {
            p_217599_1_.draw();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    };

}
