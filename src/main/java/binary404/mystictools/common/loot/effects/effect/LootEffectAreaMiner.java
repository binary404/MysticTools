package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class LootEffectAreaMiner implements IEffectAction {

    @Override
    public void toggleAction(PlayerEntity player, ItemStack stack) {
        int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL);

        level = (level + 1) % 5;

        LootNbtHelper.setLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL, level);
    }

    @Override
    public void handleHarvest(PlayerEntity player, ItemStack stack, List<ItemStack> drops) {

    }

    @Override
    public ITextComponent modificationResponseMessage(PlayerEntity player, ItemStack stack) {
        int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL);

        String message = "[Mystic Tools]: ";

        message += TextFormatting.RESET;
        message += stack.getDisplayName().getString();
        message += TextFormatting.RESET;
        message += "'s ";
        message += I18n.format("weaponeffect.area_miner.name");
        message += ", has been set to: ";
        if (level == 0)
            message += "[Off]";
        else
            message += "[" + (level + 1) + "x" + (level + 1) + "]";

        return new StringTextComponent(message);
    }

    @Override
    public void handleUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {

    }

    @Override
    public String getStatusString(ItemStack stack) {
        int level = LootNbtHelper.getLootIntValue(stack, LootTags.LOOT_TAG_EFFECT_LEVEL);
        String status = "[" + (level + 1) + "x" + (level + 1) + "]";
        return status;
    }

    @Override
    public ActionResult<ItemStack> handleUse(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand) {
        return defaultAction;
    }
}
