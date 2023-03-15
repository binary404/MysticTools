package binary404.mystictools.common.ritual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

public record Ritual(HolderSet<ConfiguredRitualModule<?, ?>> modules) {
    public static final Codec<Ritual> CODEC = ExtraCodecs.lazyInitializedCodec(() -> RecordCodecBuilder.create(instance -> instance.group(
            ConfiguredRitualModule.LIST_CODEC.fieldOf("modules").forGetter(Ritual::modules)
    ).apply(instance, Ritual::new)));

    public static final Ritual EMPTY = new Ritual(HolderSet.direct());

    public static ResourceLocation getRegistryId(Ritual ritual, Level level) {
        return level.registryAccess().registryOrThrow(ModRituals.RITUAL_REGISTRY_KEY).getKey(ritual);
    }

    public static Ritual getRitual(ResourceLocation location, Level level) {
        return level.registryAccess().registryOrThrow(ModRituals.RITUAL_REGISTRY_KEY).get(location);
    }
}
