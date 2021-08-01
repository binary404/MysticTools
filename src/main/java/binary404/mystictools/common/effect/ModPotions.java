package binary404.mystictools.common.effect;

import binary404.mystictools.common.core.RegistryHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPotions {

    public static final MobEffect FREEZE = new EffectFreeze();

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<MobEffect> event) {
        IForgeRegistry<MobEffect> r = event.getRegistry();
        RegistryHelper.register(r, FREEZE, "freeze");
    }

}
