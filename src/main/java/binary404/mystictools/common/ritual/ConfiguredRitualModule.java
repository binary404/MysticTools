package binary404.mystictools.common.ritual;

import binary404.mystictools.common.tile.SacrificialAltarBlockEntity;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public record ConfiguredRitualModule<MC extends RitualModuleConfiguration, M extends RitualModule<MC>>(M ritualModule, MC config) {

    public static final Codec<ConfiguredRitualModule<?, ?>> CODEC = RitualTypes.RITUAL_MODULE_REGISTRY.get().getCodec().dispatch((m) -> {
        return m.ritualModule;
    }, RitualModule::configuredCodec);

    public void clientTick(Level level, BlockPos pos, SacrificialAltarBlockEntity blockEntity) {
        this.ritualModule.clientTick(config, level, pos, blockEntity);
    }

    public void tick(Level level, BlockPos pos, SacrificialAltarBlockEntity blockEntity) {
        this.ritualModule.tick(config, level, pos, blockEntity);
    }

}
