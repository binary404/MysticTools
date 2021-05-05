package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.effect.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

public class LootEffect implements IEffect {
    public static final Map<String, LootEffect> REGISTRY = new HashMap<>();

    public static LootEffect LIGHTNING;
    public static LootEffect LEECH;
    public static LootEffect HEAL;
    public static LootEffect DASH;
    public static LootEffect STUN;

    public static LootEffect SLEEP;
    public static LootEffect MULTI;
    public static LootEffect AREA_MINER;
    public static LootEffect AUTO_SMELT;
    public static LootEffect VOID;

    public static LootEffect JUMP;
    public static LootEffect PARRY;

    public static LootEffect SHOCKWAVE;
    public static LootEffect REFLECT;
    public static LootEffect HEALTH;
    public static LootEffect KNOCKBACK_RESISTANCE;

    public static LootEffect INSIGHT;

    public static void init() {
        if (ConfigHandler.COMMON.enableLightning.get())
            LIGHTNING = create("lightning", EffectType.PASSIVE).setAction(new LootEffectLightning()).setItemTypes(LootSet.LootSetType.SWORD);
        if (ConfigHandler.COMMON.enableLeach.get())
            LEECH = create("leech", EffectType.PASSIVE).setAmplifier(1, ConfigHandler.COMMON.maxLeachAmount.get()).setItemTypes(LootSet.LootSetType.SWORD);
        if (ConfigHandler.COMMON.enableHeal.get())
            HEAL = create("heal", EffectType.USE).setAction(new LootEffectHeal()).setItemTypes(LootSet.LootSetType.SWORD);
        if (ConfigHandler.COMMON.enableDash.get())
            DASH = create("dash", EffectType.USE).setAction(new LootEffectDash()).setItemTypes(LootSet.LootSetType.SWORD);
        if (ConfigHandler.COMMON.enableStun.get())
            STUN = create("stun", EffectType.USE).setAction(new LootEffectStun()).setItemTypes(LootSet.LootSetType.SWORD);
        if (ConfigHandler.COMMON.enableSleep.get())
            SLEEP = create("sleep", EffectType.USE).setAction(new LootEffectActionSleep()).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL);
        if (ConfigHandler.COMMON.enableMultiTool.get())
            MULTI = create("multi", EffectType.PASSIVE).setItemTypes(LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.PICKAXE);
        if (ConfigHandler.COMMON.enableAreaMiner.get())
            AREA_MINER = create("area_miner", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.AXE).setAction(new LootEffectAreaMiner());
        if (ConfigHandler.COMMON.enableAutoSmelt.get())
            AUTO_SMELT = create("auto_smelt", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.AXE).setAction(new LootEffectAutoSmelt());
        if (ConfigHandler.COMMON.enableVoid.get())
            VOID = create("void", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL).setAction(new LootEffectVoid());
        if (ConfigHandler.COMMON.enableJump.get())
            JUMP = create("jump", EffectType.PASSIVE).setAmplifier(2, ConfigHandler.COMMON.maxJumpAmount.get()).setItemTypes(LootSet.LootSetType.ARMOR_BOOTS);
        if (ConfigHandler.COMMON.enableParry.get())
            PARRY = create("parry", EffectType.PASSIVE).setAmplifier(10, ConfigHandler.COMMON.maxParryChance.get()).setItemTypes(LootSet.LootSetType.ARMOR_BOOTS);
        if (ConfigHandler.COMMON.enableShockwave.get())
            SHOCKWAVE = create("shockwave", EffectType.PASSIVE).setAction(new LootEffectShockwave()).setItemTypes(LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE);
        if (ConfigHandler.COMMON.enableReflect.get())
            REFLECT = create("reflect", EffectType.PASSIVE).setItemTypes(LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_LEGGINGS);
        if (ConfigHandler.COMMON.enableHealth.get())
            HEALTH = create("health", EffectType.PASSIVE, Attributes.MAX_HEALTH).setAmplifier(1, ConfigHandler.COMMON.maxHealth.get()).setItemTypes(LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE);
        if (ConfigHandler.COMMON.enableKnockbackResistance.get())
            KNOCKBACK_RESISTANCE = create("knockback_resistance", EffectType.PASSIVE, Attributes.KNOCKBACK_RESISTANCE).setAmplifier(1, ConfigHandler.COMMON.maxKnockbackResistance.get()).setItemTypes(LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_LEGGINGS);
        if(ConfigHandler.COMMON.enableInsight.get())
            INSIGHT = create("insight", EffectType.PASSIVE).setAmplifier(1, ConfigHandler.COMMON.maxInsight.get()).setItemTypes(LootSet.LootSetType.ARMOR_HELMET);
    }

    private String id;
    private List<LootSet.LootSetType> applyToItems = new ArrayList<>();

    private Attribute attribute;
    private IEffectAction action;

    private int amplifierMin = 0;
    private int amplifierMax = 0;

    private final EffectType effectType;

    public enum EffectType {
        ACTIVE(TextFormatting.GOLD),
        PASSIVE(TextFormatting.GREEN),
        USE(TextFormatting.AQUA);

        private TextFormatting color;

        private EffectType(TextFormatting color) {
            this.color = color;
        }

        public TextFormatting getColor() {
            return this.color;
        }

        public boolean equals(EffectType type) {
            return type == this;
        }
    }

    private LootEffect(EffectType type) {
        this.effectType = type;
    }

    public EffectType getType() {
        return this.effectType;
    }

    public boolean applyToItemType(LootSet.LootSetType type) {
        return applyToItems.contains(type);
    }

    public IEffectAction getAction() {
        return this.action;
    }

    protected LootEffect setAction(IEffectAction action) {
        this.action = action;
        return this;
    }

    protected LootEffect setItemTypes(LootSet.LootSetType... itemTypes) {
        for (LootSet.LootSetType itemType : itemTypes) {
            applyToItems.add(itemType);
        }
        return this;
    }

    public static LootEffect getById(String id) {
        LootEffect weaponEffect = REGISTRY.get(id);

        return weaponEffect;
    }

    public String getId() {
        return this.id;
    }

    public CompoundNBT getNbt(Random rand) {
        CompoundNBT tag = new CompoundNBT();

        tag.putString("id", this.getId());
        tag.putInt("amplifier", this.getAmplifier(rand));
        return tag;
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

    public static List<LootEffect> getEffectList(ItemStack stack) {
        List<LootEffect> list = new ArrayList<>();

        ListNBT effectList = LootNbtHelper.getLootTagList(stack, LootTags.LOOT_TAG_EFFECTLIST);

        int count = effectList.size();

        for (int i = 0; i < count; ++i) {
            CompoundNBT e = effectList.getCompound(i);
            list.add(LootEffect.getById(e.getString("id")));
        }

        return list;
    }

    protected static LootEffect create(String id, EffectType type) {
        LootEffect effect = new LootEffect(type);

        effect.id = id;
        REGISTRY.put(id, effect);
        return effect;
    }

    protected static LootEffect create(String id, EffectType type, Attribute attribute) {
        LootEffect effect = new LootEffect(type);

        effect.id = id;
        effect.attribute = attribute;
        REGISTRY.put(id, effect);

        return effect;
    }

    protected LootEffect setAmplifier(int min, int max) {
        this.amplifierMin = min;
        this.amplifierMax = max;
        return this;
    }

    private int getAmplifier(Random rand) {
        return MathHelper.nextInt(rand, this.amplifierMin, this.amplifierMax);
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public String getActionStatus(ItemStack stack) {
        String statusString = "";

        if (this.getAction() != null) {
            statusString = this.getAction().getStatusString(stack);
        }

        return statusString;
    }

    public String getAmplifierString(ItemStack stack, String effectId) {
        return getAmplifierString(stack, effectId, 0);
    }

    public String getAmplifierString(ItemStack stack, String effectId, int add) {
        return TextFormatting.BOLD + "" + (getAmplifierFromStack(stack, effectId) + add) + "" + TextFormatting.RESET + "" + ((this.getType() == EffectType.PASSIVE) ? TextFormatting.GREEN : TextFormatting.GOLD) + "";
    }

}
