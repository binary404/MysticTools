package binary404.mystictools.common.entity;

import binary404.mystictools.MysticTools;
import binary404.mystictools.client.render.RenderNoOp;
import binary404.mystictools.common.items.MysticTier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MysticTools.modid);

    public static final RegistryObject<EntityType<EffectCloudEntity>> EFFECT_CLOUD = ENTITIES.register("effect_cloud", () -> EntityType.Builder.<EffectCloudEntity>of(EffectCloudEntity::new, MobCategory.MISC).fireImmune().sized(6.0f, 0.5f)
            .clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).build(""));

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.EFFECT_CLOUD.get(), RenderNoOp::new);
    }
}
