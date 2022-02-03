package binary404.mystictools.common.loot;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.core.config.ModConfigs;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.LootEffect;
import com.google.gson.annotations.Expose;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.*;

public class LootRarity {

    public static final Map<String, LootRarity> REGISTRY = new HashMap<>();

    public static LootRarity generateRandomRarity(Random random, Player entity) {
/*        int rarity = random.nextInt(100) + 1;
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
            lootRarity = LootRarity.COMMON;*/
        LootRarity rarity = ModConfigs.RARITIES.RARITIES.getRandom(random);
        System.out.println(ModConfigs.RARITIES.RARITIES);
        return rarity;
    }

    public static void init() {
        //NO-OP
    }

    public static LootRarity COMMON = get("common", ChatFormatting.WHITE)
            .setDamage(5, 7)
            .setSpeed(-3.1F, -2.399F)
            .setArmor(1, 3)
            .setToughness(0, 0)
            .setEfficiency(5.0F, 12.0F)
            .setDurability(100, 500)
            .setPotionCount(0, 1)
            .setEffectCount(0, 0)
            .setPossibleEffects("dash", "sleep", "void", "health", "blast")
            .setUnbreakableChance(0);

    public static LootRarity UNCOMMON = get("uncommon", ChatFormatting.GRAY)
            .setDamage(7, 10)
            .setSpeed(-2.8F, -2.3F)
            .setArmor(2, 5)
            .setToughness(1, 3)
            .setEfficiency(11.0F, 25.0F)
            .setDurability(350, 1450)
            .setPotionCount(2, 3)
            .setEffectCount(0, 1)
            .setPossibleEffects("knockback_resistance", "dash", "sleep", "void", "auto_smelt", "blast")
            .setUnbreakableChance(5);

    public static LootRarity RARE = get("rare", ChatFormatting.YELLOW)
            .setDamage(10, 13)
            .setSpeed(-2.6999F, -2.1F)
            .setArmor(3, 6)
            .setToughness(2, 5)
            .setEfficiency(15.0F, 40.0F)
            .setDurability(850, 2500)
            .setPotionCount(1, 4)
            .setEffectCount(1, 2)
            .setPossibleEffects("knockback_resistance", "dash", "sleep", "void", "auto_smelt", "insight", "area_miner", "leech", "jump", "blast")
            .setUnbreakableChance(7);

    public static LootRarity EPIC = get("epic", ChatFormatting.BLUE)
            .setDamage(13, 17)
            .setSpeed(-2.39999F, -1.8F)
            .setArmor(4, 8)
            .setToughness(1, 5)
            .setEfficiency(20.0F, 42.0F)
            .setDurability(2000, 4500)
            .setPotionCount(1, 4)
            .setEffectCount(1, 3)
            .setPossibleEffects("potion_cloud", "reach", "knockback_resistance", "dash", "sleep", "void", "auto_smelt", "insight", "area_miner", "leech", "jump", "health", "stun", "shockwave", "reflect", "parry", "multi", "lightning", "heal", "blast")
            .setUnbreakableChance(15);

    public static LootRarity UNIQUE = get("unique", ChatFormatting.DARK_PURPLE)
            .setDamage(17, 27)
            .setSpeed(-2.0F, -1.5F)
            .setArmor(5, 10)
            .setToughness(2, 6)
            .setEfficiency(20.56F, 56.05F)
            .setDurability(1000, 4000)
            .setPotionCount(3, 5)
            .setEffectCount(3, 4)
            .setPossibleEffects("potion_cloud", "reach", "knockback_resistance", "dash", "sleep", "void", "auto_smelt", "insight", "area_miner", "leech", "jump", "health", "stun", "shockwave", "reflect", "parry", "multi", "lightning", "heal", "blast")
            .setUnbreakableChance(30);

    @Expose
    private String id;
    @Expose
    private ChatFormatting color = ChatFormatting.WHITE;
    @Expose
    private int unbreakableChance;
    @Expose
    private int damageMin = 0;
    @Expose
    private int damageMax = 7;
    @Expose
    private float speedMin = 0.0f;
    @Expose
    private float speedMax = 1.0f;
    @Expose
    private double armorMin = 3.0;
    @Expose
    private double armorMax = 10.0;
    @Expose
    private double toughnessMin = 3.0;
    @Expose
    private double toughnessMax = 10.0;
    @Expose
    private double efficiencyMin = 1.0;
    @Expose
    private double efficiencyMax = 1.0;
    @Expose
    private int durabilityMin = 0;
    @Expose
    private int durabilityMax = 0;
    @Expose
    private int potionMin = 0;
    @Expose
    private int potionMax = 1;
    @Expose
    private int effectMin = 0;
    @Expose
    private int effectMax = 1;
    @Expose
    private List<String> possibleEffects = new ArrayList<>();

    private LootRarity() {
    }


    public ChatFormatting getColor() {
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

    public List<String> getPossibleEffectIds() {
        return this.possibleEffects;
    }

    public List<LootEffect> getPossibleEffects() {
        List<LootEffect> effects = new ArrayList<>();
        for(String id : getPossibleEffectIds()) {
            LootEffect effect = LootEffect.REGISTRY.get(id);
            if(effect != null)
                effects.add(effect);
        }
        return effects;
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
            throw new SimpleCommandExceptionType(new TranslatableComponent("argument.id.invalid")).createWithContext(reader);
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
        this.toughnessMax = max;
        return this;
    }

    protected LootRarity setEfficiency(float min, float max) {
        this.efficiencyMin = min;
        this.efficiencyMax = max;
        return this;
    }

    protected LootRarity setUnbreakableChance(int chance) {
        this.unbreakableChance = chance;
        return this;
    }

    protected LootRarity setPossibleEffects(String... lootEffects) {
        this.possibleEffects.addAll(List.of(lootEffects));
        return this;
    }

    protected LootRarity setPossibleEffects(LootEffect... lootEffects) {
        List<String> possibleEffectsToAdd = new ArrayList<>();
        for (LootEffect effect : lootEffects) {
            possibleEffectsToAdd.add(effect.getId());
        }
        this.possibleEffects.addAll(possibleEffectsToAdd);
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

    public double getDamage(Random rand) {
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

    public double getArmor(Random rand) {
        double armor = this.armorMin;

        armor += (this.armorMax - armor) * rand.nextDouble();
        armor = (float) Math.round(armor * 100.0F) / 100.0F;

        return armor;
    }

    public double getToughness(Random rand) {
        double toughness = this.toughnessMin;

        toughness += (this.toughnessMax - toughness) * rand.nextDouble();
        toughness = (float) Math.round(toughness * 100.0F) / 100.0F;

        return toughness;
    }

    public double getEfficiency(Random rand) {
        double efficiency = this.efficiencyMin;

        efficiency += (this.efficiencyMax - efficiency) * rand.nextDouble();

        return efficiency;
    }

    public int getUnbreakableChance() {
        return unbreakableChance;
    }

    protected static LootRarity get(String id, ChatFormatting color) {
        LootRarity r = new LootRarity();

        r.id = id;
        r.color = color;

        return r;
    }

}
