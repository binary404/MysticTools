package binary404.mystictools.common.world.gen;

import binary404.mystictools.MysticTools;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
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
        return GenerationStep.Decoration.UNDERGROUND_DECORATION;
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

        if (worldgenrandom.nextDouble() < 0.6)
            return true;

        return false;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        JigsawConfiguration newConfig = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(MysticTools.modid, "shrine/starts")), 7);

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

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);

        NoiseColumn column = context.chunkGenerator().getBaseColumn(blockpos.getX(), blockpos.getZ(), context.heightAccessor());

        for (int y = -58; y < 33; y++) {
            if (column.getBlock(y).getBlock() instanceof AirBlock) {
                if (column.getBlock(y + 6).getBlock() instanceof AirBlock && !(column.getBlock(y - 2).getBlock() instanceof AirBlock)) {
                    MysticTools.LOGGER.error("generating shrine at " + blockpos.getX() + " " + y + " " + blockpos.getZ());
                    return JigsawPlacement.addPieces(newContext, PoolElementStructurePiece::new, new BlockPos(blockpos.getX(), y, blockpos.getZ()), false, false);
                }
            }
        }
        return Optional.empty();
    }
}
