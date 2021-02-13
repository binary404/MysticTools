package binary404.mystictools.common.loot.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.List;

public interface IEffectAction {
    default public void toggleAction(PlayerEntity player, ItemStack stack) {

    }

    default public void handleHarvest(PlayerEntity player, ItemStack stack, List<ItemStack> drops, BlockPos pos) {

    }

    default public void handleUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {

    }

    default public void handleArmorHit(ItemStack stack, LivingEntity wearer, LivingEntity attacker) {

    }

    default public ActionResult<ItemStack> handleUse(ActionResult<ItemStack> defaultAction, World world, PlayerEntity player, Hand hand) {
        return defaultAction;
    }

    default public ITextComponent modificationResponseMessage(PlayerEntity player, ItemStack stack) {
        return new StringTextComponent("");
    }

    default public boolean hasResponseMessage(PlayerEntity player, ItemStack stack) {
        return true;
    }

    default public String getStatusString(ItemStack stack) {
        return "";
    }

    default public void handleHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

    }
}
