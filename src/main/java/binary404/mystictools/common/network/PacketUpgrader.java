package binary404.mystictools.common.network;

import binary404.mystictools.common.tile.TileEntityUpgrader;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpgrader {

    double x, y, z;
    int flags;

    public PacketUpgrader(double x, double y, double z, int flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.flags = flags;
    }

    public static void encode(PacketUpgrader msg, PacketBuffer buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.flags);
    }

    public static PacketUpgrader decode(PacketBuffer buffer) {
        return new PacketUpgrader(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
    }

    public static void handle(PacketUpgrader packet, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            BlockPos pos = new BlockPos(packet.x, packet.y, packet.z);

            TileEntity tileEntity = ctx.get().getSender().world.getTileEntity(pos);

            if (tileEntity instanceof TileEntityUpgrader) {
                TileEntityUpgrader upgrader = (TileEntityUpgrader) tileEntity;

                switch (packet.flags) {
                    case 0:
                        upgrader.reroll();
                        break;
                    case 1:
                        upgrader.upgrade();
                        break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
