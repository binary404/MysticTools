package binary404.mystictools.common.world.cabability;

import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityHelper {

    public static IUniqueSave getSave(World world) {
        LazyOptional<IUniqueSave> save = world.getCapability(UniqueSave.SaveProvider.SAVE);
        return save.orElse(new UniqueSave.Save());
    }

    public static void init() {
        CapabilityManager.INSTANCE.register(IUniqueSave.class, new UniqueSave.SaveStorage(), UniqueSave.Save::new);
    }

    @SubscribeEvent
    public static void attachCabability(AttachCapabilitiesEvent<World> event) {
        event.addCapability(UniqueSave.SaveProvider.NAME, new UniqueSave.SaveProvider());
    }

}
