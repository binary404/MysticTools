package binary404.mystictools.common.loot.modifiers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLootModifiers {

    public static final LootItemConditionType AUTOSMELT = new LootItemConditionType(new AutoSmeltCondition.AutoSmeltSerializer());
    public static final LootItemConditionType VOID = new LootItemConditionType(new VoidCondition.VoidSerializer());

    public static void init() {
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation("mystictools", "autosmelt"), AUTOSMELT);
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation("mystictools", "void"), VOID);
    }

    @SubscribeEvent
    public static void registerModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new AutoSmeltModifier.Serializer().setRegistryName(new ResourceLocation("mystictools", "autosmelt")));
        event.getRegistry().register(new VoidModifier.Serializer().setRegistryName(new ResourceLocation("mystictools", "void")));
    }

}
