package binary404.mystictools;

import binary404.mystictools.proxy.ClientProxy;
import binary404.mystictools.proxy.IProxy;
import binary404.mystictools.proxy.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mystictools")
public class MysticTools {

    public static MysticTools instance;
    public static IProxy proxy = new IProxy() {
    };

    public static final Logger LOGGER = LogManager.getLogger("mystictools");

    public static final String modid = "mystictools";

    public MysticTools() {
        instance = this;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.runForDist(() -> () -> proxy = new ClientProxy(), () -> () -> proxy = new ServerProxy());
        proxy.registerHandlers();
        proxy.attachEventHandlers(MinecraftForge.EVENT_BUS);
        proxy.attachEventHandlers(modEventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::parallelDispatch);
    }

    private void parallelDispatch(ParallelDispatchEvent event) {
    }


    private void commonSetup(FMLCommonSetupEvent event) {
    }

    public static boolean fxlibLoaded() {
        return ModList.get().isLoaded("fx_lib");
    }

}
