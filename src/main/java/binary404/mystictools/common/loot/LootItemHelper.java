package binary404.mystictools.common.loot;

import binary404.mystictools.common.loot.effects.BasicEffect;
import binary404.mystictools.common.loot.effects.IEffect;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import java.util.*;

public class LootItemHelper {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    protected static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

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

            /*
            List<LootWeaponEffect> effects = LootWeaponEffect.getEffectList(stack);

            String uuid_string = LootItemHelper.getLootStringValue(stack, LOOT_TAG_UUID);

            if (uuid_string.length() > 0) {
                for (LootWeaponEffect effect : effects) {
                    if (effect.getAttribute() != null) {
                        modifiers.put(effect.getAttribute().getName(), new AttributeModifier(UUID.fromString(uuid_string), "Equipment modifier", (double) LootWeaponEffect.getAmplifierFromStack(stack, effect.getId()), 0));
                    }
                }
            }
             */
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

    public static void handleBasicEffectsAfterHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.getHealth() <= 0.0) {
            int kills = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_KILLS);
            kills++;
            if (kills >= LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_LEVEL))
                LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_LEVEL, LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_LEVEL) * 2);
            LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_KILLS, kills);
        }

        List<BasicEffect> effects = BasicEffect.getEffectList(stack);

        if (effects.size() > 0) {
            for (BasicEffect effect : effects) {
                effect.onHit(BasicEffect.getDurationFromStack(stack, effect.getId()), BasicEffect.getAmplifierFromStack(stack, effect.getId()), target, attacker);
            }
        }
    }

    public static BasicEffect getRandomExcluding(Random rand, LootSet.LootSetType type, List<BasicEffect> exclude) {
        BasicEffect effect = null;

        List<BasicEffect> list = new ArrayList<>();
        for (BasicEffect e : BasicEffect.REGISTRY.values()) {
            if (e.applyToItemType(type)) {
                list.add(e);
            }
        }

        list.removeAll(exclude);

        if (list.size() > 0)
            effect = list.get(rand.nextInt(list.size()));

        return effect;
    }

}
