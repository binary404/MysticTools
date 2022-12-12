package binary404.mystictools.common.ritual;

import binary404.mystictools.MysticTools;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public abstract class RitualType<RC extends RitualConfiguration> {
    private final Codec<ConfiguredRitualType<RC, RitualType<RC>>> configuredCodec;

    public RitualType(Codec<RC> codec) {
        this.configuredCodec = codec.fieldOf("config").xmap((r) -> {
            return new ConfiguredRitualType<>(this, r);
        }, ConfiguredRitualType::config).codec();
    }

    public Codec<ConfiguredRitualType<RC, RitualType<RC>>> configuredCodec() {
        return configuredCodec;
    }

    //Return false if the ritual cannot begin - other checks
    public abstract boolean startRitual(RC config, Level level, BlockPos pos);

    //Return false if the current ritual tick cannot move forward
    public abstract boolean tickRitual(RC config, Level level, BlockPos pos);

    //Return false if the ritual is not finished
    public abstract boolean endRitual(RC config, Level level, BlockPos pos);
}
