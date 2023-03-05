package binary404.mystictools.common.ritual;

import binary404.mystictools.common.tile.SacrificialAltarBlockEntity;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public abstract class RitualModule<MC extends RitualModuleConfiguration> {
    private final Codec<ConfiguredRitualModule<MC, RitualModule<MC>>> configuredCodec;

    public RitualModule(Codec<MC> codec) {
        this.configuredCodec = codec.fieldOf("config").xmap((r) -> {
            return new ConfiguredRitualModule<>(this, r);
        }, ConfiguredRitualModule::config).codec();
    }

    public Codec<ConfiguredRitualModule<MC, RitualModule<MC>>> configuredCodec() {
        return this.configuredCodec;
    }

    public abstract void clientTick(MC config, RitualContext context);

    public abstract void tick(MC config, RitualContext context);

    public boolean checkStart(MC config, RitualContext context) {
        return true;
    }

    public void onEnd(MC config, RitualContext context) {

    }
}
