package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.effect.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class LootEffect implements IEffect {
    public static final Map<String, LootEffect> REGISTRY = new HashMap<>();

    public static final LootEffect LIGHTNING = create("lightning", EffectType.PASSIVE).setAction(new LootEffectLightning()).setItemTypes(LootSet.LootSetType.SWORD);
    public static final LootEffect LEECH = create("leech", EffectType.PASSIVE).setAmplifier(1, 100).setItemTypes(LootSet.LootSetType.SWORD);

    public static final LootEffect SLEEP = create("sleep", EffectType.USE).setAction(new LootEffectActionSleep()).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL);
    public static final LootEffect MULTI = create("multi", EffectType.PASSIVE).setItemTypes(LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.PICKAXE);
    public static final LootEffect AREA_MINER = create("area_miner", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.AXE).setAction(new LootEffectAreaMiner());
    public static final LootEffect AUTO_SMELT = create("auto_smelt", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.SHOVEL, LootSet.LootSetType.AXE).setAction(new LootEffectAutoSmelt());
    public static final LootEffect VOID = create("void", EffectType.ACTIVE).setItemTypes(LootSet.LootSetType.PICKAXE, LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL).setAction(new LootEffectVoid());

    private String id;
    private List<LootSet.LootSetType> applyToItems = new ArrayList<>();

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

    protected LootEffect setAmplifier(int min, int max) {
        this.amplifierMin = min;
        this.amplifierMax = max;
        return this;
    }

    private int getAmplifier(Random rand) {
        return MathHelper.nextInt(rand, this.amplifierMin, this.amplifierMax);
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
