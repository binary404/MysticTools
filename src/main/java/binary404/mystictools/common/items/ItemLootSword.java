package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLootSword extends SwordItem implements ILootItem {

    public ItemLootSword() {
        super(MysticTier.MYSTIC_TIER, 3, -2.4F, new Item.Properties().group(MysticTools.tab));
        /*
        this.addPropertyOverride(new ResourceLocation("model"), new IItemPropertyGetter() {
            @Override
            public float call(ItemStack p_call_1_, @Nullable World p_call_2_, @Nullable LivingEntity p_call_3_) {
                float model = 1.0F;

                model = LootNbtHelper.getLootIntValue(p_call_1_, LootTags.LOOT_TAG_MODEL);

                return model;
            }
        });
         */
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));

        return rarity == LootRarity.UNIQUE;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(LootItemHelper.getLootName(stack, super.getDisplayName(stack).getString()));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return LootItemHelper.getMaxDamage(stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean hit = super.hitEntity(stack, target, attacker);

        LootItemHelper.handlePotionEffects(stack, target, attacker);
        LootItemHelper.handleHit(stack, target, attacker);

        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));
        if (rarity == LootRarity.UNIQUE) {
            UniqueEffect.getUniqueEffect(stack).hit(target, attacker, stack);
        }

        return hit;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));
        if (rarity == LootRarity.UNIQUE) {
            UniqueEffect.getUniqueEffect(stack).tick(entityIn, stack);
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));
        if (rarity == LootRarity.UNIQUE) {
            UniqueEffect.getUniqueEffect(stack).rightClick(playerIn, stack);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {

        Multimap<Attribute, AttributeModifier> multiMap = HashMultimap.create();

        return LootItemHelper.modifiersForStack(slot, stack, multiMap, "Weapon modifier");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int attackDamage = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_DAMAGE);

        if (Screen.hasShiftDown()) {
            LootItemHelper.addInformation(stack, tooltip);
        }

        tooltip.add(new StringTextComponent(TextFormatting.RESET + "" + "Sword"));

        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "" + attackDamage + " Damage"));
    }
}
