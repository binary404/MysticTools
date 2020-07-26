package binary404.mystictools.proxy;

import binary404.mystictools.client.RenderCauldron;
import binary404.mystictools.common.blocks.ModTiles;
import binary404.mystictools.common.core.ClientHandler;
import net.minecraftforge.common.ForgeConfig;
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
    }

}
