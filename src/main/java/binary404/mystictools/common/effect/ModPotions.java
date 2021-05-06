package binary404.mystictools.common.effect;

import binary404.mystictools.common.core.RegistryHelper;
import net.minecraft.potion.Effect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPotions {

    public static final Effect FREEZE = new EffectFreeze();

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Effect> event) {
        IForgeRegistry<Effect> r = event.getRegistry();
        RegistryHelper.register(r, FREEZE, "freeze");
    }

}
