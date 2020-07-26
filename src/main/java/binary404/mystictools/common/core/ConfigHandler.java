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

        public CommonConfig(ForgeConfigSpec.Builder builder) {
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
        }
    }
}
