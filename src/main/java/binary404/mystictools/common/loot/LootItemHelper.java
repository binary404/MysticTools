package binary404.mystictools.common.loot;

import binary404.mystictools.common.core.RandomCollection;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.PotionEffect;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ToolType;

import java.util.*;

public class LootItemHelper {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");


    public static ItemStack getRandomLoot(Random rand, LootRarity rarity) {
        RandomCollection<Item> col = new RandomCollection<Item>(rand);

        col.add(3, ModItems.loot_sword);
        col.add(1, ModItems.loot_axe);
        col.add(1, ModItems.loot_pickaxe);

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

        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));

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

}
