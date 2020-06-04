package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.core.util.Utils;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.IEffectAction;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class LootEffectAutoSmelt implements IEffectAction {

    @Override
    public void toggleAction(PlayerEntity player, ItemStack stack) {
        boolean active = false;

        if (stack == null)
            return;

        active = !LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE);
        LootNbtHelper.setLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE, active);
    }

    @Override
    public void handleHarvest(PlayerEntity player, ItemStack stack, List<ItemStack> drops) {
        System.out.println("AUTO SMELTING");
        boolean effectActive = LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE);

        if (!effectActive)
            return;

        List<ItemStack> smelted = new ArrayList<ItemStack>();

        for (ItemStack drop : drops) {
            ItemStack smeltResult = Utils.findSmeltingResult(player.world, drop).orElse(drop);

            smelted.add(smeltResult.copy());
        }

        drops.clear();
        drops.addAll(smelted);
    }

    @Override
    public ITextComponent modificationResponseMessage(PlayerEntity player, ItemStack stack) {
        boolean active = LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE);

        String message = "[Mystic Tools]: ";

        message += stack.getDisplayName().getString();
        message += TextFormatting.RESET;
        message += "'s ";
        message += I18n.format("weaponeffect.auto_smelt.name");
        message += ", has been set to: ";
        message += active;

        return new StringTextComponent(message);
    }

    @Override
    public void handleUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    }

    @Override
    public String getStatusString(ItemStack stack) {
        String status = "[Off]";

        if (LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE))
            status = "[On]";

        return status;
    }

    @Override
    public ActionResult<ItemStack> handleUse(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand) {
        return defaultAction;
    }

}
