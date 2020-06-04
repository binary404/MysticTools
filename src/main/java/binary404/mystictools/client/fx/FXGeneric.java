package binary404.mystictools.client.fx;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXGeneric extends Particle {

    boolean doneFrames = false;
    boolean flipped = false;
    double windX;
    double windZ;
    public boolean loop = false;
    float rotationSpeed = 0.0f;
    int startParticle = 0;
    int numParticles = 1;
    int particleInc = 1;
    float[] scaleKeys = new float[]{1.0f};
    float[] scaleFrames = new float[]{0.0f};
    float[] alphaKeys = new float[]{1.0f};
    float[] alphaFrames = new float[]{1.0f};
    double slowDown = 0.9800000190734863d;
    float randomX;
    float randomY;
    float randomZ;
    int[] finalFrames = null;
    boolean angled = false;
    float angleYaw;
    float anglePitch;
    int gridSize = 64;

    public static final ResourceLocation particles = new ResourceLocation("mystictools:textures/misc/particles.png");

    private int particleTextureIndexX;
    private int particleTextureIndexY;
    private float particleScale;

    public FXGeneric(World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0, 0);
    }

    public FXGeneric(World world, double x, double y, double z, double mX, double mY, double mZ) {
        super(world, x, y, z, mX, mY, mZ);
        setSize(0.1F, 0.1F);
        this.setPosition(x, y, z);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        this.particleAlpha = 1.0f;
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
    }

    @Override
    public void renderParticle(IVertexBuilder bufferBuilder, ActiveRenderInfo activeRenderInfo, float v) {
        if (this.loop) {
            setParticleTextureIndex(this.startParticle + this.age / this.particleInc % this.numParticles);
        } else {
            float fs = this.age / this.maxAge;
            setParticleTextureIndex((int) (this.startParticle + Math.min(this.numParticles * fs, (this.numParticles - 1))));
        }
        if (this.finalFrames != null && this.finalFrames.length > 0 && this.age > this.maxAge - this.finalFrames.length) {
            int frame = this.maxAge - this.age;
            if (frame < 0)
                frame = 0;
            setParticleTextureIndex(this.finalFrames[frame]);
        }
        this.particleAlpha = (this.alphaFrames.length <= 0) ? 0.0F : this.alphaFrames[Math.min(this.age, this.alphaFrames.length - 1)];
        this.particleScale = (this.scaleFrames.length <= 0) ? 0.0F : this.scaleFrames[Math.min(this.age, this.scaleFrames.length - 1)];
        draw(bufferBuilder, activeRenderInfo, v);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return NORMAL_RENDER;
    }

    public void draw(IVertexBuilder wr, ActiveRenderInfo renderInfo, float partialTicks) {
        float tx1 = (float) this.particleTextureIndexX / (float) this.gridSize;
        float tx2 = tx1 + 1.0F / (float) this.gridSize;
        float ty1 = (float) this.particleTextureIndexY / (float) this.gridSize;
        float ty2 = ty1 + 1.0F / (float) this.gridSize;
        float ts = 0.1F * this.particleScale;

        float fs;
        if (this.flipped) {
            fs = tx1;
            tx1 = tx2;
            tx2 = fs;
        }

        fs = MathHelper.clamp(((float) this.age + partialTicks) / (float) this.maxAge, 0.0F, 1.0F);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & '\uffff';
        int k = i & '\uffff';
        Vec3d interpPos = renderInfo.getProjectedView();
        float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPos.x);
        float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPos.y);
        float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPos.z);
        if (this.angled) {
            RenderSystem.translated(f5, f6, f7);
            RenderSystem.rotatef(-this.angleYaw + 90.0F, 0.0F, 1.0F, 0.0F);
            RenderSystem.rotatef(this.anglePitch + 90.0F, 1.0F, 0.0F, 0.0F);
            if (this.particleAngle != 0.0F) {
                float f8 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
                RenderSystem.rotatef(f8 * 57.29577951308232F, 0.0F, 0.0F, 1.0F);
            }

            wr.pos((double) (-ts), (double) (-ts), 0.0D).tex(tx2, ty2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            wr.pos((double) (-ts), (double) ts, 0.0D).tex(tx2, ty1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            wr.pos((double) ts, (double) ts, 0.0D).tex(tx1, ty1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            wr.pos((double) ts, (double) (-ts), 0.0D).tex(tx1, ty2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        } else {
            Quaternion quaternion = new Quaternion(renderInfo.getRotation());
            float f3 = MathHelper.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
            quaternion.multiply(Vector3f.ZP.rotation(f3));

            Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
            vector3f1.transform(quaternion);
            Vector3f[] avec3d = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            float f4 = this.particleScale;

            for (int x = 0; x < 4; ++x) {
                Vector3f vector3f = avec3d[x];
                vector3f.transform(quaternion);
                vector3f.mul(f4);
                vector3f.add(f5, f6, f7);
            }

            wr.pos((double) f5 + avec3d[0].getX(), (double) f6 + avec3d[0].getY(), (double) f7 + avec3d[0].getZ()).tex(tx2, ty2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            wr.pos((double) f5 + avec3d[1].getX(), (double) f6 + avec3d[1].getY(), (double) f7 + avec3d[1].getZ()).tex(tx2, ty1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            wr.pos((double) f5 + avec3d[2].getX(), (double) f6 + avec3d[2].getY(), (double) f7 + avec3d[2].getZ()).tex(tx1, ty1).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
            wr.pos((double) f5 + avec3d[3].getX(), (double) f6 + avec3d[3].getY(), (double) f7 + avec3d[3].getZ()).tex(tx1, ty2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        }
    }

    @Override
    public void tick() {
        if (!this.doneFrames)
            calculateFrames();

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge)
            setExpired();

        this.prevParticleAngle = this.particleAngle;
        this.particleAngle += 3.1415927F * this.rotationSpeed * 2.0F;
        this.motionY -= 0.04D * this.particleGravity;
        move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= this.slowDown;
        this.motionY *= this.slowDown;
        this.motionZ *= this.slowDown;
        this.motionX += this.world.rand.nextGaussian() * this.randomX;
        this.motionY += this.world.rand.nextGaussian() * this.randomY;
        this.motionZ += this.world.rand.nextGaussian() * this.randomZ;
        this.motionX += this.windX;
        this.motionZ += this.windZ;
        if (this.onGround && this.slowDown != 1.0D) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    void calculateFrames() {
        this.doneFrames = true;
        if (this.alphaKeys == null)
            setAlphaF(1.0F);

        this.alphaFrames = new float[this.maxAge + 1];
        float inc = (this.alphaKeys.length - 1) / (float) this.maxAge;
        float is = 0.0F;

        for (int a = 0; a <= this.maxAge; a++) {
            int isF = MathHelper.floor(is);
            float diff = (isF < this.alphaKeys.length - 1) ? (diff = this.alphaKeys[isF + 1] - this.alphaKeys[isF]) : 0.0F;
            float pa = is - isF;
            this.alphaFrames[a] = this.alphaKeys[isF] + diff * pa;
            is += inc;
        }
        if (this.scaleKeys == null)
            setScale(1.0F);

        this.scaleFrames = new float[this.maxAge + 1];
        inc = (this.scaleKeys.length - 1) / (float) this.maxAge;
        is = 0.0F;
        for (int a = 0; a <= this.maxAge; a++) {
            int isF = MathHelper.floor(is);
            float diff = (isF < this.scaleKeys.length - 1) ? (diff = this.scaleKeys[isF + 1] - this.scaleKeys[isF]) : 0.0F;
            float pa = is - isF;
            this.scaleFrames[a] = this.scaleKeys[isF] + diff * pa;
            is += inc;
        }
    }

    public boolean isFlipped() {
        return this.flipped;
    }

    public void setFlipped(boolean flip) {
        this.flipped = flip;
    }

    public void setSlowDown(double slowDown) {
        this.slowDown = slowDown;
    }

    public void setGravity(float g) {
        this.particleGravity = g;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public void setParticles(int startParticle, int numParticles, int particleInc) {
        this.numParticles = numParticles;
        this.particleInc = particleInc;
        this.startParticle = startParticle;
        setParticleTextureIndex(startParticle);
    }

    public void setParticle(int startParticle) {
        this.numParticles = 1;
        this.particleInc = 1;
        this.startParticle = startParticle;
        setParticleTextureIndex(startParticle);
    }

    public void setScale(float... scale) {
        this.particleScale = scale[0];
        this.scaleKeys = scale;
    }

    public void setAlphaFA(float... a1) {
        super.setAlphaF(a1[0]);
        this.alphaKeys = a1;
    }

    @Override
    protected void setAlphaF(float p_82338_1_) {
        setAlphaFA(p_82338_1_);
    }

    public void setRotationSpeed(float rot) {
        this.rotationSpeed = (float) (rot * 0.017453292519943D);
    }

    public void setRotationSpeed(float start, float rot) {
        this.particleAngle = (float) (start * Math.PI * 2.0D);
        this.rotationSpeed = (float) (rot * 0.017453292519943D);
    }

    public void setParticleTextureIndex(int index) {
        if (index < 0) index = 0;
        this.particleTextureIndexX = index % this.gridSize;
        this.particleTextureIndexY = index / this.gridSize;
    }

    private static final IParticleRenderType NORMAL_RENDER = new IParticleRenderType() {
        @Override
        public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(770, 771);
            RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
            RenderSystem.disableLighting();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            textureManager.bindTexture(particles);
            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        }

        @Override
        public void finishRender(Tessellator tessellator) {
            tessellator.draw();
            RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        @Override
        public String toString() {
            return "mystictools:generic";
        }
    };
}
