package binary404.mystictools;

import binary404.mystictools.common.world.cabability.CapabilityHelper;
import binary404.mystictools.proxy.ClientProxy;
import binary404.mystictools.proxy.IProxy;
import binary404.mystictools.proxy.ServerProxy;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mystictools")
public class MysticTools {

    public static MysticTools instance;
    public static IProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger("mystictools");

    public MysticTools() {
        instance = this;
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        proxy.registerHandlers();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        CapabilityHelper.init();
    }

}
