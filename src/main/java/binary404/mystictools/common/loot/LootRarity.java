package binary404.mystictools.common.loot;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.items.ModItems;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LootRarity {

    private static final Map<String, LootRarity> REGISTRY = new HashMap<>();

    public static Map<String, LootRarity> getRegistry() {
        return REGISTRY;
    }

    public static LootRarity generateRandomRarity(Random random, PlayerEntity entity) {
        int rarity = random.nextInt(100) + 1;
        LootRarity lootRarity;
        int commonRarity = ConfigHandler.COMMON.commonRarity.get();
        int uncommonRarity = ConfigHandler.COMMON.uncommonRarity.get();
        int rareRarity = ConfigHandler.COMMON.rareRarity.get();
        int epicRarity = ConfigHandler.COMMON.epicRarity.get();
        int uniqueRarity = ConfigHandler.COMMON.uniqueRarity.get();

        if (rarity <= commonRarity)
            lootRarity = LootRarity.COMMON;
        else if (rarity > commonRarity && rarity <= commonRarity + uncommonRarity)
            lootRarity = LootRarity.UNCOMMON;
        else if (rarity > commonRarity + uncommonRarity && rarity <= commonRarity + uncommonRarity + rareRarity)
            lootRarity = LootRarity.RARE;
        else if (rarity > commonRarity + uncommonRarity + rareRarity && rarity <= commonRarity + uncommonRarity + rareRarity + epicRarity)
            lootRarity = LootRarity.EPIC;
        else if (rarity > commonRarity + uncommonRarity + rareRarity + epicRarity && rarity <= commonRarity + uncommonRarity + rareRarity + epicRarity + uniqueRarity)
            lootRarity = LootRarity.UNIQUE;
        else
            lootRarity = LootRarity.COMMON;
        return lootRarity;
    }

    public static void init() {
        COMMON = get("common", TextFormatting.WHITE)
                .setDamage(ConfigHandler.COMMON.commonDamageMin.get(), ConfigHandler.COMMON.commonDamageMax.get())
                .setSpeed(-3.1F, -2.399F)
                .setArmor(1, 3)
                .setToughness(0, 0)
                .setEfficiency(5.0F, 12.0F)
                .setDurability(100, 500)
                .setPotionCount(ConfigHandler.COMMON.commonPotionMin.get(), ConfigHandler.COMMON.commonPotionMax.get())
                .setEffectCount(ConfigHandler.COMMON.commonEffectMin.get(), ConfigHandler.COMMON.commonEffectMax.get());

        UNCOMMON = get("uncommon", TextFormatting.GRAY)
                .setDamage(ConfigHandler.COMMON.uncommonDamageMin.get(), ConfigHandler.COMMON.uncommonDamageMax.get())
                .setSpeed(-2.8F, -2.3F)
                .setArmor(2, 5)
                .setToughness(1, 3)
                .setEfficiency(11.0F, 25.0F)
                .setDurability(350, 1450)
                .setPotionCount(ConfigHandler.COMMON.uncommonPotionMin.get(), ConfigHandler.COMMON.uncommonPotionMax.get())
                .setEffectCount(ConfigHandler.COMMON.uncommonEffectMin.get(), ConfigHandler.COMMON.uncommonEffectMax.get());

        RARE = get("rare", TextFormatting.YELLOW)
                .setDamage(ConfigHandler.COMMON.rareDamageMin.get(), ConfigHandler.COMMON.rareDamageMax.get())
                .setSpeed(-2.6999F, -2.1F)
                .setArmor(3, 6)
                .setToughness(2, 5)
                .setEfficiency(15.0F, 40.0F)
                .setDurability(850, 2500)
                .setPotionCount(ConfigHandler.COMMON.rarePotionMin.get(), ConfigHandler.COMMON.rarePotionMax.get())
                .setEffectCount(ConfigHandler.COMMON.rareEffectMin.get(), ConfigHandler.COMMON.rareEffectMax.get());

        EPIC = get("epic", TextFormatting.BLUE)
                .setDamage(ConfigHandler.COMMON.epicDamageMin.get(), ConfigHandler.COMMON.epicDamageMax.get())
                .setSpeed(-2.39999F, -1.8F)
                .setArmor(4, 8)
                .setToughness(1, 5)
                .setEfficiency(20.0F, 42.0F)
                .setDurability(2000, 4500)
                .setPotionCount(ConfigHandler.COMMON.epicPotionMin.get(), ConfigHandler.COMMON.epicPotionMax.get())
                .setEffectCount(ConfigHandler.COMMON.epicEffectMin.get(), ConfigHandler.COMMON.epicEffectMax.get());

        UNIQUE = get("unique", TextFormatting.DARK_PURPLE)
                .setDamage(ConfigHandler.COMMON.uniqueDamageMin.get(), ConfigHandler.COMMON.uniqueDamageMax.get())
                .setSpeed(-2.0F, -1.5F)
                .setArmor(5, 10)
                .setToughness(2, 6)
                .setEfficiency(20.56F, 56.05F)
                .setDurability(1000, 4000)
                .setPotionCount(ConfigHandler.COMMON.uniquePotionMin.get(), ConfigHandler.COMMON.uniquePotionMax.get())
                .setEffectCount(ConfigHandler.COMMON.uniqueEffectMin.get(), ConfigHandler.COMMON.uniqueEffectMax.get());

        ModItems.common_case.lootRarity = COMMON;
        ModItems.uncommon_case.lootRarity = UNCOMMON;
        ModItems.rare_case.lootRarity = RARE;
        ModItems.epic_case.lootRarity = EPIC;
        ModItems.unique_case.lootRarity = UNIQUE;
    }

    public static LootRarity COMMON;

    public static LootRarity UNCOMMON;

    public static LootRarity RARE;

    public static LootRarity EPIC;

    public static LootRarity UNIQUE;

    private TextFormatting color = TextFormatting.WHITE;
    private int damageMin = 0;
    private int damageMax = 7;
    private float speedMin = 0.0F;
    private float speedMax = 1.0F;
    private float armorMin = 3.0F;
    private float armorMax = 10.0F;
    private float toughnessMin = 3.0F;
    private float tougnessMax = 10.0F;
    private float efficiencyMin = 1.0F;
    private float efficiencyMax = 1.0F;
    private int durabilityMin = 0;
    private int durabilityMax = 0;
    private int potionMin = 0;
    private int potionMax = 1;
    private int effectMin = 0;
    private int effectMax = 1;
    private String id;

    private LootRarity() {
    }


    public TextFormatting getColor() {
        return this.color;
    }

    public String getId() {
        return this.id;
    }

    @Nullable
    public static LootRarity fromId(String id) {
        LootRarity r = null;

        if (REGISTRY.containsKey(id)) {
            r = REGISTRY.get(id);
        }

        return r;
    }

    public static LootRarity read(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while (reader.canRead() && isValidPathCharacter(reader.peek())) {
            reader.skip();
        }

        String s = reader.getString().substring(i, reader.getCursor());

        try {
            return LootRarity.fromId(s);
        } catch (Exception e) {
            reader.setCursor(i);
            throw new SimpleCommandExceptionType(new TranslationTextComponent("argument.id.invalid")).createWithContext(reader);
        }
    }

    public static boolean isValidPathCharacter(char charIn) {
        return charIn >= 'a' && charIn <= 'z';
    }

    protected LootRarity setPotionCount(int min, int max) {
        this.potionMin = min;
        this.potionMax = max;
        return this;
    }

    protected LootRarity setEffectCount(int min, int max) {
        this.effectMin = min;
        this.effectMax = max;
        return this;
    }

    protected LootRarity setDurability(int min, int max) {
        this.durabilityMin = min;
        this.durabilityMax = max;
        return this;
    }

    protected LootRarity setDamage(int min, int max) {
        this.damageMin = min;
        this.damageMax = max;
        return this;
    }

    protected LootRarity setSpeed(float min, float max) {
        this.speedMin = min;
        this.speedMax = max;
        return this;
    }

    protected LootRarity setArmor(float min, float max) {
        this.armorMin = min;
        this.armorMax = max;
        return this;
    }

    protected LootRarity setToughness(float min, float max) {
        this.toughnessMin = min;
        this.tougnessMax = max;
        return this;
    }

    protected LootRarity setEfficiency(float min, float max) {
        this.efficiencyMin = min;
        this.efficiencyMax = max;
        return this;
    }

    public int getPotionCount(Random rand) {
        int modifierCount = this.potionMin;

        if (modifierCount < this.potionMax)
            modifierCount += rand.nextInt(this.potionMax - modifierCount + 1);

        return modifierCount;
    }

    public int getEffectCount(Random rand) {
        int modifierCount = this.effectMin;

        if (modifierCount < this.effectMax)
            modifierCount += rand.nextInt(this.effectMax - modifierCount + 1);

        return modifierCount;
    }

    public int getDurability(Random rand) {
        int durability = this.durabilityMin;

        if (durability < this.durabilityMax)
            durability += rand.nextInt(this.durabilityMax - durability + 1);

        return durability;
    }

    public int getDamage(Random rand) {
        int damage = this.damageMin;

        if (damage < this.damageMax)
            damage += rand.nextInt(this.damageMax - damage + 1);

        return damage;
    }

    public float getSpeed(Random rand) {
        float speed = this.speedMin;

        speed += (this.speedMax - speed) * rand.nextFloat();

        return speed;
    }

    public float getArmor(Random rand) {
        float armor = this.armorMin;

        armor += (this.armorMax - armor) * rand.nextFloat();
        armor = (float) Math.round(armor * 100.0F) / 100.0F;

        return armor;
    }

    public float getToughness(Random rand) {
        float toughness = this.toughnessMin;

        toughness += (this.tougnessMax - toughness) * rand.nextFloat();
        toughness = (float) Math.round(toughness * 100.0F) / 100.0F;

        return toughness;
    }

    public float getEfficiency(Random rand) {
        float efficiency = this.efficiencyMin;

        efficiency += (this.efficiencyMax - efficiency) * rand.nextFloat();

        return efficiency;
    }

    protected static LootRarity get(String id, TextFormatting color) {
        LootRarity r = new LootRarity();

        r.id = id;
        r.color = color;

        REGISTRY.put(id, r);

        return r;
    }

}
