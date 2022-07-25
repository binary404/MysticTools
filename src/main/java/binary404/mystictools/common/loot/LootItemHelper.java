package binary404.mystictools.common.loot;

import binary404.mystictools.common.core.config.ModConfigs;
import binary404.mystictools.common.core.config.entry.ItemEntry;
import binary404.mystictools.common.core.util.WeightedList;
import binary404.mystictools.common.items.ItemLootArmor;
import binary404.mystictools.common.items.ItemLootSword;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.effects.*;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.phys.Vec3;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;

public class LootItemHelper {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    private static final UUID[] ARMOR_TOUGHNESS_MODIFIERS = new UUID[]{UUID.fromString("b972ad47-fa70-496b-aebf-7fda5cd071aa"), UUID.fromString("3f788515-def1-4300-9e2a-df95a505cc0b"), UUID.fromString("018d3528-e366-4d1b-a7d2-54ff503f4e74"), UUID.fromString("d42d1c71-99ad-45ef-b1f6-98f90c6b0c7b")};

    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    public static final Set<ToolAction> digActions = Set.of(ToolActions.AXE_DIG, ToolActions.HOE_DIG, ToolActions.PICKAXE_DIG, ToolActions.SHOVEL_DIG);

    public static ItemStack getRandomLoot(RandomSource rand) {

        ItemEntry itemEntry = ModConfigs.ITEMS.ITEMS.getRandom(rand);

        ItemStack stack = new ItemStack(itemEntry.toItem());

        return stack;
    }

    public static LootSet.LootSetType getItemType(Item item) {
        return ItemTypeRegistry.get(item);
    }

