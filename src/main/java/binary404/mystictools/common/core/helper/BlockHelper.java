package binary404.mystictools.common.core.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;

public class BlockHelper {

    public static void damageMiningItem(ItemStack stack, ServerPlayer player, int amount) {
        Runnable damageItem = () -> stack.hurtAndBreak(amount, (LivingEntity) player, livingEntity -> {});
        damageItem.run();
    }

    public static boolean breakBlock(ServerLevel world, ServerPlayer player, BlockPos pos, boolean breakBlock, boolean ignoreHarvestRestrictions) {
        return breakBlock(world, player, pos, world.getBlockState(pos), player.getMainHandItem(), breakBlock, ignoreHarvestRestrictions);
    }

    public static boolean breakBlock(ServerLevel world, ServerPlayer player, BlockPos pos, BlockState stateBroken, ItemStack heldItem, boolean breakBlock, boolean ignoreHarvestRestrictions) {
        ItemStack original = player.getItemInHand(InteractionHand.MAIN_HAND);
        try {
            player.setItemInHand(InteractionHand.MAIN_HAND, heldItem);
            return doNativeBreakBlock(world, player, pos, stateBroken, heldItem, breakBlock, ignoreHarvestRestrictions);
        } finally {
            player.setItemInHand(InteractionHand.MAIN_HAND, original);
        }
    }

    private static boolean doNativeBreakBlock(ServerLevel world, ServerPlayer player, BlockPos pos, BlockState stateBroken, ItemStack heldItem, boolean breakBlock, boolean ignoreHarvestRestrictions) {
        int xp;
        try {
            boolean preCancelEvent = false;
            if (!heldItem.isEmpty() && !heldItem.getItem().canAttackBlock(stateBroken, (Level) world, pos, (Player) player)) {
                preCancelEvent = true;
            }
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent((Level) world, pos, stateBroken, (Player) player);
            event.setCanceled(preCancelEvent);
            MinecraftForge.EVENT_BUS.post((Event) event);

            if (event.isCanceled()) {
                return false;
            }
            xp = event.getExpToDrop();
        } catch (Exception exc) {
            return false;
        }
        if (xp == -1) {
            return false;
        }

        if (heldItem.onBlockStartBreak(pos, (Player) player)) {
            return false;
        }

        boolean harvestable = true;
        try {
            if (!ignoreHarvestRestrictions) {
                harvestable = stateBroken.canHarvestBlock((BlockGetter) world, pos, (Player) player);
            }
        } catch (Exception exc) {
            return false;
        }

        try {
            heldItem.copy().mineBlock((Level) world, stateBroken, pos, (Player) player);
        } catch (Exception exc) {
            return false;
        }

        boolean wasCapturingStates = world.captureBlockSnapshots;
        List<BlockSnapshot> previousCapturedStates = new ArrayList<>(world.capturedBlockSnapshots);

        BlockEntity tileEntity = world.getBlockEntity(pos);

        world.captureBlockSnapshots = true;
        try {
            if (breakBlock) {
                if (!stateBroken.onDestroyedByPlayer((Level) world, pos, (Player) player, harvestable, Fluids.EMPTY.defaultFluidState())) {
                    restoreWorldState((Level) world, wasCapturingStates, previousCapturedStates);
                    return false;
                }
            } else {
                stateBroken.getBlock().playerWillDestroy((Level) world, pos, stateBroken, (Player) player);
            }
        } catch (Exception exc) {
            restoreWorldState((Level) world, wasCapturingStates, previousCapturedStates);
            return false;
        }

        stateBroken.getBlock().destroy((LevelAccessor) world, pos, stateBroken);

        if (harvestable) {
            try {
                stateBroken.getBlock().playerDestroy((Level) world, (Player) player, pos, stateBroken, tileEntity, heldItem.copy());
            } catch (Exception exc) {
                restoreWorldState((Level) world, wasCapturingStates, previousCapturedStates);
                return false;
            }
        }

        if (xp > 0) {
            stateBroken.getBlock().popExperience(world, pos, xp);
        }
        BlockDropCaptureHelper.startCapturing();
        try {
            world.captureBlockSnapshots = false;
            world.restoringBlockSnapshots = true;
            world.capturedBlockSnapshots.forEach(s -> {
                world.sendBlockUpdated(s.getPos(), s.getReplacedBlock(), s.getCurrentBlock(), s.getFlag());
                s.getCurrentBlock().updateNeighbourShapes((LevelAccessor) world, s.getPos(), 11);
            });
            world.restoringBlockSnapshots = false;
        } finally {
            BlockDropCaptureHelper.getCapturedStacksAndStop();


            world.capturedBlockSnapshots.clear();
            world.captureBlockSnapshots = wasCapturingStates;
            world.capturedBlockSnapshots.addAll(previousCapturedStates);
        }
        return true;
    }

    private static void restoreWorldState(Level world, boolean prevCaptureFlag, List<BlockSnapshot> prevSnapshots) {
        world.captureBlockSnapshots = false;

        world.restoringBlockSnapshots = true;
        world.capturedBlockSnapshots.forEach(s -> s.restore(true));
        world.restoringBlockSnapshots = false;

        world.capturedBlockSnapshots.clear();

        world.captureBlockSnapshots = prevCaptureFlag;
        world.capturedBlockSnapshots.addAll(prevSnapshots);
    }

}
