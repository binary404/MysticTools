package binary404.mystictools.common.network;

import binary404.mystictools.client.fx.FXHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class PacketSparkle {

    double x, y, z;
    float r, g, b;

    public PacketSparkle(double x, double y, double z, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static void encode(PacketSparkle msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeFloat(msg.r);
        buffer.writeFloat(msg.g);
        buffer.writeFloat(msg.b);
    }

    public static PacketSparkle decode(FriendlyByteBuf buffer) {
        return new PacketSparkle(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    public static void handle(PacketSparkle msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            Random rand = new Random();
            for (int i = 0; i < 10; i++)
                FXHelper.sparkle(msg.x + rand.nextGaussian() * 0.4, msg.y + rand.nextGaussian() * 0.4, msg.z + rand.nextGaussian() * 0.4, rand.nextGaussian() * 0.08, rand.nextGaussian() * 0.08, rand.nextGaussian() * 0.08, msg.r, msg.g, msg.b, msg.r, msg.g, msg.b, 0.15F, 0.08F, 16);
        });
        ctx.get().setPacketHandled(true);
    }

}
