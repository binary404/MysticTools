package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class LootEffectAutoSmelt implements IEffectAction {

    @Override
    public void toggleAction(Player player, ItemStack stack) {
        boolean active = false;

        if (stack == null)
            return;

        active = !LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE);
        LootNbtHelper.setLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE, active);
    }

    public boolean active(ItemStack stack) {
        return LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE);
    }

    @Override
    public void handleHarvest(Player player, ItemStack stack, List<ItemStack> drops, BlockPos pos) {
        boolean effectActive = LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE);

        if (!effectActive)
            return;

        NetworkHandler.sendToNearby(player.level, player, new PacketSparkle(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.92F, 0.35F, 0.12F));
    }

    @Override
    public Component modificationResponseMessage(Player player, ItemStack stack) {
        boolean active = LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE);

        String message = "[Mystic Tools]: ";

        message += stack.getDisplayName().getString();
        message += ChatFormatting.RESET;
        message += "'s ";
        message += "auto smelt";
        message += ", has been set to: ";
        message += active;

        return Component.literal(message);
    }

    @Override
    public void handleUpdate(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
    }

    @Override
    public String getStatusString(ItemStack stack) {
        String status = "[Off]";

        if (LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE))
            status = "[On]";

        return status;
    }

    @Override
    public InteractionResultHolder<ItemStack> handleUse(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        return defaultAction;
    }

}
