package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.core.config.entry.AttributeOverride;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.effect.*;
import com.google.gson.annotations.Expose;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

public class LootEffect implements IEffect {
    public static final Map<String, LootEffect> REGISTRY = new HashMap<>();

    public static LootEffect LIGHTNING = create("lightning", EffectType.PASSIVE).setAction(LootEffects.LIGHTNING).setItemTypes(LootSet.LootSetType.SWORD);
    public static LootEffect LEECH = create("leech", EffectType.PASSIVE).setAmplifier(10, 100).setItemTypes(LootSet.LootSetType.SWORD);
    public static LootEffect HEAL = create("heal", EffectType.USE).setAction(LootEffects.HEAL).setItemTypes(LootSet.LootSetType.SWORD);
    public static LootEffect DASH = create("dash", EffectType.USE).setAction(LootEffects.DASH).setItemTypes(LootSet.LootSetType.SWORD);
    public static LootEffect STUN = create("stun", EffectType.USE).setAction(LootEffects.STUN).setItemTypes(LootSet.LootSetType.SWORD);
    public static LootEffect POTION_CLOUD = create("potion_cloud", EffectType.PASSIVE).setAction(LootEffects.POTION_CLOUD).setItemTypes(LootSet.LootSetType.SWORD);
    public static LootEffect BLAST = create("blast", EffectType.USE).setAction(LootEffects.BLAST).setItemTypes(LootSet.LootSetType.SWORD);

    public static LootEffect REACH = create("reach", EffectType.PASSIVE, new AttributeOverride(ForgeMod.REACH_DISTANCE.get())).setAmplifier(1, 5).setItemTypes(LootSet.LootSetType.SWORD, LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL);

    public static LootEffect SLEEP = create("sleep", EffectType.USE).setAction(LootEffects.SLEEP).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL);
    public static LootEffect MULTI = create("multi", EffectType.PASSIVE).setItemTypes(LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.PICKAXE);
    public static LootEffect AREA_MINER = create("area_miner", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.AXE).setAction(LootEffects.AREA_MINER);
    public static LootEffect AUTO_SMELT = create("auto_smelt", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.AXE).setAction(LootEffects.AUTOSMELT);
    public static LootEffect VOID = create("void", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL).setAction(LootEffects.VOID);

    public static LootEffect JUMP = create("jump", EffectType.PASSIVE).setAmplifier(2, 5).setItemTypes(LootSet.LootSetType.ARMOR_BOOTS);
    public static LootEffect PARRY = create("parry", EffectType.PASSIVE).setAmplifier(2.5, 20).setItemTypes(LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_HELMET);

    public static LootEffect SHOCKWAVE = create("shockwave", EffectType.PASSIVE).setAction(LootEffects.SHOCKWAVE).setItemTypes(LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_CHESTPLATE);
    public static LootEffect REFLECT = create("reflect", EffectType.PASSIVE).setItemTypes(LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_LEGGINGS);
    public static LootEffect HEALTH = create("health", EffectType.PASSIVE, new AttributeOverride(Attributes.MAX_HEALTH)).setAmplifier(2, 10).setItemTypes(LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_HELMET, LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL);
    public static LootEffect KNOCKBACK_RESISTANCE = create("knockback_resistance", EffectType.PASSIVE, new AttributeOverride(Attributes.KNOCKBACK_RESISTANCE)).setAmplifier(0.05, 0.3).setItemTypes(LootSet.LootSetType.ARMOR_CHESTPLATE, LootSet.LootSetType.ARMOR_LEGGINGS, LootSet.LootSetType.ARMOR_BOOTS, LootSet.LootSetType.ARMOR_HELMET);

    public static LootEffect INSIGHT = create("insight", EffectType.PASSIVE).setAmplifier(1, 6).setItemTypes(LootSet.LootSetType.ARMOR_HELMET);

    public static void init() {
        //NO-OP
    }

    @Expose
    private String id;
    @Expose
    private List<LootSet.LootSetType> applyToItems = new ArrayList<>();
    @Expose
    private AttributeOverride attribute;
    @Expose
    private LootEffects action;

    @Expose
    private double amplifierMin = 0;
    @Expose
    private double amplifierMax = 0;

    @Expose
    private EffectType effectType;

    public enum EffectType {
        ACTIVE(ChatFormatting.GOLD),
        PASSIVE(ChatFormatting.GREEN),
        USE(ChatFormatting.AQUA);

        private ChatFormatting color;

        private EffectType(ChatFormatting color) {
            this.color = color;
        }

        public ChatFormatting getColor() {
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
        if (this.action != null)
            return this.action.action;
        return null;
    }

    protected LootEffect setAction(LootEffects action) {
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

    public static double getAmplifierFromStack(ItemStack stack, String effectId) {
        double amplifier = 0;

        List<LootEffectInstance> effects = ModAttributes.LOOT_EFFECTS.getOrDefault(stack, new ArrayList<>()).getValue(stack);

        for (LootEffectInstance instance : effects) {
            if (instance.getId().contains(effectId)) {
                amplifier = instance.getAmplifier();
            }
        }

        return amplifier;
    }

    public static List<LootEffectInstance> getEffectList(ItemStack stack) {
        List<LootEffectInstance> list = ModAttributes.LOOT_EFFECTS.getOrDefault(stack, new ArrayList<>()).getValue(stack);

        return list;
    }

    protected static LootEffect create(String id, EffectType type) {
        LootEffect effect = new LootEffect(type);

        effect.id = id;
        REGISTRY.put(id, effect);
        return effect;
    }

    protected static LootEffect create(String id, EffectType type, AttributeOverride attribute) {
        LootEffect effect = new LootEffect(type);

        effect.id = id;
        effect.attribute = attribute;

        return effect;
    }

    protected LootEffect setAmplifier(double min, double max) {
        this.amplifierMin = min;
        this.amplifierMax = max;
        return this;
    }

    public double getAmplifier(Random rand) {
        return Mth.nextDouble(rand, this.amplifierMin, this.amplifierMax);
    }

    public AttributeOverride getAttribute() {
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
        return ChatFormatting.BOLD + "" + (getAmplifierFromStack(stack, effectId) + add) + "" + ChatFormatting.RESET + "" + ((this.getType() == EffectType.PASSIVE) ? ChatFormatting.GREEN : ChatFormatting.GOLD) + "";
    }


}
