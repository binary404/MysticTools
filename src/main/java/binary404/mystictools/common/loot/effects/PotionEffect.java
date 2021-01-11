package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.LootTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextFormatting;

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

    public static void init() {
        if (ConfigHandler.COMMON.enableWither.get())
            WITHERING = create("wither", PotionType.TARGET, Effects.WITHER).setAmplifier(0, 1).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enableSlowness.get())
            SLOWNESS = create("slowness", PotionType.TARGET, Effects.SLOWNESS).setAmplifier(1, 5).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enablePoison.get())
            POISON = create("poison", PotionType.TARGET, Effects.POISON).setAmplifier(0, 3).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enableWeakness.get())
            WEAKNESS = create("weakness", PotionType.TARGET, Effects.WEAKNESS).setAmplifier(0, 3).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enableBlindness.get())
            BLINDNESS = create("blindness", PotionType.TARGET, Effects.BLINDNESS).setAmplifier(0, 3).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.BOW);
        if (ConfigHandler.COMMON.enableRegeneration.get())
            REGENERATION = create("regeneration", PotionType.USER, Effects.REGENERATION).setAmplifier(0, 2).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableSpeed.get())
            SPEED = create("speed", PotionType.USER, Effects.SPEED).setAmplifier(0, 1).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableHaste.get())
            HASTE = create("haste", PotionType.USER, Effects.HASTE).setAmplifier(2, 4).setItemTypes(LootSet.LootSetType.AXE, LootSet.LootSetType.PICKAXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableResistance.get())
            RESISTANCE = create("resistance", PotionType.USER, Effects.RESISTANCE).setAmplifier(0, 2).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.SWORD, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
        if (ConfigHandler.COMMON.enableFireResistance.get())
            FIRE_RESISTANCE = create("fire_resistance", PotionType.USER, Effects.FIRE_RESISTANCE).setAmplifier(0, 0).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.SWORD, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_HELMET);
    }

    private final PotionType effectType;

    private String id;
    private Effect effect;

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

    public CompoundNBT getNbt(Random rand) {
        CompoundNBT tag = new CompoundNBT();

        tag.putString("id", this.getId());
        tag.putInt("duration", this.getDuration(rand));
        tag.putInt("amplifier", this.getAmplifier(rand));

        return tag;
    }

    public static int getDurationFromStack(ItemStack stack, String effectId) {
        int duration = 0;

        ListNBT effectTagList = LootNbtHelper.getLootTagList(stack, LootTags.LOOT_TAG_POTIONLIST);

        int count = effectTagList.size();

        for (int i = 0; i < count; ++i) {
            CompoundNBT e = effectTagList.getCompound(i);
            if (e.getString("id").contains(effectId)) {
                duration = e.getInt("duration");
            }
        }

        return duration;
    }

    public static int getAmplifierFromStack(ItemStack stack, String effectId) {
        int amplifier = 0;

        ListNBT effectTagList = LootNbtHelper.getLootTagList(stack, LootTags.LOOT_TAG_EFFECTLIST);

        int count = effectTagList.size();

        for (int i = 0; i < count; ++i) {
            CompoundNBT e = effectTagList.getCompound(i);
            if (e.getString("id").contains(effectId)) {
                amplifier = e.getInt("amplifier");
            }
        }

        return amplifier;
    }

    public static List<PotionEffect> getPotionlist(ItemStack stack) {
        List<PotionEffect> list = new ArrayList<PotionEffect>();

        ListNBT effectTagList = LootNbtHelper.getLootTagList(stack, LootTags.LOOT_TAG_POTIONLIST);

        int count = effectTagList.size();

        for (int i = 0; i < count; ++i) {
            CompoundNBT e = effectTagList.getCompound(i);
            list.add(PotionEffect.getById(e.getString("id")));
        }

        return list;
    }

    protected static PotionEffect create(String id, PotionType type, Effect effect) {
        PotionEffect weaponEffect = new PotionEffect(type);

        weaponEffect.id = id;
        weaponEffect.effect = effect;
        ;

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

    private int getDuration(Random rand) {
        int duration = this.durationMin;

        if (duration < this.durationMax)
            duration += rand.nextInt(this.durationMax - duration + 1);

        return duration;
    }

    private int getAmplifier(Random rand) {
        int amplifier = this.amplifierMin;

        if (amplifier < this.amplifierMax)
            amplifier += rand.nextInt(this.amplifierMax - amplifier + 1);

        return amplifier;
    }

    @Nullable
    public EffectInstance getPotionEffect(int duration, int amplifier) {
        if (this.effect == null)
            return null;

        EffectInstance weaponEffect = new EffectInstance(this.effect, duration, amplifier, true, false);
        return weaponEffect;
    }

    public void onHit(int duration, int amplifier, LivingEntity target, LivingEntity attacker) {
        EffectInstance instance = this.getPotionEffect(duration, amplifier);
        if (instance != null) {
            if (this.getType() == PotionType.USER)
                attacker.addPotionEffect(instance);
            else
                target.addPotionEffect(instance);
        }
    }

    public String getAmplifierString(ItemStack stack, String effectId) {
        return getAmplifierString(stack, effectId, 0);
    }

    public String getAmplifierString(ItemStack stack, String effectId, int add) {
        return TextFormatting.BOLD + "" + (getAmplifierFromStack(stack, effectId) + add) + "" + TextFormatting.RESET + "" + ((this.getType() == PotionType.USER) ? TextFormatting.GOLD : TextFormatting.RED) + "";
    }

    public String getDurationString(ItemStack stack, String effectId) {
        return TextFormatting.BOLD + "" + (getDurationFromStack(stack, effectId) / 100) + "" + TextFormatting.RESET + "" + TextFormatting.GREEN + "";
    }

    public enum PotionType {
        USER(TextFormatting.GOLD),
        TARGET(TextFormatting.RED);

        private TextFormatting color;

        private PotionType(TextFormatting color) {
            this.color = color;
        }

        public TextFormatting getColor() {
            return this.color;
        }

        public boolean equals(PotionType type) {
            return type == this;
        }
    }
}
