package binary404.mystictools.common.ritual.modules;

import binary404.mystictools.common.ritual.RitualContext;
import binary404.mystictools.common.ritual.RitualModule;
import binary404.mystictools.common.ritual.modules.config.ParticleConfig;
import com.mojang.serialization.Codec;

public class ParticleModule extends RitualModule<ParticleConfig> {

    public ParticleModule(Codec<ParticleConfig> codec) {
        super(codec);
    }

    @Override
    public void clientTick(ParticleConfig config, RitualContext context) {

    }

    @Override
    public void tick(ParticleConfig config, RitualContext context) {
    }
}
