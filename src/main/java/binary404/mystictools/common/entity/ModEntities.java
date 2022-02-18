package binary404.mystictools.common.entity;

import binary404.mystictools.client.render.RenderNoOp;
import binary404.mystictools.common.items.MysticTier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static binary404.mystictools.common.core.RegistryHelper.register;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final EntityType<EffectCloudEntity> EFFECT_CLOUD = EntityType.Builder.<EffectCloudEntity>of(EffectCloudEntity::new, MobCategory.MISC)
            .fireImmune().sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).build("");

    @SubscribeEvent
    public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event) {
        IForgeRegistry<EntityType<?>> r = event.getRegistry();
        register(r, EFFECT_CLOUD, "effect_cloud");
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.EFFECT_CLOUD, RenderNoOp::new);
    }
}
