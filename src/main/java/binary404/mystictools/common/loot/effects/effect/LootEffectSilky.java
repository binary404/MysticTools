package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.core.helper.ActiveFlags;
import binary404.mystictools.common.core.helper.BlockDropCaptureHelper;
import binary404.mystictools.common.core.helper.BlockHelper;
import binary404.mystictools.common.core.helper.OverLevelEnchantmentHelper;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class LootEffectSilky implements IEffectAction {

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

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        ItemStack heldStack = player.getMainHandItem();
        boolean active = LootNbtHelper.getLootBooleanValue(heldStack, LootTags.LOOT_TAG_EFFECT_ACTIVE);
        if (!active || !LootItemHelper.hasEffect(heldStack, LootEffect.getById("silky")))
            return;
        ActiveFlags.IS_SILKY_MINING.runIfNotSet(() -> {
            ServerLevel level = (ServerLevel) event.getWorld();
            ItemStack miningStack = OverLevelEnchantmentHelper.enableSilkTouch(heldStack.copy());
            BlockPos pos = event.getPos();

            BlockDropCaptureHelper.startCapturing();
            try {
                BlockHelper.breakBlock(level, player, pos, level.getBlockState(pos), miningStack, true, true);
                BlockHelper.damageMiningItem(heldStack, player, 1);
            } finally {
                BlockDropCaptureHelper.getCapturedStacksAndStop()
                        .forEach((item) -> Block.popResource((Level) level, (BlockPos) item.blockPosition(), (ItemStack) item.getItem()));
            }
            event.setCanceled(true);
        });
    }

    @Override
    public Component modificationResponseMessage(Player player, ItemStack stack) {
        boolean active = LootNbtHelper.getLootBooleanValue(stack, LootTags.LOOT_TAG_EFFECT_ACTIVE);

        String message = "[Mystic Tools]: ";

        message += stack.getDisplayName().getString();
        message += ChatFormatting.RESET;
        message += "'s ";
        message += "silky";
        message += ", has been set to: ";
        message += active;

        return new TextComponent(message);
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
