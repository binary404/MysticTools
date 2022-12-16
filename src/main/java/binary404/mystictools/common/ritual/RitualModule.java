package binary404.mystictools.common.ritual;

import binary404.mystictools.common.tile.SacrificialAltarBlockEntity;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

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

    public abstract void clientTick(MC config, Level level, BlockPos pos, SacrificialAltarBlockEntity blockEntity);

    public abstract void tick(MC config, Level level, BlockPos pos, SacrificialAltarBlockEntity blockEntity);

}
