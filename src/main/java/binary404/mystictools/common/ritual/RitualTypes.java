package binary404.mystictools.common.ritual;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.ritual.types.SummonConfiguration;
import binary404.mystictools.common.ritual.types.SummonRitualType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RitualTypes {

    //Registry's
    public static final DeferredRegister<RitualType<?>> RITUAL_TYPE_DEFERRED_REGISTER = DeferredRegister.create(new ResourceLocation(MysticTools.modid, "ritual_types"), MysticTools.modid);
    public static final Supplier<IForgeRegistry<RitualType<?>>> RITUAL_TYPE_REGISTRY = RITUAL_TYPE_DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);
    public static final DeferredRegister<RitualModule<?>> RITUAL_MODULE_DEFERRED_REGISTER = DeferredRegister.create(new ResourceLocation(MysticTools.modid, "ritual_modules"), MysticTools.modid);
    public static final Supplier<IForgeRegistry<RitualModule<?>>> RITUAL_MODULE_REGISTRY = RITUAL_MODULE_DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<RitualType<SummonConfiguration>> SUMMON = RITUAL_TYPE_DEFERRED_REGISTER.register("summon", () -> new SummonRitualType(SummonConfiguration.CODEC));
}
