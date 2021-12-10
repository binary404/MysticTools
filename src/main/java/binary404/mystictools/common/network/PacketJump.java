package binary404.mystictools.common.network;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketJump {

    public static void encode(PacketJump msg, FriendlyByteBuf buf) {
    }

    public static PacketJump decode(FriendlyByteBuf buf) {
        return new PacketJump();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();

                player.causeFoodExhaustion(0.3F);
                player.fallDistance = 0;
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
