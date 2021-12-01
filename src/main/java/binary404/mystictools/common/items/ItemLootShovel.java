package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.UniqueEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemLootShovel extends ShovelItem implements ILootItem {

    public ItemLootShovel() {
        super(MysticTier.MYSTIC_TIER, 1.5F, -3.0F, new Item.Properties().tab(MysticTools.tab));

        /*
        this.addPropertyOverride(new ResourceLocation("model"), new IItemPropertyGetter() {
            @Override
            public float call(ItemStack p_call_1_, @Nullable World p_call_2_, @Nullable LivingEntity p_call_3_) {
                float model = 1.0F;

                model = (float) LootNbtHelper.getLootIntValue(p_call_1_, LootTags.LOOT_TAG_MODEL);

                return model;
            }
        });
        */
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));

        return rarity == LootRarity.UNIQUE;
    }

    @Override
    public Component getName(ItemStack stack) {
        return new TextComponent(LootItemHelper.getLootName(stack, super.getName(stack).getString()));
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
        LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(stack, "common").getValue(stack));
        if (rarity == LootRarity.UNIQUE) {
            UniqueEffect.getUniqueEffect(stack).breakBlock(pos, player.level, player, stack);
        }

        boolean onBreak = super.onBlockStartBreak(stack, pos, player);

        LootItemHelper.handleBreak(stack, player, pos);

        if (LootItemHelper.hasEffect(stack, LootEffect.AREA_MINER) && LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL) > 1) {
            HitResult raytrace = LootItemHelper.getBlockOnReach(player.level, player);
            if (raytrace != null) {
                int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL);
                onBreak = LootItemHelper.breakBlocks(stack, level, player.level, pos, ((BlockHitResult) raytrace).getDirection(), player);
            }
        }

        return onBreak;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity && isSelected)
            LootItemHelper.handlePotionEffects(stack, null, (LivingEntity) entityIn);
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

        Multimap<Attribute, AttributeModifier> multiMap = HashMultimap.create();

        return LootItemHelper.modifiersForStack(slot, stack, multiMap, "Tool modifier");
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return (float) LootItemHelper.getEfficiency(stack, state);
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        if (LootItemHelper.hasEffect(stack, LootEffect.MULTI)) {
            return Sets.newHashSet(ToolType.SHOVEL, ToolType.PICKAXE, ToolType.AXE);
        }

        return Sets.newHashSet(ToolType.SHOVEL);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable Player player, @Nullable BlockState blockState) {
        if (LootItemHelper.hasEffect(stack, LootEffect.MULTI)) {
            return 4;
        }

        return super.getHarvestLevel(stack, tool, player, blockState);
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state) {
        if (LootItemHelper.hasEffect(stack, LootEffect.MULTI)) {
            return true;
        }
        return super.isCorrectToolForDrops(state);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return LootItemHelper.getMaxDamage(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (Screen.hasShiftDown()) {
            LootItemHelper.addInformation(stack, tooltip);
        }

        tooltip.add(new TextComponent(ChatFormatting.RESET + "" + "Shovel"));

        double efficiency = ModAttributes.LOOT_EFFICIENCY.getOrDefault(stack, 1.0).getValue(stack);
        tooltip.add(new TextComponent(ChatFormatting.GRAY + "" + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(efficiency) + " Mining Speed"));
    }

}
