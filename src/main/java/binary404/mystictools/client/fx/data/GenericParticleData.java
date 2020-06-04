package binary404.mystictools.client.fx.data;

import binary404.mystictools.client.fx.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import javax.annotation.Nonnull;
import java.util.Locale;

public class GenericParticleData implements IParticleData {

    public final float size;
    public final float r, g, b;
    public final int gridSize;
    public final int maxAge;
    public final int startPart;
    public final int numPart;
    public final int partInc;
    public final boolean loop;
    public float[] alpha;
    public float rotation;
    public float rot2;

    public GenericParticleData(float size, float r, float g, float b, int gridSize, int maxAge, int startPart, int numPart, int partInc, boolean loop) {
        this.size = size;
        this.r = r;
        this.g = g;
        this.b = b;
        this.gridSize = gridSize;
        this.maxAge = maxAge;
        this.startPart = startPart;
        this.numPart = numPart;
        this.partInc = partInc;
        this.loop = loop;
        alpha = new float[]{1.0f};
    }

    public void setAlpha(float... alpha) {
        this.alpha = alpha;
    }

    public void setRotation(float rotation, float rot2) {
        this.rotation = rotation;
        this.rot2 = rot2;
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.GENERIC;
    }

    @Override
    public void write(PacketBuffer buf) {
        buf.writeFloat(size);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeInt(gridSize);
        buf.writeInt(maxAge);
        buf.writeInt(startPart);
        buf.writeInt(numPart);
        buf.writeInt(partInc);
        buf.writeInt(alpha.length);
        for (int i = 0; i < alpha.length; i++) {
            buf.writeFloat(alpha[i]);
        }
    }

    @Nonnull
    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %d %d %d %d, %b",
                this.getType().getRegistryName(), this.size, this.r, this.g, this.b, this.gridSize, this.maxAge, this.startPart, this.numPart, this.partInc, this.loop);
    }

    public static final IDeserializer<GenericParticleData> DESERIALIZER = new IDeserializer<GenericParticleData>() {
        @Nonnull
        @Override
        public GenericParticleData deserialize(@Nonnull ParticleType<GenericParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float size = reader.readFloat();
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            int grid = reader.readInt();
            reader.expect(' ');
            int age = reader.readInt();
            reader.expect(' ');
            int start = reader.readInt();
            reader.expect(' ');
            int num = reader.readInt();
            reader.expect(' ');
            int part = reader.readInt();
            reader.expect(' ');
            boolean loop = reader.readBoolean();

            return new GenericParticleData(size, r, g, b, grid, age, start, num, part, loop);
        }

        @Override
        public GenericParticleData read(@Nonnull ParticleType<GenericParticleData> type, PacketBuffer buf) {
            GenericParticleData data = new GenericParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean());
            float[] alpha = new float[buf.readInt()];
            for (int i = 0; i < alpha.length; i++) {
                alpha[i] = buf.readFloat();
            }
            data.setAlpha(alpha);
            return data;
        }
    };

}
