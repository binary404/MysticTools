package binary404.mystictools.common.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistryHelper {

    public static <V extends IForgeRegistryEntry<V>> V register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name) {
        reg.register(thing.setRegistryName(name));
        return (V) thing;
    }

    public static <V extends IForgeRegistryEntry<V>> V register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
        register(reg, thing, new ResourceLocation("mystictools", name));
        return (V) thing;
    }

}
