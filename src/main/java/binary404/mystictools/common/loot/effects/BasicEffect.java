package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.LootTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.*;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;

public class BasicEffect implements IEffect {
    public static final Map<String, BasicEffect> REGISTRY = new HashMap<>();


    public static final BasicEffect WITHERING = create("wither", BasicType.TARGET, Effects.WITHER).setItemTypes(LootSet.LootSetType.SWORD);
    public static final BasicEffect SLOWNESS = create("slowness", BasicType.TARGET, Effects.SLOWNESS).setItemTypes(LootSet.LootSetType.SWORD);

    public static final BasicEffect REGENERATION = create("regeneration", BasicType.USER, Effects.REGENERATION).setItemTypes(LootSet.LootSetType.SWORD);
    public static final BasicEffect SPEED = create("speed", BasicType.USER, Effects.SPEED).setItemTypes(LootSet.LootSetType.SWORD);

    private final BasicType effectType;

    private String id;
    private Effect effect;

    private List<LootSet.LootSetType> applyToItems = new ArrayList<LootSet.LootSetType>();

    private int durationMin = 100;
    private int durationMax = 300;
    private int amplifierMin = 0;
    private int amplifierMax = 0;

    private BasicEffect(BasicType type) {
        this.effectType = type;
    }

    public BasicType getType() {
        return this.effectType;
    }

    public boolean applyToItemType(LootSet.LootSetType type) {
        return applyToItems.contains(type);
    }

    protected BasicEffect setItemTypes(LootSet.LootSetType... itemTypes) {
        for (LootSet.LootSetType type : itemTypes) {
            applyToItems.add(type);
        }

        return this;
    }

    @Nullable
    public static BasicEffect getById(String id) {
        BasicEffect effect = REGISTRY.get(id);

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

        ListNBT effectTagList = LootNbtHelper.getLootTagList(stack, LootTags.LOOT_TAG_EFFECTLIST);

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

    public static List<BasicEffect> getEffectList(ItemStack stack) {
        List<BasicEffect> list = new ArrayList<BasicEffect>();

        ListNBT effectTagList = LootNbtHelper.getLootTagList(stack, LootTags.LOOT_TAG_EFFECTLIST);

        int count = effectTagList.size();

        for (int i = 0; i < count; ++i) {
            CompoundNBT e = effectTagList.getCompound(i);
            list.add(BasicEffect.getById(e.getString("id")));
        }

        return list;
    }

    protected static BasicEffect create(String id, BasicType type) {
        return create(id, type, null);
    }

    protected static BasicEffect create(String id, BasicType type, Effect effect) {
        BasicEffect weaponEffect = new BasicEffect(type);

        weaponEffect.id = id;
        weaponEffect.effect = effect;
        ;

        REGISTRY.put(id, weaponEffect);

        return weaponEffect;
    }

    protected BasicEffect setDuration(int min, int max) {
        this.durationMin = min * 100;
        this.durationMax = max * 100;
        return this;
    }

    protected BasicEffect setAmplifier(int min, int max) {
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
            if (this.getType() == BasicType.USER)
                attacker.addPotionEffect(instance);
            else
                target.addPotionEffect(instance);
        }
    }

    public enum BasicType {
        USER(TextFormatting.GOLD),
        TARGET(TextFormatting.RED);

        private TextFormatting color;

        private BasicType(TextFormatting color) {
            this.color = color;
        }

        public TextFormatting getColor() {
            return this.color;
        }

        public boolean equals(BasicType type) {
            return type == this;
        }
    }
}
