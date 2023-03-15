package binary404.mystictools.common.blocks;

import binary404.mystictools.common.tile.ModTiles;
import binary404.mystictools.common.tile.SacrificialAltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockRitualAltar extends BaseEntityBlock {

    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public BlockRitualAltar() {
        super(BlockBehaviour.Properties.of(Material.STONE).strength(3.5f).lightLevel((state) -> {
            if (state.getValue(ACTIVATED)) return 10;
            return 0;
        }));
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVATED, false));
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldStack = pPlayer.getItemInHand(pHand);
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof SacrificialAltarBlockEntity altar) {
                if (!pState.getValue(ACTIVATED)) {
                    if (!heldStack.isEmpty()) {
                        pPlayer.setItemInHand(pHand, altar.tryAddItem(heldStack));
                        return InteractionResult.CONSUME;
                    } else if (heldStack.isEmpty() && pPlayer.isShiftKeyDown()) {
                        int openSlot = altar.getFirstNonEmptySlot();
                        if (openSlot >= 0) {
                            pPlayer.setItemInHand(pHand, altar.getStackInSlot(openSlot));
                            altar.setStackInSlot(openSlot, ItemStack.EMPTY);
                            return InteractionResult.CONSUME;
                        }
                        return InteractionResult.PASS;
                    } else {
                        boolean started = SacrificialAltarBlockEntity.attemptStartRitual(pLevel, pPos, pState, altar);
                        return InteractionResult.CONSUME;
                    }
                }
            }
            return InteractionResult.PASS;
        }
        if (!pState.getValue(ACTIVATED)) return InteractionResult.SUCCESS;
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModTiles.ALTAR.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pState.getValue(ACTIVATED))
            return createTickerHelper(pBlockEntityType, ModTiles.ALTAR.get(), SacrificialAltarBlockEntity::ritualTick);
        return super.getTicker(pLevel, pState, pBlockEntityType);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ACTIVATED);
    }
}
