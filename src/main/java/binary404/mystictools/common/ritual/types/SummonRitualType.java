package binary404.mystictools.common.ritual.types;

import binary404.mystictools.common.ritual.RitualType;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

public class SummonRitualType extends RitualType<SummonConfiguration> {
    public SummonRitualType(Codec<SummonConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean canStart(SummonConfiguration config, Level level, BlockPos pos) {

        return false;
    }

    @Override
    public boolean tickRitual(SummonConfiguration config, Level level, BlockPos pos) {
        return false;
    }

    @Override
    public boolean endRitual(SummonConfiguration config, Level level, BlockPos pos) {
        return false;
    }
}
