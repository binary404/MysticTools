package binary404.mystictools.common.loot.effects;


import binary404.mystictools.common.loot.LootSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public interface IEffectAction {
    default public void toggleAction(Player player, ItemStack stack) {

    }

    default public void handleHarvest(Player player, ItemStack stack, List<ItemStack> drops, BlockPos pos) {

    }

    default public void handleUpdate(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {

    }

    default public void handleArmorHit(ItemStack stack, LivingEntity wearer, LivingEntity attacker) {

    }

    default public InteractionResultHolder<ItemStack> handleUse(InteractionResultHolder<ItemStack> defaultAction, Level world, Player player, InteractionHand hand) {
        return defaultAction;
    }

    default public Component modificationResponseMessage(Player player, ItemStack stack) {
        return Component.literal("");
    }

    default public boolean hasResponseMessage(Player player, ItemStack stack) {
        return true;
    }

    default public String getStatusString(ItemStack stack) {
        return "";
    }

    default public String getAdditionalTooltip(ItemStack stack, int id) {
        return "";
    }

    default public void handleHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int id) {
    }

    default public void rollExtra(ItemStack stack, LootSet.LootSetType type, RandomSource random) {

    }

    default public CompoundTag addAdditionalData(ItemStack stack, LootSet.LootSetType type, RandomSource random) {
        return new CompoundTag();
    }
}
