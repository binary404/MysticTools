package binary404.mystictools.common.loot.modifiers;

import binary404.mystictools.MysticTools;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLootModifiers {

    public static final LootConditionType AUTOSMELT = new LootConditionType(new AutoSmeltCondition.Serializer());

    public static void init() {
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation("mystictools", "autosmelt"), AUTOSMELT);
    }

    @SubscribeEvent
    public static void registerModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new AutoSmeltModifier.Serializer().setRegistryName(new ResourceLocation("mystictools", "autosmelt")));
    }

}
