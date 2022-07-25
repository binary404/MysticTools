package binary404.mystictools.common.effect;

import binary404.mystictools.MysticTools;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPotions {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MysticTools.modid);

    public static final RegistryObject<MobEffect> FREEZE = EFFECTS.register("freeze", () -> new EffectFreeze());
}
