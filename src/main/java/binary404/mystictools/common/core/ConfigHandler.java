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
        public final ForgeConfigSpec.BooleanValue enableNightVision;
        public final ForgeConfigSpec.BooleanValue enableWaterBreathing;
        public final ForgeConfigSpec.BooleanValue enableHeroOfTheVillage;

        public final ForgeConfigSpec.BooleanValue enableLightning;
        public final ForgeConfigSpec.BooleanValue enableLeach;
        public final ForgeConfigSpec.IntValue maxLeachAmount;
        public final ForgeConfigSpec.BooleanValue enableHeal;
        public final ForgeConfigSpec.BooleanValue enableDash;
        public final ForgeConfigSpec.BooleanValue enableStun;
        public final ForgeConfigSpec.BooleanValue enableSleep;
        public final ForgeConfigSpec.BooleanValue enableMultiTool;
        public final ForgeConfigSpec.BooleanValue enableAreaMiner;
        public final ForgeConfigSpec.BooleanValue enableAutoSmelt;
        public final ForgeConfigSpec.BooleanValue enableVoid;
        public final ForgeConfigSpec.BooleanValue enableJump;
        public final ForgeConfigSpec.IntValue maxJumpAmount;
        public final ForgeConfigSpec.BooleanValue enableParry;
        public final ForgeConfigSpec.IntValue maxParryChance;
        public final ForgeConfigSpec.BooleanValue enableShockwave;
        public final ForgeConfigSpec.BooleanValue enableReflect;
        public final ForgeConfigSpec.BooleanValue enableHealth;
        public final ForgeConfigSpec.IntValue maxHealth;
        public final ForgeConfigSpec.BooleanValue enableKnockbackResistance;
        public final ForgeConfigSpec.IntValue maxKnockbackResistance;
        public final ForgeConfigSpec.BooleanValue enableInsight;
        public final ForgeConfigSpec.IntValue maxInsight;

        public final ForgeConfigSpec.BooleanValue enableMobDropCrate;

        public final ForgeConfigSpec.IntValue commonRarity;
        public final ForgeConfigSpec.IntValue uncommonRarity;
        public final ForgeConfigSpec.IntValue rareRarity;
        public final ForgeConfigSpec.IntValue epicRarity;
        public final ForgeConfigSpec.IntValue uniqueRarity;

        public final ForgeConfigSpec.IntValue commonDamageMin;
        public final ForgeConfigSpec.IntValue commonDamageMax;
        public final ForgeConfigSpec.IntValue commonPotionMin;
        public final ForgeConfigSpec.IntValue commonPotionMax;
        public final ForgeConfigSpec.IntValue commonEffectMin;
        public final ForgeConfigSpec.IntValue commonEffectMax;

        public final ForgeConfigSpec.IntValue uncommonDamageMin;
        public final ForgeConfigSpec.IntValue uncommonDamageMax;
        public final ForgeConfigSpec.IntValue uncommonPotionMin;
        public final ForgeConfigSpec.IntValue uncommonPotionMax;
        public final ForgeConfigSpec.IntValue uncommonEffectMin;
        public final ForgeConfigSpec.IntValue uncommonEffectMax;

        public final ForgeConfigSpec.IntValue rareDamageMin;
        public final ForgeConfigSpec.IntValue rareDamageMax;
        public final ForgeConfigSpec.IntValue rarePotionMin;
        public final ForgeConfigSpec.IntValue rarePotionMax;
        public final ForgeConfigSpec.IntValue rareEffectMin;
        public final ForgeConfigSpec.IntValue rareEffectMax;

        public final ForgeConfigSpec.IntValue epicDamageMin;
        public final ForgeConfigSpec.IntValue epicDamageMax;
        public final ForgeConfigSpec.IntValue epicPotionMin;
        public final ForgeConfigSpec.IntValue epicPotionMax;
        public final ForgeConfigSpec.IntValue epicEffectMin;
        public final ForgeConfigSpec.IntValue epicEffectMax;

        public final ForgeConfigSpec.IntValue uniqueDamageMin;
        public final ForgeConfigSpec.IntValue uniqueDamageMax;
        public final ForgeConfigSpec.IntValue uniquePotionMin;
        public final ForgeConfigSpec.IntValue uniquePotionMax;
        public final ForgeConfigSpec.IntValue uniqueEffectMin;
        public final ForgeConfigSpec.IntValue uniqueEffectMax;

        public final ForgeConfigSpec.IntValue uniqueCount;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            commonDamageMin = builder.comment("Minimum damage on a common weapon").defineInRange("commonDamageMin", 5, 1, Integer.MAX_VALUE);
            commonDamageMax = builder.comment("Maximum damage on a common weapon").defineInRange("commonDamageMax", 10, 1, Integer.MAX_VALUE);
            commonPotionMin = builder.comment("Minimum number of potion effects on a common item").defineInRange("commonPotionMin", 0, 0, 5);
            commonPotionMax = builder.comment("Maximum number of potion effects on a common item").defineInRange("commonPotionMax", 1, 0, 5);
            commonEffectMin = builder.comment("Minimum number of effects on a common item").defineInRange("commonEffectMin", 0, 0, 4);
            commonEffectMax = builder.comment("Maximum number of effects on a common item").defineInRange("commonEffectMax", 0, 0, 4);

            uncommonDamageMin = builder.comment("Minimum damage on a uncommon weapon").defineInRange("uncommonDamageMin", 8, 1, Integer.MAX_VALUE);
            uncommonDamageMax = builder.comment("Maximum damage on a uncommon weapon").defineInRange("uncommonDamageMax", 13, 1, Integer.MAX_VALUE);
            uncommonPotionMin = builder.comment("Minimum number of potion effects on a uncommon item").defineInRange("uncommonPotionMin", 2, 0, 5);
            uncommonPotionMax = builder.comment("Maximum number of potion effects on a uncommon item").defineInRange("uncommonPotionMax", 3, 0, 5);
            uncommonEffectMin = builder.comment("Minimum number of effects on a uncommon item").defineInRange("uncommonEffectMin", 0, 0, 4);
            uncommonEffectMax = builder.comment("Maximum number of effects on a uncommon item").defineInRange("uncommonEffectMax", 1, 2, 4);

            rareDamageMin = builder.comment("Minimum damage on a rare weapon").defineInRange("rareDamageMin", 11, 1, Integer.MAX_VALUE);
            rareDamageMax = builder.comment("Maximum damage on a rare weapon").defineInRange("rareDamageMax", 16, 1, Integer.MAX_VALUE);
            rarePotionMin = builder.comment("Minimum number of potion effects on a rare item").defineInRange("rarePotionMin", 1, 0, 5);
            rarePotionMax = builder.comment("Maximum number of potion effects on a rare item").defineInRange("rarePotionMax", 4, 0, 5);
            rareEffectMin = builder.comment("Minimum number of effects on a rare item").defineInRange("rareEffectMin", 1, 0, 4);
            rareEffectMax = builder.comment("Maximum number of effects on a rare item").defineInRange("rareEffectMax", 2, 0, 4);

            epicDamageMin = builder.comment("Minimum damage on a epic weapon").defineInRange("epicDamageMin", 14, 1, Integer.MAX_VALUE);
            epicDamageMax = builder.comment("Maximum damage on a epic weapon").defineInRange("epicDamageMax", 20, 1, Integer.MAX_VALUE);
            epicPotionMin = builder.comment("Minimum number of potion effects on a epic item").defineInRange("epicPotionMin", 1, 0, 5);
            epicPotionMax = builder.comment("Maximum number of potion effects on a epic item").defineInRange("epicPotionMax", 4, 0, 5);
            epicEffectMin = builder.comment("Minimum number of effects on a epic item").defineInRange("epicEffectMin", 2, 0, 4);
            epicEffectMax = builder.comment("Maximum number of effects on a epic item").defineInRange("epicEffectMax", 3, 0, 4);

            uniqueDamageMin = builder.comment("Minimum damage on a unique weapon").defineInRange("uniqueDamageMin", 18, 1, Integer.MAX_VALUE);
            uniqueDamageMax = builder.comment("Maximum damage on a unique weapon").defineInRange("uniqueDamageMax", 26, 1, Integer.MAX_VALUE);
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
            enableNightVision = builder.comment("enable night vision effect on helmets").define("enableNightVision", true);
            enableWaterBreathing = builder.comment("enable water breathing effect on helmets").define("enableWaterBreathing", true);
            enableHeroOfTheVillage = builder.comment("enable hero of the village effect on helmets").define("enableHeroOfTheVillage", true);

            enableLightning = builder.comment("enable lightning effect on swords").define("enableLightning", true);
            enableLeach = builder.comment("enable life leach effect on swords").define("enableLeach", true);
            maxLeachAmount = builder.comment("max leach amount").defineInRange("maxLeachAmount", 100, 1, 100);
            enableHeal = builder.comment("enable heal effect on swords").define("enableHeal", true);
            enableDash = builder.comment("enable dash effect on swords").define("enableDash", true);
            enableStun = builder.comment("enable stun effect on swords").define("enableStun", true);
            enableSleep = builder.comment("enable sleep effect on tools").define("enableSleep", true);
            enableMultiTool = builder.comment("enable multi tool effect on tools").define("enableMultiTool", true);
            enableAreaMiner = builder.comment("enable area miner effect on tools").define("enableAreaMiner", true);
            enableAutoSmelt = builder.comment("enable auto smelt effect on tools").define("enableAutoSmelt", true);
            enableVoid = builder.comment("enable void effect on tools").define("enableVoid", true);
            enableJump = builder.comment("enable jump effect on boots").define("enableJump", true);
            maxJumpAmount = builder.comment("max jump level").defineInRange("maxJumpAmount", 5, 2, 10);
            enableParry = builder.comment("enable parry effect on boots").define("enableParry", true);
            maxParryChance = builder.comment("maximum parry chance").defineInRange("maximumParryChance", 65, 10, 100);
            enableShockwave = builder.comment("enable shockwave effect on leggings and chestplates").define("enableShockwave", true);
            enableReflect = builder.comment("enable reflect effect on leggings and chestplates").define("enableReflect", true);
            enableHealth = builder.comment("enable health boost effect on leggings and chestplates").define("enableHealth", true);
            maxHealth = builder.comment("max health boost").defineInRange("maxHealth", 6, 1, 20);
            enableKnockbackResistance = builder.comment("enable knockback resistance effect on leggings and chestplates").define("enableKnockbackResistance", true);
            maxKnockbackResistance = builder.comment("max knockback resistance").defineInRange("maxKnockbackResistance", 1, 1, 6);
            enableInsight = builder.comment("enable insight effect on helmets").define("enableInsight", true);
            maxInsight = builder.comment("max insight level").defineInRange("maxInsight", 5, 1, 20);

            enableMobDropCrate = builder.comment("enables mobs dropping crates").define("enableMobDropCrate", false);

            commonRarity = builder.comment("chance that a loot crate will generate a common item").defineInRange("commonRarity", 50, 0, 100);
            uncommonRarity = builder.comment("chance that a loot crate will generate a uncommon item").defineInRange("uncommonRarity", 20, 0, 100);
            rareRarity = builder.comment("chance that a loot crate will generate a rare item").defineInRange("rareRarity", 12, 0, 100);
            epicRarity = builder.comment("chance that a loot crate will generate a epic item").defineInRange("epicRarity", 10, 0, 100);
            uniqueRarity = builder.comment("chance that a loot crate will generate a unique item").defineInRange("uniqueRarity", 8, 0, 100);

            uniqueCount = builder.comment("amount of unique items that can be found in a world").defineInRange("uniqueCount", 5, 1, 50);
        }
    }
}
