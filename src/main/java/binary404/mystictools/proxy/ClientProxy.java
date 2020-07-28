package binary404.mystictools.proxy;

import binary404.mystictools.client.RenderCauldron;
import binary404.mystictools.client.fx.FXBlock;
import binary404.mystictools.client.gui.GuiUpgrader;
import binary404.mystictools.common.tile.ModContainers;
import binary404.mystictools.common.tile.ModTiles;
import binary404.mystictools.common.core.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

    @Override
    public void registerHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            ClientHandler.KeyBindings.init();
        });
        ClientRegistry.bindTileEntityRenderer(ModTiles.CAULDRON, RenderCauldron::new);
        ScreenManager.registerFactory(ModContainers.UPGRADER, GuiUpgrader::new);
    }

    public void blockParticle(int x, int y, int z) {
        FXBlock data = new FXBlock(Minecraft.getInstance().world, x + 0.5, y + 0.5, z + 0.5, true, 600, Minecraft.getInstance().world.getBlockState(new BlockPos(x, y, z)).getBlock());
        Minecraft.getInstance().particles.addEffect(data);
    }

    @Override
    public World getWorld() {
        return Minecraft.getInstance().world;
    }
}
