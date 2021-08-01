package binary404.mystictools.common.loot;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.util.RandomCollection;
import binary404.mystictools.common.items.ItemLootArmor;
import binary404.mystictools.common.items.ItemLootAxe;
import binary404.mystictools.common.items.ItemLootSword;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import com.google.common.collect.Multimap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
import net.minecraftforge.common.ForgeInternalHandler;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.*;

public class LootItemHelper {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    public static ItemStack getRandomLoot(Random rand) {
        RandomCollection<Item> col = new RandomCollection<Item>(rand);

        col.add(2, ModItems.loot_sword);
        col.add(3, ModItems.loot_axe);
        col.add(3, ModItems.loot_pickaxe);
        col.add(3, ModItems.loot_shovel);
        col.add(2, ModItems.loot_bow);
        col.add(1, ModItems.loot_boots);
        col.add(1, ModItems.loot_leggings);
        col.add(1, ModItems.loot_chestplate);
        col.add(1, ModItems.loot_helmet);

        ItemStack stack = new ItemStack(col.next());

        return stack;
    }

    public static LootSet.LootSetType getItemType(Item item) {
        return ItemTypeRegistry.get(item);
    }

    public static String getLootName(ItemStack stack, String current) {
        String displayName = current;

        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));
        String name = LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_NAME);

        displayName = name;

        if (rarity != null)
            displayName = rarity.getColor() + displayName;

        return displayName;
    }

    public static int getMaxDamage(ItemStack stack) {
        int maxDamage = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_DURABILITY);

        if (maxDamage == 0)
            maxDamage = 100;

        return maxDamage;
    }

    public static Multimap<Attribute, AttributeModifier> modifiersForStack(EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> initial, String modifierKey) {
        return modifiersForStack(slot, EquipmentSlot.MAINHAND, stack, initial, modifierKey);
    }

    public static Multimap<Attribute, AttributeModifier> modifiersForStack(EquipmentSlot slot, EquipmentSlot effectiveSlot, ItemStack stack, Multimap<Attribute, AttributeModifier> initial, String modifierKey) {
        Multimap<Attribute, AttributeModifier> modifiers = initial;

        if (slot == effectiveSlot) {
            int attackDamage = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_DAMAGE);
            float attackSpeed = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_SPEED);
            float armorPoints = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_ARMOR);
            float armorToughness = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_TOUGHNESS);

            if (attackDamage > 0 && stack.getItem() instanceof ItemLootSword)
                applyAttributeModifier(modifiers, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER, modifierKey, (double) attackDamage);

            if (attackSpeed > 0 && !(stack.getItem() instanceof ArmorItem))
                applyAttributeModifier(modifiers, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, modifierKey, (double) attackSpeed);

            if (armorPoints > 0 && stack.getItem() instanceof ItemLootArmor)
                applyAttributeModifier(modifiers, Attributes.ARMOR, ARMOR_MODIFIERS[slot.getIndex()], modifierKey, (double) armorPoints);

            if (armorToughness > 0 && stack.getItem() instanceof ItemLootArmor)
                applyAttributeModifier(modifiers, Attributes.ARMOR_TOUGHNESS, ARMOR_MODIFIERS[slot.getIndex()], modifierKey, (double) armorToughness);

            List<LootEffect> effects = LootEffect.getEffectList(stack);

            String uuid_string = LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_UUID);

            if (uuid_string.length() > 0) {
                for(LootEffect effect : effects) {
                    if(effect != null) {
                        if(effect.getAttribute() != null) {
                            modifiers.put(effect.getAttribute(), new AttributeModifier(UUID.fromString(uuid_string), "EquipmentModifier", LootEffect.getAmplifierFromStack(stack, effect.getId()), AttributeModifier.Operation.ADDITION));
                        }
                    }
                }
            }
        }

        return modifiers;
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
        if (target != null && target.getHealth() <= 0.0) {

            List<LootEffect> effects = LootEffect.getEffectList(stack);

            if (effects.size() > 0) {
                for (LootEffect effect : effects) {
                    if (effect.getAction() != null)
                        effect.getAction().handleHit(stack, target, attacker);
                }
            }

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

        List<LootEffect> effects = LootEffect.getEffectList(stack);

        if (effects != null) {
            for (LootEffect effect : effects) {
                if (effect != null) {
                    IEffectAction action = effect.getAction();

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

        List<LootEffect> effects = LootEffect.getEffectList(stack);

        effect_check:
        for (LootEffect e : effects) {
            if (e == effect) {
                hasEffect = true;
                break effect_check;
            }
        }

        return hasEffect;
    }

    public static void handlePotionEffects(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        List<PotionEffect> effects = PotionEffect.getPotionlist(stack);

        if (effects.size() > 0) {
            for (PotionEffect effect : effects) {
                effect.onHit(PotionEffect.getDurationFromStack(stack, effect.getId()), PotionEffect.getAmplifierFromStack(stack, effect.getId()), target, attacker);
            }
        }
    }

    public static PotionEffect getRandomPotionExcluding(Random rand, LootSet.LootSetType type, List<PotionEffect> exclude) {
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

    public static float getEfficiency(ItemStack stack, BlockState state) {
        float speed = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_EFFICIENCY);

        for (ToolType type : stack.getItem().getToolTypes(stack)) {
            Material material = state.getMaterial();
            if (state.getBlock().isToolEffective(state, type)
                    || (type == ToolType.PICKAXE && (material == Material.METAL || material == Material.HEAVY_METAL || material == Material.STONE))
                    || (type == ToolType.AXE && (material == Material.WOOD || material == Material.PLANT || material == Material.LEAVES))) {
                return speed;
            }
        }

        return 1.0f;
    }

    public static void addInformation(ItemStack stack, List<Component> tooltip) {
        addInformation(stack, tooltip, true);
    }

    public static void addInformation(ItemStack stack, List<Component> tooltip, boolean show_durability) {
        int durability = stack.getMaxDamage();

        List<PotionEffect> effects = PotionEffect.getPotionlist(stack);
        for (PotionEffect effect : effects) {
            tooltip.add(new TextComponent(
                    ChatFormatting.RESET
                            + "- " + effect.getType().getColor()
                            + I18n.get("weaponeffect." + effect.getId() + ".description",
                            new Object[]{
                                    effect.getDurationString(stack, effect.getId()),
                                    effect.getAmplifierString(stack, effect.getId()),
                                    effect.getAmplifierString(stack, effect.getId(), 1)})));
        }
        List<LootEffect> effects1 = LootEffect.getEffectList(stack);
        for (LootEffect effect : effects1) {
            tooltip.add(new TextComponent(
                    ChatFormatting.RESET + "- " + effect.getType().getColor() + I18n.get("weaponeffect." + effect.getId() + ".description", new Object[]{
                            effect.getAmplifierString(stack, effect.getId()),
                            effect.getAmplifierString(stack, effect.getId(), 1)
                    })
            ));
        }

        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));

        if (rarity == LootRarity.UNIQUE) {
            String effect = I18n.get(stack.getTag().getCompound(LootTags.LOOT_TAG).getCompound(LootTags.LOOT_TAG_UNIQUE).getString("id") + ".description");

            tooltip.add(new TextComponent("+ " + ChatFormatting.ITALIC + "" + ChatFormatting.DARK_PURPLE + "" + effect));
        }

        if (rarity != null)
            tooltip.add(new TextComponent("Rarity: " + rarity.getColor() + I18n.get(rarity.getId())));

        if (show_durability)
            tooltip.add(new TextComponent(ChatFormatting.RESET + "" + durability + "" + ChatFormatting.GRAY + " Durability"));

        int xp = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_XP);
        if (xp > 0)
            tooltip.add(new TextComponent(ChatFormatting.RED + "" + xp + " Xp/" + LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_LEVEL)));

        int modifiers = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_UPGRADE);
        if (modifiers > 0)
            tooltip.add(new TextComponent(ChatFormatting.BOLD + "" + ChatFormatting.GOLD + "" + modifiers + " Modifiers"));
    }

    @Nullable
    public static LootEffect getRandomEffectExcluding(Random rand, LootSet.LootSetType type, List<LootEffect> exclude) {
        LootEffect weaponEffect = null;

        boolean hasActive = false;
        boolean hasUse = false;

        for (LootEffect ex : exclude) {
            if (ex.getType() == LootEffect.EffectType.ACTIVE)
                hasActive = true;

            if (ex.getType() == LootEffect.EffectType.USE)
                hasUse = true;
        }

        List<LootEffect> list = new ArrayList<LootEffect>();

        for (LootEffect e : LootEffect.REGISTRY.values()) {
            if (e.applyToItemType(type)) {
                if (
                        !(hasActive && e.getType() == LootEffect.EffectType.ACTIVE)
                                && !(hasUse && e.getType() == LootEffect.EffectType.USE))
                    list.add(e);
            }
        }

        list.removeAll(exclude);

        if (list.size() > 0)
            weaponEffect = list.get(rand.nextInt(list.size()));

        return weaponEffect;
    }

    public static ItemStack rerollStats(ItemStack stack) {
        Random random = new Random();

        LootRarity lootRarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));

        LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_DAMAGE, lootRarity.getDamage(random));
        LootNbtHelper.setLootFloatValue(stack, LootTags.LOOT_TAG_SPEED, lootRarity.getSpeed(random));
        LootNbtHelper.setLootFloatValue(stack, LootTags.LOOT_TAG_EFFICIENCY, lootRarity.getEfficiency(random));
        LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_DURABILITY, lootRarity.getDurability(random));

        LootNbtHelper.setLootFloatValue(stack, LootTags.LOOT_TAG_DRAWSPEED, lootRarity.getSpeed(random) + 4.0F);
        LootNbtHelper.setLootFloatValue(stack, LootTags.LOOT_TAG_POWER, 1.0F + ((float) lootRarity.getDamage(random) / 20.0F));

        return stack;
    }

    public static ItemStack generateLoot(LootRarity lootRarity, LootSet.LootSetType type, ItemStack loot) {
        Random random = new Random();

        CompoundTag tag = new CompoundTag();
        tag.putInt("HideFlags", 2);
        CompoundTag lootTag = new CompoundTag();

        int model = 1 + random.nextInt(type.models);

        lootTag.putInt(LootTags.LOOT_TAG_MODEL, model);

        lootTag.putString(LootTags.LOOT_TAG_UUID, UUID.randomUUID().toString());

        lootTag.putString(LootTags.LOOT_TAG_RARITY, lootRarity.getId());
        lootTag.putInt(LootTags.LOOT_TAG_DAMAGE, lootRarity.getDamage(random));
        lootTag.putFloat(LootTags.LOOT_TAG_SPEED, lootRarity.getSpeed(random));
        lootTag.putFloat(LootTags.LOOT_TAG_EFFICIENCY, lootRarity.getEfficiency(random));
        lootTag.putInt(LootTags.LOOT_TAG_DURABILITY, lootRarity.getDurability(random));
        lootTag.putInt(LootTags.LOOT_TAG_LEVEL, 10);
        lootTag.putInt(LootTags.LOOT_TAG_UPGRADE, 0);

        lootTag.putFloat(LootTags.LOOT_TAG_DRAWSPEED, lootRarity.getSpeed(random) + 4.0F);
        lootTag.putFloat(LootTags.LOOT_TAG_POWER, 1.0F + ((float) lootRarity.getDamage(random) / 20.0F));

        if (loot.getItem() instanceof ItemLootArmor) {
            lootTag.putFloat(LootTags.LOOT_TAG_ARMOR, lootRarity.getArmor(random));
            lootTag.putFloat(LootTags.LOOT_TAG_TOUGHNESS, lootRarity.getToughness(random));
        }

        int modifierCount = lootRarity.getPotionCount(random);

        boolean unbreakable = false;

        if (lootRarity == LootRarity.UNIQUE)
            unbreakable = true;

        if (modifierCount > 0) {
            List<PotionEffect> appliedEffects = new ArrayList<>();
            ListTag effectList = new ListTag();

            for (int m = 0; m < modifierCount; m++) {
                PotionEffect effect = LootItemHelper.getRandomPotionExcluding(random, type, appliedEffects);

                if (effect != null) {
                    effectList.add(effect.getNbt(random));
                    appliedEffects.add(effect);
                } else {
                }
            }
            if (lootRarity != LootRarity.COMMON)
                if (random.nextInt(100) > 90)
                    unbreakable = true;
            lootTag.put(LootTags.LOOT_TAG_POTIONLIST, effectList);
        }

        modifierCount = lootRarity.getEffectCount(random);

        if (modifierCount > 0) {
            List<LootEffect> appliedEffects = new ArrayList<>();
            ListTag effectList = new ListTag();

            for (int m = 0; m < modifierCount; ++m) {
                LootEffect me = LootItemHelper.getRandomEffectExcluding(random, type, appliedEffects);

                if (me != null) {
                    effectList.add(me.getNbt(random));
                    appliedEffects.add(me);
                }
            }
            lootTag.put(LootTags.LOOT_TAG_EFFECTLIST, effectList);
        }

        tag.put(LootTags.LOOT_TAG, lootTag);

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

        switch (level) {
            case 1: //2x2
                xradN = 0;
                xradP = 1;
                yradN = 1;
                yradP = 0;
                zradN = 0;
                zradP = 0;
                break;
            case 2: //3x3
                xradN = 1;
                xradP = 1;
                yradN = 1;
                yradP = 1;
                zradN = 0;
                zradP = 0;
                break;
            case 3: //4x4
                xradN = 1;
                xradP = 2;
                yradN = 1;
                yradP = 2;
                zradN = 0;
                zradP = 0;
                break;
            case 4: //5x5
                xradN = 2;
                xradP = 2;
                yradN = 2;
                yradP = 2;
                zradN = 0;
                zradP = 0;
                break;
            default:
                xradN = 0;
                xradP = 0;
                yradN = 0;
                yradP = 0;
                zradN = 0;
                zradP = 0;
                break;
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

        if (level == 4 && side.getAxis() != Direction.Axis.Y) {
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

        boolean canHarvest =
                (ForgeHooks.canHarvestBlock(state, player, world, pos) || stack.getItem().isCorrectToolForDrops(state))
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

            if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                block.destroy(world, pos, state);
                block.playerDestroy(world, player, pos, state, tileEntity, stack);
                if (world instanceof ServerLevel)
                    block.popExperience((ServerLevel) world, pos, xp);
            }

            ((ServerPlayer) player).connection.send(new ClientboundBlockUpdatePacket(world, pos));
            return true;
        } else {
            world.levelEvent(2001, pos, Block.getId(state));

            if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                block.destroy(world, pos, state);
            }

            stack.mineBlock(world, state, pos, player);

            return true;
        }
    }

}
