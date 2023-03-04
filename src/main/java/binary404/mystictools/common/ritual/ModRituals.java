package binary404.mystictools.common.ritual;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.ritual.modules.ParticleModule;
import binary404.mystictools.common.ritual.modules.SummonModule;
import binary404.mystictools.common.ritual.modules.config.ParticleConfig;
import binary404.mystictools.common.ritual.modules.config.SummonConfig;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRituals {
    //Registries
    public static final DeferredRegister<RitualModule<?>> RITUAL_MODULE_DEFERRED_REGISTER = DeferredRegister.create(new ResourceLocation(MysticTools.modid, "ritual_modules"), MysticTools.modid);
    public static final Supplier<IForgeRegistry<RitualModule<?>>> RITUAL_MODULE_REGISTRY = RITUAL_MODULE_DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<RitualModule<?>> particle_module = RITUAL_MODULE_DEFERRED_REGISTER.register("particle_module", () -> new ParticleModule(ParticleConfig.CODEC));
    public static final RegistryObject<RitualModule<?>> summon_module = RITUAL_MODULE_DEFERRED_REGISTER.register("summon_module", () -> new SummonModule(SummonConfig.CODEC));

    //Datapack registries
    public static final ResourceKey<Registry<Ritual>> RITUAL_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MysticTools.modid, "ritual"));
    public static final ResourceKey<Registry<ConfiguredRitualModule<?, ?>>> CONFIGURED_RITUAL_MODULE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MysticTools.modid, "configured_ritual_module"));

    @SubscribeEvent
    public static void createDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(RITUAL_REGISTRY_KEY, Ritual.CODEC, Ritual.CODEC);
        event.dataPackRegistry(CONFIGURED_RITUAL_MODULE_KEY, ConfiguredRitualModule.DIRECT_CODEC, ConfiguredRitualModule.DIRECT_CODEC);
        MysticTools.LOGGER.info("Registered ritual datapack registries");
    }
}
