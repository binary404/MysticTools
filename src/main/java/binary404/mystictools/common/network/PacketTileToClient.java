package binary404.mystictools.common.network;

import binary404.mystictools.common.core.util.Utils;
import binary404.mystictools.common.tile.TileMod;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTileToClient {

    private long pos;
    private CompoundNBT nbt;

    public PacketTileToClient(BlockPos pos, CompoundNBT nbt) {
        this.pos = pos.toLong();
        this.nbt = nbt;
    }

    public PacketTileToClient(long pos, CompoundNBT nbt) {
        this.pos = pos;
        this.nbt = nbt;
    }

    public static void encode(PacketTileToClient packet, PacketBuffer buffer) {
        buffer.writeLong(packet.pos);
        Utils.writeCompoundNBTToBuffer(buffer, packet.nbt);
    }

    public static PacketTileToClient decode(PacketBuffer buffer) {
        return new PacketTileToClient(buffer.readLong(), Utils.readCompoundNBTFromBuffer(buffer));
    }

    public static void handle(PacketTileToClient message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            World world = Minecraft.getInstance().world;
            BlockPos bp = BlockPos.fromLong(message.pos);
            if (world != null && bp != null) {
                TileEntity te = world.getTileEntity(bp);
                if (te != null && te instanceof TileMod)
                    ((TileMod) te).messageFromServer((message.nbt == null) ? new CompoundNBT() : message.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
