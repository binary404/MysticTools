package binary404.mystictools.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketJump {

    public static void encode(PacketJump msg, PacketBuffer buf) {
    }

    public static PacketJump decode(PacketBuffer buf) {
        return new PacketJump();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();

                player.addExhaustion(0.3F);
                player.fallDistance = 0;
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
