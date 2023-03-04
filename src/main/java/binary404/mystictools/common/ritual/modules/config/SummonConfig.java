package binary404.mystictools.common.ritual.modules.config;

import binary404.mystictools.common.ritual.RitualModuleConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class SummonConfig implements RitualModuleConfiguration {

    public static final Codec<SummonConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedEntityType.CODEC.listOf().fieldOf("entityTypes").forGetter(config -> config.entityTypes)
    ).apply(instance, SummonConfig::new));

    public List<WeightedEntityType> entityTypes;

    public SummonConfig(List<WeightedEntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public record WeightedEntityType(EntityType<?> entity, float weight) {
        public static final Codec<WeightedEntityType> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                        ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entity").forGetter((weightedEntityType) -> weightedEntityType.entity),
                        Codec.floatRange(0.0f, 1.0f).fieldOf("weight").forGetter(WeightedEntityType::weight))
                .apply(instance, WeightedEntityType::new));
    }

}
