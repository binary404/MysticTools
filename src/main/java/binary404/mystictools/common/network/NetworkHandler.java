package binary404.mystictools.common.network;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL = "6";
    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation("mystictools", "channel"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);

    public static void init() {
        int id = 0;
        HANDLER.registerMessage(id++, PacketToggle.class, PacketToggle::encode, PacketToggle::decode, PacketToggle::handle);
        HANDLER.registerMessage(id++, PacketSparkle.class, PacketSparkle::encode, PacketSparkle::decode, PacketSparkle::handle);
        HANDLER.registerMessage(id++, PacketFX.class, PacketFX::encode, PacketFX::decode, PacketFX::handle);
        HANDLER.registerMessage(id++, PacketJump.class, PacketJump::encode, PacketJump::decode, PacketJump::handle);
        HANDLER.registerMessage(id++, PacketArc.class, PacketArc::encode, PacketArc::decode, PacketArc::handle);
        HANDLER.registerMessage(id++, PacketRemoveSpirals.class, PacketRemoveSpirals::encode, PacketRemoveSpirals::decode, PacketRemoveSpirals::handle);
    }

    public static void sendToNearby(Level world, BlockPos pos, Object toSend) {
        if (world instanceof ServerLevel) {
            ServerLevel ws = (ServerLevel) world;

            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> HANDLER.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

    public static void sendToNearby(Level world, Entity e, Object toSend) {
        sendToNearby(world, new BlockPos(e.getX(), e.getY(), e.getZ()), toSend);
    }

    public static void sendTo(ServerPlayer playerMP, Object toSend) {
        HANDLER.sendTo(toSend, playerMP.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }

}
