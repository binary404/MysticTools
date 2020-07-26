package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemLootAxe extends AxeItem implements ILootItem {

    public ItemLootAxe() {
        super(ItemTier.DIAMOND, 5.0F, -3.0F, new Item.Properties().group(MysticTools.tab));

        this.addPropertyOverride(new ResourceLocation("model"), new IItemPropertyGetter() {
            @Override
            public float call(ItemStack p_call_1_, @Nullable World p_call_2_, @Nullable LivingEntity p_call_3_) {
                float model = 0F;

                model = LootNbtHelper.getLootIntValue(p_call_1_, LootTags.LOOT_TAG_MODEL);

                return model;
            }
        });
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));

        return rarity == LootRarity.UNIQUE;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new StringTextComponent(LootItemHelper.getLootName(stack, super.getDisplayName(stack).getString()));
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
        LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(stack, LootTags.LOOT_TAG_RARITY));
        if (rarity == LootRarity.UNIQUE) {
            UniqueEffect.getUniqueEffect(stack).breakBlock(pos, player.world, player, stack);
        }

        boolean onBreak = super.onBlockStartBreak(stack, pos, player);

        if (LootItemHelper.hasEffect(stack, LootEffect.AREA_MINER) && LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL) > 1) {
            RayTraceResult raytrace = LootItemHelper.getBlockOnReach(player.world, player);
            if (raytrace != null) {
                int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL);
                onBreak = LootItemHelper.breakBlocks(stack, level, player.world, pos, ((BlockRayTraceResult) raytrace).getFace(), player);
            }
        }

        return onBreak;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity && isSelected)
            LootItemHelper.handlePotionEffects(stack, null, (LivingEntity) entityIn);
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

        return LootItemHelper.use(super.onItemRightClick(worldIn, playerIn, handIn), worldIn, playerIn, handIn);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {

        Multimap<String, AttributeModifier> multiMap = super.getAttributeModifiers(slot, stack);

        return LootItemHelper.modifiersForStack(slot, stack, multiMap, "Tool modifier");
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return LootItemHelper.getEfficiency(stack, state);
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        if (LootItemHelper.hasEffect(stack, LootEffect.MULTI)) {
            return Sets.newHashSet(ToolType.AXE, ToolType.PICKAXE, ToolType.SHOVEL);
        }
        return Sets.newHashSet(ToolType.AXE);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        if (LootItemHelper.hasEffect(stack, LootEffect.MULTI)) {
            return 3;
        }
        return super.getHarvestLevel(stack, tool, player, blockState);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return LootItemHelper.getMaxDamage(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            LootItemHelper.addInformation(stack, tooltip);
        }

        tooltip.add(new StringTextComponent(TextFormatting.RESET + "" + "Axe"));

        float efficiency = LootNbtHelper.getLootFloatValue(stack, LootTags.LOOT_TAG_EFFICIENCY);
        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "" + ItemStack.DECIMALFORMAT.format(efficiency) + " Mining Speed"));
    }
}
