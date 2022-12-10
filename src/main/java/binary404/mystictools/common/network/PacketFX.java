package binary404.mystictools.common.network;

import binary404.mystictools.MysticTools;
import binary404.mystictools.client.fx.FXHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFX {

    int id;
    double x, y, z;

    public PacketFX(double x, double y, double z, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public static void encode(PacketFX msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.id);
    }

    public static PacketFX decode(FriendlyByteBuf buffer) {
        return new PacketFX(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
    }

    public static void handle(PacketFX msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            switch (msg.id) {
                case 0: {
                    int count = 50;
                    Level world = MysticTools.proxy.getWorld();
                    for (int a = 0; a < 100; a++) {
                        boolean floaty = world.random.nextFloat() < 0.8f;
                        float r = Mth.nextInt(world.random, 255, 255) / 255.0F;
                        float g = Mth.nextInt(world.random, 64, 255) / 255.0F;
                        float b = Mth.nextInt(world.random, 189, 255) / 255.0F;
                        float r2 = Mth.nextInt(world.random, 255, 255) / 255.0F;
                        float g2 = Mth.nextInt(world.random, 64, 255) / 255.0F;
                        float b2 = Mth.nextInt(world.random, 189, 255) / 255.0F;
                        FXHelper.sparkle(msg.x + world.random.nextGaussian() * 0.5, msg.y + world.random.nextGaussian() * 0.5, msg.z + world.random.nextGaussian() * 0.5, world.random.nextGaussian() * 0.04, world.random.nextGaussian() * 0.04 + (floaty ? 0.05D : 0.15D), world.random.nextGaussian() * 0.04, r, g, b, r2, g2, b2, 0.04F, floaty ? 0.04F : 0.6F, floaty ? 100 : 30);
                    }
                    break;
                }
                case 1: {
                    double startRadius = 1.0;
                    double endRadius = 5.0;
                    double radiusIncrease = 0.5;
                    for (double i = startRadius; i <= endRadius; i += radiusIncrease) {
                        final double radius = i;
                        MysticTools.proxy.scheduleDelayed(() -> FXHelper.shockwave(msg.x, msg.y, msg.z, radius), (int) (i * 5));
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
