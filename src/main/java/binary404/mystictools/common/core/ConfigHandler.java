package binary404.mystictools.common.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHandler {

    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.IntValue lootCrateWeight;
        public final ForgeConfigSpec.IntValue lootCrateMinRolls;
        public final ForgeConfigSpec.IntValue lootCrateMaxRools;

        public final ForgeConfigSpec.BooleanValue enableWither;
        public final ForgeConfigSpec.BooleanValue enableSlowness;
        public final ForgeConfigSpec.BooleanValue enablePoison;
        public final ForgeConfigSpec.BooleanValue enableWeakness;
        public final ForgeConfigSpec.BooleanValue enableBlindness;
        public final ForgeConfigSpec.BooleanValue enableRegeneration;
        public final ForgeConfigSpec.BooleanValue enableSpeed;
        public final ForgeConfigSpec.BooleanValue enableHaste;
        public final ForgeConfigSpec.BooleanValue enableResistance;
        public final ForgeConfigSpec.BooleanValue enableFireResistance;

        public final ForgeConfigSpec.BooleanValue enableMobDropCrate;

        public final ForgeConfigSpec.IntValue commonRarity;
        public final ForgeConfigSpec.IntValue uncommonRarity;
        public final ForgeConfigSpec.IntValue rareRarity;
        public final ForgeConfigSpec.IntValue epicRarity;
        public final ForgeConfigSpec.IntValue uniqueRarity;

        public final ForgeConfigSpec.IntValue commonPotionMin;
        public final ForgeConfigSpec.IntValue commonPotionMax;
        public final ForgeConfigSpec.IntValue commonEffectMin;
        public final ForgeConfigSpec.IntValue commonEffectMax;

        public final ForgeConfigSpec.IntValue uncommonPotionMin;
        public final ForgeConfigSpec.IntValue uncommonPotionMax;
        public final ForgeConfigSpec.IntValue uncommonEffectMin;
        public final ForgeConfigSpec.IntValue uncommonEffectMax;

        public final ForgeConfigSpec.IntValue rarePotionMin;
        public final ForgeConfigSpec.IntValue rarePotionMax;
        public final ForgeConfigSpec.IntValue rareEffectMin;
        public final ForgeConfigSpec.IntValue rareEffectMax;

        public final ForgeConfigSpec.IntValue epicPotionMin;
        public final ForgeConfigSpec.IntValue epicPotionMax;
        public final ForgeConfigSpec.IntValue epicEffectMin;
        public final ForgeConfigSpec.IntValue epicEffectMax;

        public final ForgeConfigSpec.IntValue uniquePotionMin;
        public final ForgeConfigSpec.IntValue uniquePotionMax;
        public final ForgeConfigSpec.IntValue uniqueEffectMin;
        public final ForgeConfigSpec.IntValue uniqueEffectMax;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            commonPotionMin = builder.comment("Minimum number of potion effects on a common item").defineInRange("commonPotionMin", 0, 0, 5);
            commonPotionMax = builder.comment("Maximum number of potion effects on a common item").defineInRange("commonPotionMax", 1, 0, 5);

            commonEffectMin = builder.comment("Minimum number of effects on a common item").defineInRange("commonEffectMin", 0, 0, 4);
            commonEffectMax = builder.comment("Maximum number of effects on a common item").defineInRange("commonEffectMax", 0, 0, 4);

            uncommonPotionMin = builder.comment("Minimum number of potion effects on a uncommon item").defineInRange("uncommonPotionMin", 0, 0, 5);
            uncommonPotionMax = builder.comment("Maximum number of potion effects on a uncommon item").defineInRange("uncommonPotionMax", 4, 0, 5);

            uncommonEffectMin = builder.comment("Minimum number of effects on a uncommon item").defineInRange("uncommonEffectMin", 0, 0, 4);
            uncommonEffectMax = builder.comment("Maximum number of effects on a uncommon item").defineInRange("uncommonEffectMax", 1, 2, 4);

            rarePotionMin = builder.comment("Minimum number of potion effects on a rare item").defineInRange("rarePotionMin", 1, 0, 5);
            rarePotionMax = builder.comment("Maximum number of potion effects on a rare item").defineInRange("rarePotionMax", 4, 0, 5);

            rareEffectMin = builder.comment("Minimum number of effects on a rare item").defineInRange("rareEffectMin", 1, 0, 4);
            rareEffectMax = builder.comment("Maximum number of effects on a rare item").defineInRange("rareEffectMax", 2, 0, 4);

            epicPotionMin = builder.comment("Minimum number of potion effects on a epic item").defineInRange("epicPotionMin", 1, 0, 5);
            epicPotionMax = builder.comment("Maximum number of potion effects on a epic item").defineInRange("epicPotionMax", 4, 0, 5);

            epicEffectMin = builder.comment("Minimum number of effects on a epic item").defineInRange("epicEffectMin", 2, 0, 4);
            epicEffectMax = builder.comment("Maximum number of effects on a epic item").defineInRange("epicEffectMax", 3, 0, 4);

            uniquePotionMin = builder.comment("Minimum number of potion effects on a unique item").defineInRange("uniquePotionMin", 3, 0, 5);
            uniquePotionMax = builder.comment("Maximum number of potion effects on a unique item").defineInRange("uniquePotionMax", 5, 0, 5);

            uniqueEffectMin = builder.comment("Minimum number of effects on a unique item").defineInRange("uniqueEffectMin", 3, 0, 4);
            uniqueEffectMax = builder.comment("Maximum number of effects on a unique item").defineInRange("uniqueEffectMax", 4, 0, 4);

            lootCrateWeight = builder.comment("The chance that the lootcrate loot table will be injected into a chest loot table").defineInRange("lootCrateWeight", 100, 1, 400);
            lootCrateMinRolls = builder.comment("The minimum number of rolls a loot crate will try to generate, each roll has a 30% chance of generating a loot crate").defineInRange("lootCrateMinRolls", 3, 0, 300);
            lootCrateMaxRools = builder.comment("the maximum number of rolls a loot crate will try to generate, each roll has a 30% chance of generating a loot crate").defineInRange("lootCrateMaxRolls", 5, 0, 300);

            enableWither = builder.comment("enable wither effect on tools").define("enableWither", true);
            enableSlowness = builder.comment("enable slowness effect on tools").define("enableSlowness", true);
            enablePoison = builder.comment("enable poison effect on tools").define("enablePoison", true);
            enableWeakness = builder.comment("enable weakness effect on tools").define("enableWeakness", true);
            enableBlindness = builder.comment("enable blindness effect on tools").define("enableBlindness", true);
            enableRegeneration = builder.comment("enable regeneration effect on tools").define("enableRegeneration", true);
            enableSpeed = builder.comment("enable speed effect on tools").define("enableSpeed", true);
            enableHaste = builder.comment("enable haste effect on tools").define("enableHaste", true);
            enableResistance = builder.comment("enable resistance effect on tools").define("enableResistance", true);
            enableFireResistance = builder.comment("enable fire resistance effect on tools").define("enableFireResistance", true);

            enableMobDropCrate = builder.comment("enables mobs dropping crates").define("enableMobDropCrate", true);

            commonRarity = builder.comment("chance that a loot crate will generate a common item").defineInRange("commonRarity", 50, 1, 100);
            uncommonRarity = builder.comment("chance that a loot crate will generate a uncommon item").defineInRange("uncommonRarity", 20, 1, 100);
            rareRarity = builder.comment("chance that a loot crate will generate a rare item").defineInRange("rareRarity", 12, 1, 100);
            epicRarity = builder.comment("chance that a loot crate will generate a epic item").defineInRange("epicRarity", 10, 1, 100);
            uniqueRarity = builder.comment("chance that a loot crate will generate a unique item").defineInRange("uniqueRarity", 8, 1, 100);
        }
    }
}
