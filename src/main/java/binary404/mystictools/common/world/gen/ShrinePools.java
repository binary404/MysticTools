package binary404.mystictools.common.world.gen;

import binary404.mystictools.MysticTools;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class ShrinePools {

    public static void bootstrap() {

    }

    static {
        Pools.register(new StructureTemplatePool(new ResourceLocation(MysticTools.modid, "shrine/start"), new ResourceLocation("empty"), ImmutableList.of()));
    }
    
}
