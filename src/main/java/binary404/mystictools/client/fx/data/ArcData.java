package binary404.mystictools.client.fx.data;

import binary404.mystictools.client.fx.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

import java.util.Locale;

public class ArcData implements IParticleData {

    public float r, g, b;
    public double hg, tx, ty, tz;


    public ArcData(double tx, double ty, double tz, float r, float g, float b, double hg) {
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        this.r = r;
        this.g = g;
        this.b = b;
        this.hg = hg;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeFloat(r);
        buffer.writeFloat(g);
        buffer.writeFloat(b);
        buffer.writeDouble(hg);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.ARC;
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%.2f %.2f %.2f %d", this.getType().getRegistryName(), this.r, this.g, this.b, this.hg);
    }

    public static final IDeserializer<ArcData> DESERIALIZER = new IDeserializer<ArcData>() {
        @Override
        public ArcData deserialize(ParticleType<ArcData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double tx = reader.readDouble();
            reader.expect(' ');
            double ty = reader.readDouble();
            reader.expect(' ');
            double tz = reader.readDouble();
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            double hg = reader.readDouble();

            return new ArcData(tx, ty, tz, r, g, b, hg);
        }

        @Override
        public ArcData read(ParticleType<ArcData> particleTypeIn, PacketBuffer buffer) {
            return new ArcData(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readDouble());
        }
    };
}
