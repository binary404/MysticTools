package binary404.mystictools.common.loot;

import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LootRarity {

    private static final Map<String, LootRarity> REGISTRY = new HashMap<>();


    public static final LootRarity COMMON = get("Common", TextFormatting.WHITE)
            .setDamage(6, 12)
            .setSpeed(-3.1F, -2.399F)
            .setArmor(0, 1)
            .setToughness(0, 0)
            .setEfficiency(5.0F, 12.0F)
            .setDurability(100, 500)
            .setPotionCount(0, 1)
            .setEffectCount(0, 1);

    public static final LootRarity UNCOMMON =
            get("Uncommon", TextFormatting.GRAY)
                    .setDamage(9, 16)
                    .setSpeed(-2.8F, -2.3F)
                    .setArmor(1, 3)
                    .setToughness(1, 3)
                    .setEfficiency(11.0F, 25.0F)
                    .setDurability(350, 1450)
                    .setPotionCount(0, 3)
                    .setEffectCount(0, 2);

    public static final LootRarity RARE =
            get("Rare", TextFormatting.YELLOW)
                    .setDamage(12, 29)
                    .setSpeed(-2.6999F, -2.1F)
                    .setArmor(2, 5)
                    .setToughness(2, 5)
                    .setEfficiency(15.0F, 40.0F)
                    .setDurability(850, 2500)
                    .setPotionCount(1, 4)
                    .setEffectCount(0, 3);

    public static final LootRarity EPIC =
            get("Epic", TextFormatting.BLUE)
                    .setDamage(16, 34)
                    .setSpeed(-2.39999F, -1.8F)
                    .setArmor(2, 5)
                    .setToughness(1, 5)
                    .setEfficiency(20.0F, 42.0F)
                    .setDurability(2000, 4500)
                    .setPotionCount(2, 5)
                    .setEffectCount(1, 3);

    public static final LootRarity UNIQUE =
            get("Unique", TextFormatting.DARK_PURPLE)
                    .setDamage(20, 54)
                    .setSpeed(-2.0F, -1.5F)
                    .setArmor(3, 7)
                    .setToughness(2, 6)
                    .setEfficiency(20.56F, 56.05F)
                    .setDurability(1000, 4000)
                    .setPotionCount(3, 6)
                    .setEffectCount(1, 3);

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

        System.out.println(modifierCount);
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
