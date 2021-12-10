package binary404.mystictools.common.network;

import binary404.mystictools.client.fx.FXHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketArc {

    double x, y, z;
    double tx, ty, tz;

    public PacketArc(double x, double y, double z, double tx, double ty, double tz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
    }

    public static void encode(PacketArc msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeDouble(msg.tx);
        buffer.writeDouble(msg.ty);
        buffer.writeDouble(msg.tz);
    }

    public static PacketArc decode(FriendlyByteBuf buffer) {
        return new PacketArc(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(PacketArc msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            FXHelper.arc(msg.x, msg.y, msg.z, msg.tx, msg.ty, msg.tz);
        });
        ctx.get().setPacketHandled(true);
    }

}
