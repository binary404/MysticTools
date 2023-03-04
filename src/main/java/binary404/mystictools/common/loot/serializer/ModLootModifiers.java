package binary404.mystictools.common.loot.serializer;

import binary404.mystictools.MysticTools;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MysticTools.modid);
    public static final DeferredRegister<LootItemConditionType> CONDITION = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MysticTools.modid);

    public static final RegistryObject<Codec<AutoSmeltModifier>> AUTOSMELT = GLM.register("autosmelt", AutoSmeltModifier.CODEC);
    public static final RegistryObject<Codec<VoidModifier>> VOID = GLM.register("void", VoidModifier.CODEC);

    public static final RegistryObject<LootItemConditionType> AUTOSMELT_CONDITION = CONDITION.register("autosmelt", () -> new LootItemConditionType(new AutoSmeltCondition.AutoSmeltSerializer()));
    public static final RegistryObject<LootItemConditionType> VOID_CONDITION = CONDITION.register("void", () -> new LootItemConditionType(new VoidCondition.VoidSerializer()));
}
