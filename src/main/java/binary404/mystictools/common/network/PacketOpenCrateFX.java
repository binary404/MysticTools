package binary404.mystictools.common.network;

import binary404.mystictools.MysticTools;
import binary404.mystictools.client.fx.FXHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketOpenCrateFX {

    double x, y, z;

    public PacketOpenCrateFX(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(PacketOpenCrateFX msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
    }

    public static PacketOpenCrateFX decode(PacketBuffer buffer) {
        return new PacketOpenCrateFX(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void handle(PacketOpenCrateFX msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            int count = 50;
            World world = MysticTools.proxy.getWorld();
            for (int a = 0; a < 50; a++) {
                boolean floaty = (a < count / 3);
                float r = MathHelper.nextInt(world.rand, 255, 255) / 255.0F;
                float g = MathHelper.nextInt(world.rand, 64, 255) / 255.0F;
                float b = MathHelper.nextInt(world.rand, 189, 255) / 255.0F;
                FXHelper.sparkle(msg.x + world.rand.nextGaussian() * 0.4, msg.y + world.rand.nextGaussian() * 0.4, msg.z + world.rand.nextGaussian() * 0.4, world.rand.nextGaussian() * 0.06, world.rand.nextGaussian() * 0.06 + (floaty ? 0.05D : 0.15D), world.rand.nextGaussian() * 0.06, r, g, b, 0.15F, floaty ? 0.04F : 0.8F, 16);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
