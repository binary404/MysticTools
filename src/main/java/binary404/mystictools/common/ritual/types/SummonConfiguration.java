package binary404.mystictools.common.ritual.types;

import binary404.mystictools.common.ritual.RitualConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class SummonConfiguration implements RitualConfiguration {

    public static final Codec<SummonConfiguration> CODEC = RecordCodecBuilder.create((r) -> {
        return r.group(Codec.list(ForgeRegistries.ENTITY_TYPES.getCodec()).fieldOf("entities").forGetter((p1) -> p1.entities),
                Codec.INT.fieldOf("count").forGetter((p1) -> p1.count)
        ).apply(r, SummonConfiguration::new);
    });

    public final List<EntityType<?>> entities;
    public final int count;

    public SummonConfiguration(List<EntityType<?>> entities, int count) {
        this.entities = entities;
        this.count = count;
    }
}
