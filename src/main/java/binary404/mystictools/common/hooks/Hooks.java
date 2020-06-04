package binary404.mystictools.common.hooks;

import binary404.mystictools.common.core.event.DropLootEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class Hooks {

    public static List<ItemStack> fireBlockDropLootEvent(LootContext.Builder contextBuilder, BlockState state1) {

        List<ItemStack> drops = new ArrayList<>(state1.getBlock().getDrops(state1,contextBuilder));
        // Build the loot context and gather info about the block from it.
        LootContext context = contextBuilder.withParameter(LootParameters.BLOCK_STATE, state1).build(LootParameterSets.BLOCK);
        ServerWorld world = context.getWorld();
        BlockPos pos = context.get(LootParameters.POSITION);
        BlockState state = context.get(LootParameters.BLOCK_STATE);
        Entity dropCause = context.get(LootParameters.THIS_ENTITY);
        PlayerEntity player = dropCause instanceof PlayerEntity ? (PlayerEntity) dropCause : null;

        DropLootEvent dropEvent = new DropLootEvent(world, pos, state, contextBuilder.build(LootParameterSets.BLOCK), player, drops);
        MinecraftForge.EVENT_BUS.post(dropEvent);
        return dropEvent.getDrops();
    }

}
