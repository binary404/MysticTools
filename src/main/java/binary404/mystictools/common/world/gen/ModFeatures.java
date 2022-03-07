package binary404.mystictools.common.world.gen;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.blocks.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModFeatures {

    public static Holder<PlacedFeature> PERIDOT_ORE;

    public static void registerOres(FMLCommonSetupEvent event) {
/*        event.enqueueWork(() -> {
            Holder<ConfiguredFeature<OreConfiguration, ?>> peridotOre = FeatureUtils.register("mystictools:peridot_ore", Feature.ORE, new OreConfiguration(OreFeatures.NATURAL_STONE, ModBlocks.peridot_ore.defaultBlockState(), 3));
            PERIDOT_ORE = PlacementUtils.register("mystictools:peridot_ore", peridotOre, commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-64), VerticalAnchor.absolute(32))));
        });*/
    }

    public static void onBiomeLoad(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        MobSpawnSettingsBuilder spawns = event.getSpawns();

        Biome.BiomeCategory category = event.getCategory();
        ResourceLocation name = event.getName();
        ResourceKey<Biome> key = name == null ? null : ResourceKey.create(Registry.BIOME_REGISTRY, name);
        boolean hasNoTypes = key == null || !BiomeDictionary.hasAnyType(key);

        if (matches(hasNoTypes, key, category, null, BiomeDictionary.Type.OVERWORLD)) {
            generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PERIDOT_ORE);
        }
    }

    /**
     * Checks if the biome matches the given categories
     */
    private static boolean matches(boolean hasNoTypes, @Nullable ResourceKey<Biome> key, Biome.BiomeCategory given, @Nullable Biome.BiomeCategory check, BiomeDictionary.Type type) {
        if (hasNoTypes || key == null) {
            // check of null means not none, the nether/end checks were done earlier
            if (check == null) {
                return given != Biome.BiomeCategory.NONE;
            }
            return given == check;
        }
        // we have a key, require matching all the given types
        return BiomeDictionary.hasType(key, type);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    private static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }
}
