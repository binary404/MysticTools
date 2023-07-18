package binary404.mystictools;

import binary404.mystictools.proxy.ClientProxy;
import binary404.mystictools.proxy.IProxy;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
        proxy.registerHandlers();
        proxy.attachEventHandlers(MinecraftForge.EVENT_BUS);
        proxy.attachEventHandlers(modEventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::parallelDispatch);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void parallelDispatch(ParallelDispatchEvent event) {
    }

    private void registerCommands(RegisterCommandsEvent event) {
        //GenerateLootCommand.register(event.getDispatcher());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    public static boolean fxlibLoaded() {
        return ModList.get().isLoaded("fx_lib");
    }

}
