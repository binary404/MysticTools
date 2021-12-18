package binary404.mystictools.common.network;

import binary404.fx_lib.fx.effects.FXSourceOrbital;
import binary404.mystictools.common.loot.effects.unique.TreeChopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketRemoveSpirals {

    public PacketRemoveSpirals() {

    }

    public static void encode(PacketRemoveSpirals msg, FriendlyByteBuf buffer) {

    }

    public static PacketRemoveSpirals decode(FriendlyByteBuf buffer) {
        return new PacketRemoveSpirals();
    }

    public static void handle(PacketRemoveSpirals msg, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        ctx.get().enqueueWork(() -> {
            if(TreeChopper.orbitals.containsKey(Minecraft.getInstance().level.dimension())) {
                for(FXSourceOrbital orbital : TreeChopper.orbitals.get(Minecraft.getInstance().level.dimension())) {
                    orbital.requestRemoval();
                }
                TreeChopper.orbitals.get(Minecraft.getInstance().level.dimension()).clear();
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
