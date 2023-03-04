package binary404.mystictools.common.ritual;

import binary404.mystictools.common.tile.SacrificialAltarBlockEntity;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record ConfiguredRitualModule<MC extends RitualModuleConfiguration, M extends RitualModule<MC>>(M ritualModule, MC config) {
    public static final Codec<ConfiguredRitualModule<?, ?>> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> ModRituals.RITUAL_MODULE_REGISTRY.get().getCodec().dispatch((m) -> {
        return m.ritualModule;
    }, RitualModule::configuredCodec));
    public static final Codec<Holder<ConfiguredRitualModule<?, ?>>> CODEC = RegistryFileCodec.create(ModRituals.CONFIGURED_RITUAL_MODULE_KEY, DIRECT_CODEC);
    public static final Codec<HolderSet<ConfiguredRitualModule<?, ?>>> LIST_CODEC = RegistryCodecs.homogeneousList(ModRituals.CONFIGURED_RITUAL_MODULE_KEY, DIRECT_CODEC);

    public void clientTick(RitualContext context) {
        this.ritualModule.clientTick(config, context);
    }

    public void tick(RitualContext context) {
        this.ritualModule.tick(config, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ConfiguredRitualModule) obj;
        return Objects.equals(this.ritualModule, that.ritualModule) &&
                Objects.equals(this.config, that.config);
    }

    @Override
    public String toString() {
        return "ConfiguredRitualModule[" +
                "ritualModule=" + ritualModule + ", " +
                "config=" + config + ']';
    }
}