    public static String getLootName(ItemStack stack, String current) {
        String displayName = current;

        LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));
        String name = LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_NAME);

        displayName = name;

        if (rarity != null)
            displayName = rarity.getColor() + displayName;

        return displayName;
    }

    public static int getMaxDamage(ItemStack stack) {
        int maxDamage = ModAttributes.LOOT_DURABILITY.getOrDefault(stack, 0).getValue(stack);

        if (maxDamage == 0)
            maxDamage = 100;

        return maxDamage;
    }

    public static int getEffectLevel(ItemStack stack) {
        int effectLevel = ModAttributes.LOOT_EFFECT_LEVEL.getOrDefault(stack, 0).getValue(stack);

        return effectLevel;
    }

    public static void setEffectLevel(ItemStack stack, int level) {
        ModAttributes.LOOT_EFFECT_LEVEL.create(stack, level);
    }

    public static Multimap<Attribute, AttributeModifier> modifiersForStack(EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> initial) {
        return modifiersForStack(slot, EquipmentSlot.MAINHAND, stack, initial);
    }

    public static Multimap<Attribute, AttributeModifier> modifiersForStack(EquipmentSlot slot, EquipmentSlot effectiveSlot, ItemStack stack, Multimap<Attribute, AttributeModifier> initial) {
        Multimap<Attribute, AttributeModifier> modifiableMap = LinkedHashMultimap.create(initial);

        if (slot == effectiveSlot) {
            double attackDamage = ModAttributes.LOOT_DAMAGE.getOrDefault(stack, 0.0).getValue(stack);
            float attackSpeed = ModAttributes.LOOT_SPEED.getOrDefault(stack, 0.0f).getValue(stack);
            double armorPoints = ModAttributes.LOOT_ARMOR.getOrDefault(stack, 0.0).getValue(stack);
            double armorToughness = ModAttributes.LOOT_TOUGHNESS.getOrDefault(stack, 0.0).getValue(stack);

            System.out.println(attackDamage);

            if (attackDamage > 0 && stack.getItem() instanceof ItemLootSword)
                applyAttributeModifier(modifiableMap, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackDamage);
            if (attackSpeed > 0)
                applyAttributeModifier(modifiableMap, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, "Weapon modifier", attackSpeed);
            if (armorPoints > 0 && stack.getItem() instanceof ItemLootArmor)
                applyAttributeModifier(modifiableMap, Attributes.ARMOR, ARMOR_MODIFIERS[slot.getIndex()], "Armor modifier", armorPoints);
            if (armorToughness > 0 && stack.getItem() instanceof ItemLootArmor)
                applyAttributeModifier(modifiableMap, Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS_MODIFIERS[slot.getIndex()], "Armor toughness", armorToughness);
/*            if (attribute == Attributes.ATTACK_DAMAGE && attackDamage > 1 && stack.getItem() instanceof ItemLootSword) {
                modifiableMap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(modifier.getId(), "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ATTACK_SPEED && attackSpeed > 1 && stack.getItem() instanceof ItemLootSword) {
                modifiableMap.put(Attributes.ATTACK_SPEED, new AttributeModifier(modifier.getId(), "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ARMOR && armorPoints > 1 && stack.getItem() instanceof ItemLootArmor) {
                modifiableMap.put(Attributes.ARMOR, new AttributeModifier(modifier.getId(), "Armor modifier", armorPoints, AttributeModifier.Operation.ADDITION));
            } else if (attribute == Attributes.ARMOR_TOUGHNESS && armorToughness > 1 && stack.getItem() instanceof ItemLootArmor) {
                modifiableMap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(modifier.getId(), "Armor toughness", armorToughness, AttributeModifier.Operation.ADDITION));
            } else {
                modifiableMap.put(attribute, modifier);
            }*/

            List<LootEffectInstance> effects = LootEffect.getEffectList(stack);

            for (LootEffectInstance effect : effects) {
                if (effect != null) {
                    if (effect.getEffect().getAttribute() != null) {
                        UUID uuid = itemHash(stack.getItem(), effect.getEffect().getAttribute().salt);
                        modifiableMap.put(effect.getEffect().getAttribute().getAttribute(), new AttributeModifier(uuid, effect.getEffect().getAttribute().modifierType, LootEffect.getAmplifierFromStack(stack, effect.getId()), AttributeModifier.Operation.ADDITION));
                    }
                }
            }
        }

        return modifiableMap;
    }

    static UUID itemHash(Item item, long salt) {
        return new UUID(salt, item.hashCode());
    }

    private static void applyAttributeModifier(Multimap<Attribute, AttributeModifier> modifiers, Attribute attribute, UUID uuid, String modifierKey, double value) {
        Collection<AttributeModifier> curModifiers = new ArrayList<AttributeModifier>();
        double attributeValue = 0;

        curModifiers.clear();
        curModifiers.addAll(modifiers.get(attribute));

        for (AttributeModifier m : curModifiers)
            attributeValue += m.getAmount();

        modifiers.removeAll(attribute);
        modifiers.put(attribute, new AttributeModifier(uuid, modifierKey, value + attributeValue, AttributeModifier.Operation.ADDITION));
    }

    public static void handleHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target != null) {

            List<LootEffectInstance> effects = LootEffect.getEffectList(stack);

            if (effects.size() > 0) {
                for (int i = 0; i < effects.size(); i++) {
                    LootEffectInstance effect = effects.get(i);
                    if (effect.getEffect() != null && effect.getEffect().getAction() != null)
                        effect.getEffect().getAction().handleHit(stack, target, attacker, i);
                }
            }

            if (target.getHealth() <= 0.0) {
                //Only level up on kill
                int kills = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_XP);
                kills++;
                int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_LEVEL);
                if (kills >= level) {
                    level *= 1.5;
                    kills = 0;
                    int upgrades = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_UPGRADE);
                    LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_LEVEL, level);
                    LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_UPGRADE, (upgrades + 1));
                }
                LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_XP, kills);
            }
        }
    }

    public static void handleBreak(ItemStack stack, Player player, BlockPos pos) {
        if (player.level.random.nextInt(10) <= 5) {
            int xp = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_XP);
            xp++;
            int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_LEVEL);
            if (xp >= level) {
                level *= 1.5;
                xp = 0;
                int upgrades = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_UPGRADE);
                LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_LEVEL, level);
                LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_UPGRADE, (upgrades + 1));
            }
            LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_XP, xp);
        }
    }

    public static InteractionResultHolder<ItemStack> use(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        List<LootEffectInstance> effects = LootEffect.getEffectList(stack);

        if (effects != null) {
            for (LootEffectInstance effect : effects) {
                if (effect != null) {
                    IEffectAction action = effect.getEffect().getAction();

                    if (action != null) {
                        defaultAction = action.handleUse(defaultAction, world, player, hand);
                    }
                }
            }
        }

        return defaultAction;
    }

    public static boolean hasEffect(ItemStack stack, LootEffect effect) {
        boolean hasEffect = false;

        List<LootEffectInstance> effects = LootEffect.getEffectList(stack);

        effect_check:
        for (LootEffectInstance e : effects) {
            if (e.getEffect() == effect) {
                hasEffect = true;
                break effect_check;
            }
        }

        return hasEffect;
    }

    public static void handlePotionEffects(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        List<PotionEffectInstance> effects = PotionEffect.getPotionlist(stack);

        if (effects.size() > 0) {
            for (PotionEffectInstance effect : effects) {
                //effect.getEffect().onHit(PotionEffect.getDurationFromStack(stack, effect.getId()), PotionEffect.getAmplifierFromStack(stack, effect.getId()), target, attacker);
            }
        }
    }

    public static PotionEffect getRandomPotionExcluding(RandomSource rand, LootSet.LootSetType type, List<PotionEffect> exclude) {
        PotionEffect effect = null;

        List<PotionEffect> list = new ArrayList<>();
        for (PotionEffect e : PotionEffect.REGISTRY.values()) {
            if (e.applyToItemType(type)) {
                list.add(e);
            }
        }

        list.removeAll(exclude);

        if (list.size() > 0)
            effect = list.get(rand.nextInt(list.size()));

        return effect;
    }

    public static PotionEffect getRandomPotionEffectExcluding(Random rand, LootSet.LootSetType type, List<PotionEffectInstance> exclude) {
        PotionEffect effect = null;

        List<PotionEffect> list = new ArrayList<>();
        List<MobEffect> effects = new ArrayList<>();

        for (PotionEffectInstance instance : exclude) {
            effects.add(instance.getEffect());
        }

        for (PotionEffect e : PotionEffect.REGISTRY.values()) {
            if (e.applyToItemType(type) && !effects.contains(e.getEffect())) {
                list.add(e);
            }
        }

        if (list.size() > 0)
            effect = list.get(rand.nextInt(list.size()));

        return effect;
    }

    public static UniqueEffect getRandomUnique(Random rand, LootSet.LootSetType type) {
        UniqueEffect effect = null;

        List<UniqueEffect> list = new ArrayList<>();
        for (UniqueEffect e : UniqueEffect.REGISTRY.values()) {
            if (e.applyToItemType(type)) {
                list.add(e);
            }
        }

        if (list.size() > 0)
            effect = list.get(rand.nextInt(list.size()));

        return effect;
    }

    public static void addInformation(ItemStack stack, List<Component> tooltip) {
        addInformation(stack, tooltip, true);
    }

    public static void addInformation(ItemStack stack, List<Component> tooltip, boolean show_durability) {
        int durability = stack.getMaxDamage();

/*        List<PotionEffectInstance> effects = PotionEffect.getPotionlist(stack);
        for (PotionEffectInstance effect : effects) {
            tooltip.add(new TextComponent(
                    ChatFormatting.RESET
                            + "- " + effect.getEffect().getType().getColor()
                            + I18n.get("weaponeffect." + effect.getId() + ".description",
                            new Object[]{
                                    effect.getEffect().getDurationString(stack, effect.getId()),
                                    effect.getEffect().getAmplifierString(stack, effect.getId()),
                                    effect.getEffect().getAmplifierString(stack, effect.getId(), 1)})));
        }*/
        List<LootEffectInstance> effects1 = LootEffect.getEffectList(stack);
        for (int i = 0; i < effects1.size(); i++) {
            LootEffectInstance effect = effects1.get(i);
            if (effect.getEffect().getType() != LootEffect.EffectType.ACTIVE) {
                String additionalTranslation = "";
                if (effect.getEffect().getAction() != null)
                    additionalTranslation = effect.getEffect().getAction().getAdditionalTooltip(stack, i);
                tooltip.add(Component.literal(
                        ChatFormatting.RESET + "- " + effect.getEffect().getType().getColor() + I18n.get("weaponeffect." + effect.getId() + ".description", new Object[]{
                                effect.getEffect().getAmplifierString(stack, effect.getId()), additionalTranslation
                        })
                ));
            } else {
                tooltip.add(Component.literal(
                        ChatFormatting.RESET + "- " + effect.getEffect().getType().getColor() + I18n.get("weaponeffect." + effect.getId() + ".description", new Object[]{
                                LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE)
                        })
                ));
            }
        }


        LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));

        if (rarity.getId().equals("unique")) {
            String effect = I18n.get(stack.getTag().getCompound(LootTags.LOOT_TAG).getCompound(LootTags.LOOT_TAG_UNIQUE).getString("id") + ".description");

            tooltip.add(Component.literal("+ " + ChatFormatting.ITALIC + "" + ChatFormatting.DARK_PURPLE + "" + effect));
        }



        tooltip.add(Component.literal("Rarity: " + rarity.getColor() + I18n.get(rarity.getId())));

        if (show_durability)
            tooltip.add(Component.literal(ChatFormatting.RESET + "" + durability + "" + ChatFormatting.GRAY + " Durability"));


    }

    static Component toolTipDots(int amount, ChatFormatting formatting) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            text.append("\u2b22 ");
        }
        return Component.literal(text.toString()).withStyle(formatting);
    }

    public static LootEffect getNextEffect(RandomSource rand, LootSet.LootSetType type, LootRarity rarity, List<LootEffect> existing) {
        LootEffect effectResult = null;

        boolean hasActive = false;
        boolean hasUse = false;

        for (LootEffect ex : existing) {
            if (ex.getType() == LootEffect.EffectType.ACTIVE)
                hasActive = true;
            if (ex.getType() == LootEffect.EffectType.USE)
                hasUse = true;
        }

        WeightedList<LootEffect> list = new WeightedList<>();
        List<LootEffect> possibleEffects = rarity.getPossibleEffects();

        for (WeightedList.Entry<LootEffect> entry : ModConfigs.EFFECTS.EFFECTS) {
            LootEffect effect = entry.value;
            if (effect.applyToItemType(type)) {
                if (!(hasActive && effect.getType() == LootEffect.EffectType.ACTIVE) && !(hasUse && effect.getType() == LootEffect.EffectType.USE) && possibleEffects.contains(effect) && effect.isCompatible(existing)) {
                    list.add(effect, entry.weight);
                }
            }
        }

        if (list.size() > 0)
            effectResult = list.getRandom(rand);

        return effectResult;
    }

    @Nullable
    public static LootEffect getRandomEffectExcluding(RandomSource rand, LootSet.LootSetType type, LootRarity rarity, List<LootEffect> exclude) {
        LootEffect weaponEffect = null;

        boolean hasActive = false;
        boolean hasUse = false;

        for (LootEffect ex : exclude) {
            if (ex.getType() == LootEffect.EffectType.ACTIVE)
                hasActive = true;

            if (ex.getType() == LootEffect.EffectType.USE)
                hasUse = true;
        }

        WeightedList<LootEffect> list = new WeightedList<>();
        List<LootEffect> possibleEffects = rarity.getPossibleEffects();

        for (WeightedList.Entry<LootEffect> entry : ModConfigs.EFFECTS.EFFECTS) {
            LootEffect effect = entry.value;
            if (effect.applyToItemType(type)) {
                if (!(hasActive && effect.getType() == LootEffect.EffectType.ACTIVE) && !(hasUse && effect.getType() == LootEffect.EffectType.USE) && possibleEffects.contains(effect) && !exclude.contains(effect)) {
                    list.add(effect, entry.weight);
                }
            }
        }

        if (list.size() > 0)
            weaponEffect = list.getRandom(rand);

        return weaponEffect;
    }

    public static ItemStack generateLoot(LootRarity lootRarity, LootSet.LootSetType type, ItemStack loot) {
        RandomSource random = RandomSource.create();

        int model = 1 + random.nextInt(type.models);

        ModAttributes.LOOT_MODEL.create(loot, model);
        ModAttributes.LOOT_UUID.create(loot, UUID.randomUUID().toString());

        ModAttributes.LOOT_RARITY.create(loot, lootRarity.getId());
        ModAttributes.LOOT_DAMAGE.create(loot, lootRarity.getDamage(random));
        ModAttributes.LOOT_SPEED.create(loot, lootRarity.getSpeed(random));
        ModAttributes.LOOT_EFFICIENCY.create(loot, lootRarity.getEfficiency(random));
        ModAttributes.LOOT_DURABILITY.create(loot, lootRarity.getDurability(random));

        ModAttributes.LOOT_DRAWSPEED.create(loot, lootRarity.getSpeed(random) + 4.0);
        ModAttributes.LOOT_POWER.create(loot, 1.0 + ((float) lootRarity.getDamage(random) / 20.0));

        if (loot.getItem() instanceof ItemLootArmor) {
            ModAttributes.LOOT_ARMOR.create(loot, lootRarity.getArmor(random));
            ModAttributes.LOOT_TOUGHNESS.create(loot, lootRarity.getToughness(random));
        }

        int modifierCount = lootRarity.getPotionCount(random);

        boolean unbreakable = lootRarity.getUnbreakableChance() > random.nextInt(100);

        /*if (modifierCount > 0) {
            List<PotionEffect> appliedEffects = new ArrayList<>();
            List<PotionEffectInstance> instances = new ArrayList<>();

            for (int m = 0; m < modifierCount; m++) {
                PotionEffect effect = LootItemHelper.getRandomPotionExcluding(random, type, appliedEffects);

                if (effect != null) {
                    instances.add(new PotionEffectInstance(effect.getEffect(), effect.getDuration(random), effect.getAmplifier(random)));
                    appliedEffects.add(effect);
                } else {
                }
            }
            ModAttributes.LOOT_POTION_EFFECTS.create(loot, instances);
        }*/

        modifierCount = lootRarity.getEffectCount(random);

        if (modifierCount > 0) {
            List<LootEffect> appliedEffects = new ArrayList<>();
            List<LootEffectInstance> instances = new ArrayList<>();

            for (int m = 0; m < modifierCount; ++m) {
                LootEffect me = LootItemHelper.getNextEffect(random, type, lootRarity, appliedEffects);

                if (me != null) {
                    instances.add(new LootEffectInstance(me));
                    appliedEffects.add(me);
                    if (me.getAction() != null) {
                        me.getAction().rollExtra(loot, type, random);
                        LootNbtHelper.setLootAdditionalData(loot, String.valueOf(m), me.getAction().addAdditionalData(loot, type, random));
                    }
                }
            }
            ModAttributes.LOOT_EFFECTS.create(loot, instances);
        }

        //tag.put(LootTags.LOOT_TAG, lootTag);

        CompoundTag tag = loot.getTag();
        tag.putInt("HideFlags", 2);

        if (unbreakable) {
            tag.putBoolean("Unbreakable", true);
        }

        loot.setTag(tag);

        String loot_name = LootSet.getNameForType(type, random);

        if (loot_name.length() > 0) {
            LootNbtHelper.setLootStringValue(loot, LootTags.LOOT_TAG_NAME, loot_name);
        }
        return loot;
    }

    public static HitResult getBlockOnReach(Level world, Player player) {
        double distance = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();

        float pitch = player.xRotO;
        float yaw = player.yRotO;
        double x = player.getX();
        double y = player.getY() + (double) player.getEyeHeight();
        double z = player.getZ();

        Vec3 vec3 = new Vec3(x, y, z);

        float f2 = Mth.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = Mth.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f4 = -Mth.cos(-pitch * 0.017453292F);
        float f5 = Mth.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;

        Vec3 vec31 = new Vec3((double) f6 * distance + vec3.x, (double) f5 * distance + vec3.y, (double) f7 * distance + vec3.z);


        return world.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
    }

    public static boolean breakBlocks(ItemStack stack, int level, Level world, BlockPos aPos, Direction side, Player player) {
        int xradN = 0;
        int xradP = 0;
        int yradN = 0;
        int yradP = 0;
        int zradN = 0;
        int zradP = 0;

        System.out.println(level);

        switch (level) {
            case 1 -> { //3x3
                xradN = 1;
                xradP = 1;
                yradN = 1;
                yradP = 1;
            }
            case 2 -> { //5x5
                xradN = 2;
                xradP = 2;
                yradN = 2;
                yradP = 2;
            }
            default -> {
                xradN = 0;
                xradP = 0;
                yradN = 0;
                yradP = 0;
                zradN = 0;
                zradP = 0;
            }
        }

        if (side.getAxis() == Direction.Axis.Y) {
            zradN = xradN;
            zradP = xradP;
            yradN = 0;
            yradP = 0;
        }

        if (side.getAxis() == Direction.Axis.X) {
            xradN = 0;
            xradP = 0;
            zradN = yradN;
            zradP = yradP;
        }

        BlockState state = world.getBlockState(aPos);
        float mainHardness = state.getDestroySpeed(world, aPos);

        if (!tryHarvestBlock(world, aPos, false, stack, player)) {
            return false;
        }

        if (level == 2 && side.getAxis() != Direction.Axis.Y) {
            aPos = aPos.above();
            BlockState theState = world.getBlockState(aPos);
            if (theState.getDestroySpeed(world, aPos) <= mainHardness + 5.0F) {
                tryHarvestBlock(world, aPos, true, stack, player);
            }
        }

        if (level > 0 && mainHardness >= 0.2F) {
            for (int xPos = aPos.getX() - xradN; xPos <= aPos.getX() + xradP; xPos++) {
                for (int yPos = aPos.getY() - yradN; yPos <= aPos.getY() + yradP; yPos++) {
                    for (int zPos = aPos.getZ() - zradN; zPos <= aPos.getZ() + zradP; zPos++) {
                        if (!(aPos.getX() == xPos && aPos.getY() == yPos && aPos.getZ() == zPos)) {
                            BlockPos thePos = new BlockPos(xPos, yPos, zPos);
                            BlockState theState = world.getBlockState(thePos);
                            if (theState.getDestroySpeed(world, thePos) <= mainHardness + 5.0F) {
                                tryHarvestBlock(world, thePos, true, stack, player);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean tryHarvestBlock(Level world, BlockPos pos, boolean isExtra, ItemStack stack, Player player) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        float hardness = state.getDestroySpeed(world, pos);

        boolean canHarvest = (ForgeHooks.isCorrectToolForDrops(state, player) || stack.getItem().isCorrectToolForDrops(state))
                && (!isExtra || stack.getItem().getDestroySpeed(stack, world.getBlockState(pos)) > 1.0F);

        if (hardness >= 0.0F && (!isExtra || (canHarvest))) {
            return breakExtraBlock(stack, world, player, pos);
        }
        return false;
    }

    public static boolean breakExtraBlock(ItemStack stack, Level world, Player player, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        stack.mineBlock(world, state, pos, player);

        if (!world.isClientSide) {
            int xp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayer) player).gameMode.getGameModeForPlayer(), (ServerPlayer) player, pos);
            if (xp == -1)
                return false;

            BlockEntity tileEntity = world.getBlockEntity(pos);

            if (block.onDestroyedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                block.destroy(world, pos, state);
                block.playerDestroy(world, player, pos, state, tileEntity, stack);
                if (world instanceof ServerLevel)
                    block.popExperience((ServerLevel) world, pos, xp);
            }

            ((ServerPlayer) player).connection.send(new ClientboundBlockUpdatePacket(world, pos));
            return true;
        } else {
            world.levelEvent(2001, pos, Block.getId(state));

            if (block.onDestroyedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                block.destroy(world, pos, state);
            }

            stack.mineBlock(world, state, pos, player);

            return true;
        }
    }

}
