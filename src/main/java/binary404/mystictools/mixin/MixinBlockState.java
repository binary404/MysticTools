package binary404.mystictools.mixin;

import binary404.mystictools.common.event.DropLootEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class MixinBlockState {

    @Inject(at = @At("RETURN"), method = "getDrops", cancellable = true)
    public void fireDropLootEvent(LootContext.Builder contextBuilder, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> drops = cir.getReturnValue();
        cir.setReturnValue(drops);
        // Build the loot context and gather info about the block from it.
        LootContext context = contextBuilder.withParameter(LootParameters.BLOCK_STATE, (BlockState) (Object) this).build(LootParameterSets.BLOCK);
        ServerWorld world = context.getWorld();
        BlockPos pos = context.get(LootParameters.POSITION);
        BlockState state = context.get(LootParameters.BLOCK_STATE);
        Entity dropCause = context.get(LootParameters.THIS_ENTITY);
        PlayerEntity player = dropCause instanceof PlayerEntity ? (PlayerEntity) dropCause : null;

        DropLootEvent dropEvent = new DropLootEvent(world, pos, state, contextBuilder.build(LootParameterSets.BLOCK), player, drops);
        MinecraftForge.EVENT_BUS.post(dropEvent);
        cir.setReturnValue(dropEvent.getDrops());
    }
}
