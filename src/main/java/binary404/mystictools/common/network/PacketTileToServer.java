package binary404.mystictools.common.network;

import binary404.mystictools.common.core.util.Utils;
import binary404.mystictools.common.tile.TileMod;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTileToServer {

    private long pos;
    private CompoundNBT nbt;

    public PacketTileToServer(BlockPos pos, CompoundNBT nbt) {
        this.pos = pos.toLong();
        this.nbt = nbt;
    }

    public PacketTileToServer(long pos, CompoundNBT nbt) {
        this.pos = pos;
        this.nbt = nbt;
    }

    public static void encode(PacketTileToServer packet, PacketBuffer buffer) {
        buffer.writeLong(packet.pos);
        Utils.writeCompoundNBTToBuffer(buffer, packet.nbt);
    }

    public static PacketTileToServer decode(PacketBuffer buffer) {
        return new PacketTileToServer(buffer.readLong(), Utils.readCompoundNBTFromBuffer(buffer));
    }

    public static void handle(PacketTileToServer message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = ctx.get().getSender().world;
            BlockPos bp = BlockPos.fromLong(message.pos);
            if (world != null && bp != null) {
                TileEntity te = world.getTileEntity(bp);
                if (te != null && te instanceof TileMod)
                    ((TileMod) te).messageFromClient((message.nbt == null) ? new CompoundNBT() : message.nbt, ctx.get().getSender());
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
