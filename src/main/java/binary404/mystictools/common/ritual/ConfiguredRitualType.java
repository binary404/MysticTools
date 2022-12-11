package binary404.mystictools.common.ritual;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public record ConfiguredRitualType<RC extends RitualConfiguration, R extends RitualType<RC>>(R ritualType, RC config) {

    public static final Codec<ConfiguredRitualType<?, ?>> CODEC = (RitualTypes.RITUAL_TYPE_REGISTRY.get().getCodec()).dispatch((r) -> {
        return r.ritualType;
    }, RitualType::configuredCodec);

    public boolean startRitual(Level level, BlockPos pos) {
        return this.ritualType.startRitual(config, level, pos);
    }

    public boolean tickRitual(Level level, BlockPos pos) {
        return this.ritualType.tickRitual(config, level, pos);
    }

    public boolean endRitual(Level level, BlockPos pos) {
        return this.ritualType.endRitual(config, level, pos);
    }
}
