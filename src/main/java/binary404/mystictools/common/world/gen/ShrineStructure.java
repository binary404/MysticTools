package binary404.mystictools.common.world.gen;

import binary404.mystictools.MysticTools;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import java.util.Optional;
import java.util.Random;

public class ShrineStructure extends StructureFeature<JigsawConfiguration> {

    private static final String NAMESPACE = "shrine";

    public ShrineStructure(Codec<JigsawConfiguration> p_197165_) {
        super(p_197165_, context -> {
            if (!checkLocation(context))
                return Optional.empty();

            return createGenerator(context);
        }, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public String getFeatureName() {
        return getRegistryName().toString();
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        int i = context.chunkPos().x >> 4;
        int j = context.chunkPos().z >> 4;
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setSeed((long) (i ^ j << 4) ^ context.seed());
        worldgenrandom.nextInt();
        if (worldgenrandom.nextDouble() < 0.3) {
            return true;
        } else {
            return false;
        }
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        int i = context.chunkPos().x >> 4;
        int j = context.chunkPos().z >> 4;
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setSeed((long) (i ^ j << 4) ^ context.seed());
        worldgenrandom.nextInt();
        int y = -64 + worldgenrandom.nextInt(99);

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(y);

        JigsawConfiguration newConfig = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY) .get(new ResourceLocation(MysticTools.modid, "shrine/start")), 7);

        PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.heightAccessor(),
                context.validBiome(),
                context.structureManager(),
                context.registryAccess()
        );

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator = JigsawPlacement.addPieces(newContext, PoolElementStructurePiece::new, blockpos, false, false);

        return structurePiecesGenerator;
    }
}
