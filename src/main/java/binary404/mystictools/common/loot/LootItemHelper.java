package binary404.mystictools.common.loot;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.util.RandomCollection;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.*;

public class LootItemHelper {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");


    public static ItemStack getRandomLoot(Random rand, LootRarity rarity) {
        RandomCollection<Item> col = new RandomCollection<Item>(rand);

        col.add(3, ModItems.loot_sword);
        col.add(2, ModItems.loot_axe);
        col.add(2, ModItems.loot_pickaxe);
        col.add(2, ModItems.loot_shovel);
        col.add(1, ModItems.loot_bow);

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

    public static Multimap<String, AttributeModifier> modifiersForStack(EquipmentSlotType slot, ItemStack stack, Multimap<String, AttributeModifier> initial, String modifierKey) {
        return modifiersForStack(slot, EquipmentSlotType.MAINHAND, stack, initial, modifierKey);
    }

    public static Multimap<String, AttributeModifier> modifiersForStack(EquipmentSlotType slot, EquipmentSlotType effectiveSlot, ItemStack stack, Multimap<String, AttributeModifier> initial, String modifierKey) {
        Multimap<String, AttributeModifier> modifiers = initial;

        if (slot == effectiveSlot) {
            int attackDamage = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_DAMAGE);
            float attackSpeed = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_SPEED);
            float armorPoints = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_ARMOR);
            float armorToughness = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_TOUGHNESS);

            //MysticTools armor TODO
            if (attackDamage > 0 && !(stack.getItem() instanceof ArmorItem))
                applyAttributeModifier(modifiers, SharedMonsterAttributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER, modifierKey, (double) attackDamage);

            if (attackSpeed > 0 && !(stack.getItem() instanceof ArmorItem))
                applyAttributeModifier(modifiers, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, modifierKey, (double) attackSpeed);

            if (armorPoints > 0)
                applyAttributeModifier(modifiers, SharedMonsterAttributes.ARMOR, ARMOR_MODIFIERS[slot.getIndex()], modifierKey, (double) armorPoints);

            if (armorToughness > 0)
                applyAttributeModifier(modifiers, SharedMonsterAttributes.ARMOR_TOUGHNESS, ARMOR_MODIFIERS[slot.getIndex()], modifierKey, (double) armorToughness);
        }

        return modifiers;
    }

    private static void applyAttributeModifier(Multimap<String, AttributeModifier> modifiers, IAttribute attribute, UUID uuid, String modifierKey, double value) {
        Collection<AttributeModifier> curModifiers = new ArrayList<AttributeModifier>();
        double attributeValue = 0;

        curModifiers.clear();
        curModifiers.addAll(modifiers.get(attribute.getName()));

        for (AttributeModifier m : curModifiers)
            attributeValue += m.getAmount();

        modifiers.removeAll(attribute.getName());
        modifiers.put(attribute.getName(), new AttributeModifier(uuid, modifierKey, value + attributeValue, AttributeModifier.Operation.ADDITION));
    }

    public static void handleHit(ItemStack stack, LivingEntity target) {
        if (target != null && target.getHealth() <= 0.0) {
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

    public static void handleBreak(ItemStack stack, PlayerEntity player, BlockPos pos) {
        if (player.world.rand.nextInt(10) <= 5) {
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

    public static ActionResult<ItemStack> use(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItemMainhand();

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
                    || (type == ToolType.PICKAXE && (material == Material.IRON || material == Material.ANVIL || material == Material.ROCK))
                    || (type == ToolType.AXE && (material == Material.WOOD || material == Material.PLANTS || material == Material.LEAVES))) {
                return speed;
            }
        }

        return 1.0f;
    }

    public static void addInformation(ItemStack stack, List<ITextComponent> tooltip) {
        addInformation(stack, tooltip, true);
    }

    public static void addInformation(ItemStack stack, List<ITextComponent> tooltip, boolean show_durability) {
        int durability = stack.getMaxDamage();

        List<PotionEffect> effects = PotionEffect.getPotionlist(stack);
        for (PotionEffect effect : effects) {
            tooltip.add(new StringTextComponent(
                    TextFormatting.RESET
                            + "- " + effect.getType().getColor()
                            + I18n.format("weaponeffect." + effect.getId() + ".description",
                            new Object[]{
                                    effect.getDurationString(stack, effect.getId()),
                                    effect.getAmplifierString(stack, effect.getId()),
                                    effect.getAmplifierString(stack, effect.getId(), 1)})));
        }
        List<LootEffect> effects1 = LootEffect.getEffectList(stack);
        for (LootEffect effect : effects1) {
            tooltip.add(new StringTextComponent(
                    TextFormatting.RESET + "- " + effect.getType().getColor() + I18n.format("weaponeffect." + effect.getId() + ".description", new Object[]{
                            effect.getAmplifierString(stack, effect.getId()),
                            effect.getAmplifierString(stack, effect.getId(), 1)
                    })
            ));
        }

        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));

        if (rarity == LootRarity.UNIQUE) {
            String effect = I18n.format(stack.getTag().getCompound(LootTags.LOOT_TAG).getCompound(LootTags.LOOT_TAG_UNIQUE).getString("id") + ".description");

            tooltip.add(new StringTextComponent("+ " + TextFormatting.ITALIC + "" + TextFormatting.DARK_PURPLE + "" + effect));
        }

        if (rarity != null)
            tooltip.add(new StringTextComponent("Rarity: " + rarity.getColor() + rarity.getId()));

        if (show_durability)
            tooltip.add(new StringTextComponent(TextFormatting.RESET + "" + durability + "" + TextFormatting.GRAY + " Durability"));

        int xp = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_XP);
        if (xp > 0)
            tooltip.add(new StringTextComponent(TextFormatting.RED + "" + xp + " Xp/" + LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_LEVEL)));

        int modifiers = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_UPGRADE);
        if (modifiers > 0)
            tooltip.add(new StringTextComponent(TextFormatting.BOLD + "" + modifiers + " Modifiers"));
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

        CompoundNBT tag = new CompoundNBT();
        tag.putInt("HideFlags", 2);
        CompoundNBT lootTag = new CompoundNBT();

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

        int modifierCount = lootRarity.getPotionCount(random);

        boolean unbreakable = false;

        if (lootRarity == LootRarity.UNIQUE)
            unbreakable = true;

        if (modifierCount > 0) {
            List<PotionEffect> appliedEffects = new ArrayList<>();
            ListNBT effectList = new ListNBT();

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
            ListNBT effectList = new ListNBT();

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

    public static RayTraceResult getBlockOnReach(World world, PlayerEntity player) {
        double distance = player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();

        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        double x = player.getPosX();
        double y = player.getPosY() + (double) player.getEyeHeight();
        double z = player.getPosZ();

        Vec3d vec3 = new Vec3d(x, y, z);

        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;

        Vec3d vec31 = vec3.add((double) f6 * distance, (double) f5 * distance, (double) f7 * distance);


        return world.rayTraceBlocks(new RayTraceContext(vec3, vec31, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));
    }

    public static boolean breakBlocks(ItemStack stack, int level, World world, BlockPos aPos, Direction side, PlayerEntity player) {
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
        float mainHardness = state.getBlockHardness(world, aPos);

        if (!tryHarvestBlock(world, aPos, false, stack, player)) {
            return false;
        }

        if (level == 4 && side.getAxis() != Direction.Axis.Y) {
            aPos = aPos.up();
            BlockState theState = world.getBlockState(aPos);
            if (theState.getBlockHardness(world, aPos) <= mainHardness + 5.0F) {
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
                            if (theState.getBlockHardness(world, thePos) <= mainHardness + 5.0F) {
                                tryHarvestBlock(world, thePos, true, stack, player);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean tryHarvestBlock(World world, BlockPos pos, boolean isExtra, ItemStack stack, PlayerEntity player) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        float hardness = state.getBlockHardness(world, pos);

        boolean canHarvest =
                (ForgeHooks.canHarvestBlock(state, player, world, pos) || stack.getItem().canHarvestBlock(state))
                        && (!isExtra || stack.getItem().getDestroySpeed(stack, world.getBlockState(pos)) > 1.0F);

        if (hardness >= 0.0F && (!isExtra || (canHarvest && !block.hasTileEntity(world.getBlockState(pos))))) {
            return breakExtraBlock(stack, world, player, pos);
        }
        return false;
    }

    public static boolean breakExtraBlock(ItemStack stack, World world, PlayerEntity player, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (player.abilities.isCreativeMode) {
            if (block.removedByPlayer(state, world, pos, player, false, world.getFluidState(pos))) {
                block.onPlayerDestroy(world, pos, state);
            }

            if (!world.isRemote) {
                ((ServerPlayerEntity) player).connection.sendPacket(new SChangeBlockPacket(world, pos));
            }

            return true;
        }

        stack.onBlockDestroyed(world, state, pos, player);

        if (!world.isRemote) {
            int xp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).interactionManager.getGameType(), (ServerPlayerEntity) player, pos);
            if (xp == -1)
                return false;

            TileEntity tileEntity = world.getTileEntity(pos);

            if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                block.onPlayerDestroy(world, pos, state);
                block.harvestBlock(world, player, pos, state, tileEntity, stack);
                block.dropXpOnBlockBreak(world, pos, xp);
            }

            ((ServerPlayerEntity) player).connection.sendPacket(new SChangeBlockPacket(world, pos));
            return true;
        } else {
            world.playEvent(2001, pos, Block.getStateId(state));

            if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                block.onPlayerDestroy(world, pos, state);
            }

            stack.onBlockDestroyed(world, state, pos, player);

            return true;
        }
    }

}
