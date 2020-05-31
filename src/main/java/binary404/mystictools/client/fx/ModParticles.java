package binary404.mystictools.client.fx;

import binary404.mystictools.client.fx.data.ArcData;
import binary404.mystictools.client.fx.data.BlockData;
import binary404.mystictools.client.fx.type.ArcType;
import binary404.mystictools.client.fx.type.BlockType;
import binary404.mystictools.common.core.RegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {


    @ObjectHolder("mystictools:arc")
    public static ParticleType<ArcData> ARC;

    @ObjectHolder("mystictools:block")
    public static ParticleType<BlockData> BLOCK;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
        RegistryHelper.register(event.getRegistry(), new ArcType(), "arc");
        RegistryHelper.register(event.getRegistry(), new BlockType(), "block");
    }

    @Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FactoryHandler {

        @SubscribeEvent
        public static void registerFactories(ParticleFactoryRegisterEvent event) {
            Minecraft.getInstance().particles.registerFactory(ModParticles.ARC, ArcType.Factory::new);
            Minecraft.getInstance().particles.registerFactory(ModParticles.BLOCK, BlockType.Factory::new);
        }

    }

}
