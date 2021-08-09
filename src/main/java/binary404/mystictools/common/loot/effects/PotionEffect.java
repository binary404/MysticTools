package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.LootTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class PotionEffect implements IEffect {
    public static final Map<String, PotionEffect> REGISTRY = new HashMap<>();

    public static PotionEffect WITHERING;
    public static PotionEffect SLOWNESS;
    public static PotionEffect POISON;
    public static PotionEffect WEAKNESS;
    public static PotionEffect BLINDNESS;

    public static PotionEffect REGENERATION;
    public static PotionEffect SPEED;
    public static PotionEffect HASTE;
    public static PotionEffect RESISTANCE;
    public static PotionEffect FIRE_RESISTANCE;

    public static PotionEffect NIGHTVISION;
    public static PotionEffect WATERBREATHING;
    public static PotionEffect HEROOFTHEVILLAGE;

    public static void init() {
        if (ConfigHandler.COMMON.enableWither.get())
            WITHERING = create("wither", PotionType.TARGET, MobEffects.WITHER).setAmplifier(0, 1).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enableSlowness.get())
            SLOWNESS = create("slowness", PotionType.TARGET, MobEffects.MOVEMENT_SLOWDOWN).setAmplifier(1, 5).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enablePoison.get())
            POISON = create("poison", PotionType.TARGET, MobEffects.POISON).setAmplifier(0, 3).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enableWeakness.get())
            WEAKNESS = create("weakness", PotionType.TARGET, MobEffects.WEAKNESS).setAmplifier(0, 3).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enableBlindness.get())
            BLINDNESS = create("blindness", PotionType.TARGET, MobEffects.BLINDNESS).setAmplifier(0, 3).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enableRegeneration.get())
            REGENERATION = create("regeneration", PotionType.USER, MobEffects.REGENERATION).setAmplifier(0, 2).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableSpeed.get())
            SPEED = create("speed", PotionType.USER, MobEffects.MOVEMENT_SPEED).setAmplifier(0, 1).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableHaste.get())
            HASTE = create("haste", PotionType.USER, MobEffects.DIG_SPEED).setAmplifier(2, 4).setItemTypes(LootSet.LootSetType.AXE, LootSet.LootSetType.PICKAXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableResistance.get())
            RESISTANCE = create("resistance", PotionType.USER, MobEffects.DAMAGE_RESISTANCE).setAmplifier(0, 2).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.SWORD, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableFireResistance.get())
            FIRE_RESISTANCE = create("fire_resistance", PotionType.USER, MobEffects.FIRE_RESISTANCE).setAmplifier(0, 0).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.SWORD, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableNightVision.get())
            NIGHTVISION = create("night_vision", PotionType.USER, MobEffects.NIGHT_VISION).setAmplifier(0, 0).setDuration(10, 10).setItemTypes(LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableWaterBreathing.get())
            WATERBREATHING = create("water_breathing", PotionType.USER, MobEffects.WATER_BREATHING).setAmplifier(0, 0).setItemTypes(LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableHeroOfTheVillage.get())
            HEROOFTHEVILLAGE = create("hero_of_the_village", PotionType.USER, MobEffects.HERO_OF_THE_VILLAGE).setAmplifier(0, 4).setItemTypes(LootSet.LootSetType.ARMOR_HELMET);
    }

    private final PotionType effectType;

    private String id;
    private MobEffect effect;

    private List<LootSet.LootSetType> applyToItems = new ArrayList<LootSet.LootSetType>();

    private int durationMin = 100;
    private int durationMax = 300;
    private int amplifierMin = 0;
    private int amplifierMax = 0;

    private PotionEffect(PotionType type) {
        this.effectType = type;
    }

    public PotionType getType() {
        return this.effectType;
    }

    public boolean applyToItemType(LootSet.LootSetType type) {
        return applyToItems.contains(type);
    }

    protected PotionEffect setItemTypes(LootSet.LootSetType... itemTypes) {
        for (LootSet.LootSetType type : itemTypes) {
            applyToItems.add(type);
        }

        return this;
    }

    @Nullable
    public static PotionEffect getById(String id) {
        PotionEffect effect = REGISTRY.get(id);

        return effect;
    }

    public String getId() {
        return this.id;
    }

    public CompoundTag getNbt(Random rand) {
        CompoundTag tag = new CompoundTag();

        tag.putString("id", this.getId());
        tag.putInt("duration", this.getDuration(rand));
        tag.putInt("amplifier", this.getAmplifier(rand));

        return tag;
    }

    public static int getDurationFromStack(ItemStack stack, String effectId) {
        int duration = 0;

        List<PotionEffectInstance> effects = ModAttributes.LOOT_POTION_EFFECTS.getOrDefault(stack, new ArrayList<>()).getValue(stack);

        for (PotionEffectInstance instance : effects) {
            if (instance.getId().contains(effectId)) {
                duration = instance.getDuration();
            }
        }

        return duration;
    }

    public static int getAmplifierFromStack(ItemStack stack, String effectId) {
        int amplifier = 0;

        List<PotionEffectInstance> effects = ModAttributes.LOOT_POTION_EFFECTS.getOrDefault(stack, new ArrayList<>()).getValue(stack);

        for (PotionEffectInstance instance : effects) {
            if (instance.getId().contains(effectId)) {
                amplifier = instance.getAmplifier();
            }
        }

        return amplifier;
    }

    public static List<PotionEffectInstance> getPotionlist(ItemStack stack) {
        List<PotionEffectInstance> list = ModAttributes.LOOT_POTION_EFFECTS.getOrDefault(stack, new ArrayList<>()).getValue(stack);

        return list;
    }

    protected static PotionEffect create(String id, PotionType type, MobEffect effect) {
        PotionEffect weaponEffect = new PotionEffect(type);

        weaponEffect.id = id;
        weaponEffect.effect = effect;

        REGISTRY.put(id, weaponEffect);

        return weaponEffect;
    }

    protected PotionEffect setDuration(int min, int max) {
        this.durationMin = min * 100;
        this.durationMax = max * 100;
        return this;
    }

    protected PotionEffect setAmplifier(int min, int max) {
        this.amplifierMin = min;
        this.amplifierMax = max;
        return this;
    }

    public int getDuration(Random rand) {
        int duration = this.durationMin;

        if (duration < this.durationMax)
            duration += rand.nextInt(this.durationMax - duration + 1);

        return duration;
    }

    public int getAmplifier(Random rand) {
        int amplifier = this.amplifierMin;

        if (amplifier < this.amplifierMax)
            amplifier += rand.nextInt(this.amplifierMax - amplifier + 1);

        return amplifier;
    }

    @Nullable
    public MobEffectInstance getPotionEffect(int duration, int amplifier) {
        if (this.effect == null)
            return null;

        MobEffectInstance weaponEffect = new MobEffectInstance(this.effect, duration, amplifier, true, false);
        return weaponEffect;
    }

    public void onHit(int duration, int amplifier, LivingEntity target, LivingEntity attacker) {
        MobEffectInstance instance = this.getPotionEffect(duration, amplifier);
        if (instance != null) {
            if (this.getType() == PotionType.USER)
                attacker.addEffect(instance);
            else
                target.addEffect(instance);
        }
    }

    public String getAmplifierString(ItemStack stack, String effectId) {
        return getAmplifierString(stack, effectId, 0);
    }

    public String getAmplifierString(ItemStack stack, String effectId, int add) {
        return ChatFormatting.BOLD + "" + (getAmplifierFromStack(stack, effectId) + add) + "" + ChatFormatting.RESET + "" + ((this.getType() == PotionType.USER) ? ChatFormatting.GOLD : ChatFormatting.RED) + "";
    }

    public String getDurationString(ItemStack stack, String effectId) {
        return ChatFormatting.BOLD + "" + (getDurationFromStack(stack, effectId) / 100) + "" + ChatFormatting.RESET + "" + ChatFormatting.GREEN + "";
    }

    public enum PotionType {
        USER(ChatFormatting.GOLD),
        TARGET(ChatFormatting.RED);

        private ChatFormatting color;

        private PotionType(ChatFormatting color) {
            this.color = color;
        }

        public ChatFormatting getColor() {
            return this.color;
        }

        public boolean equals(PotionType type) {
            return type == this;
        }
    }
}
