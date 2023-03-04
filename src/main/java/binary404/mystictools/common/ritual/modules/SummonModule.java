package binary404.mystictools.common.ritual.modules;

import binary404.mystictools.common.ritual.RitualContext;
import binary404.mystictools.common.ritual.RitualModule;
import binary404.mystictools.common.ritual.modules.config.SummonConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;

public class SummonModule extends RitualModule<SummonConfig> {

    public SummonModule(Codec<SummonConfig> codec) {
        super(codec);
    }

    @Override
    public void clientTick(SummonConfig config, RitualContext context) {

    }

    @Override
    public void tick(SummonConfig config, RitualContext context) {

    }

    @Override
    public void onEnd(SummonConfig config, RitualContext context) {
        Level level = context.getLevel();
        RandomSource random = level.getRandom();

        BlockPos toSummon = context.getPos().above(6);
        if (level instanceof ServerLevel serverLevel) {
            for (SummonConfig.WeightedEntityType entityType : config.entityTypes) {
                if (random.nextFloat() < entityType.weight()) {
                    entityType.entity().spawn(serverLevel, toSummon, MobSpawnType.MOB_SUMMONED);
                    return;
                }
            }
        }
    }
}
