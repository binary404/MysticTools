package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class LootEffectAreaMiner implements IEffectAction {

    @Override
    public void toggleAction(Player player, ItemStack stack) {
        int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL);

        level = (level + 1) % 5;

        LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL, level);
    }

    @Override
    public void handleHarvest(Player player, ItemStack stack, List<ItemStack> drops, BlockPos pos) {
    }

    @Override
    public Component modificationResponseMessage(Player player, ItemStack stack) {
        int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL);

        String message = "[Mystic Tools]: ";

        message += ChatFormatting.RESET;
        message += stack.getDisplayName().getString();
        message += ChatFormatting.RESET;
        message += "'s ";
        message += "area miner";
        message += ", has been set to: ";
        if (level == 0)
            message += "[Off]";
        else
            message += "[" + (level + 1) + "x" + (level + 1) + "]";

        return new TextComponent(message);
    }

    @Override
    public void handleUpdate(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {

    }

    @Override
    public String getStatusString(ItemStack stack) {
        int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL);
        String status = "[" + (level + 1) + "x" + (level + 1) + "]";
        return status;
    }

    @Override
    public InteractionResultHolder<ItemStack> handleUse(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        return defaultAction;
    }
}
