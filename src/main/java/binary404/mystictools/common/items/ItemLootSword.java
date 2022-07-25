package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class ItemLootSword extends SwordItem implements ILootItem {

    public ItemLootSword() {
        super(MysticTier.MYSTIC_TIER, 0, -2.4F, new Item.Properties().tab(MysticTools.tab));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));

        return rarity == LootRarity.UNIQUE || super.isFoil(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(LootItemHelper.getLootName(stack, super.getName(stack).getString()));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return LootItemHelper.getMaxDamage(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean hit = super.hurtEnemy(stack, target, attacker);

        LootItemHelper.handlePotionEffects(stack, target, attacker);
        LootItemHelper.handleHit(stack, target, attacker);

        LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));
        if (rarity == LootRarity.UNIQUE) {
            UniqueEffect.getUniqueEffect(stack).hit(target, attacker, stack, ModAttributes.LOOT_DAMAGE.getOrDefault(stack, 1.0).getValue(stack));
        }

        return hit;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));
        if (rarity == LootRarity.UNIQUE) {
            UniqueEffect.getUniqueEffect(stack).tick(entityIn, stack);
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);
        LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));
        if (rarity == LootRarity.UNIQUE) {
            UniqueEffect.getUniqueEffect(stack).rightClick(playerIn, stack);
        }

        return LootItemHelper.use(super.use(worldIn, playerIn, handIn), worldIn, playerIn, handIn);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return LootItemHelper.modifiersForStack(slot, stack, super.getAttributeModifiers(slot, stack));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && enchantment != Enchantments.MENDING;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        double attackDamage = ModAttributes.LOOT_DAMAGE.getOrDefault(stack, 1.0).getValue(stack);
        float speedDisplay = ModAttributes.LOOT_SPEED.getOrDefault(stack, 1.0f).getValue(stack);
        double speed = (double) speedDisplay;


        Player player = MysticTools.proxy.getPlayer();

        if (player != null) {
            speed += player.getAttribute(Attributes.ATTACK_SPEED).getBaseValue();
            attackDamage += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        }

        if (Screen.hasShiftDown()) {
            LootItemHelper.addInformation(stack, tooltip);
        }

        tooltip.add(Component.literal(ChatFormatting.RESET + "" + "Sword"));

        tooltip.add(Component.literal(ChatFormatting.GRAY + "" + attackDamage + " Damage | " + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(speed) + " Attack Speed"));
    }
}
