package binary404.mystictools.common.ritual.modules;

import binary404.mystictools.common.ritual.RitualModuleConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BaseModuleConfiguration(float startFraction, int chance) implements RitualModuleConfiguration {

    public static final Codec<BaseModuleConfiguration> CODEC = RecordCodecBuilder.create((r) -> {
        return r.group(
                Codec.FLOAT.fieldOf("startFraction").forGetter(BaseModuleConfiguration::startFraction),
                Codec.INT.fieldOf("intChance").forGetter(BaseModuleConfiguration::chance)
        ).apply(r, BaseModuleConfiguration::new);
    });

}
