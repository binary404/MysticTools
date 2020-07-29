package binary404.mystictools.common.blocks;

import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.tile.TileEntityUpgrader;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;


public class BlockUpgrader extends ContainerBlock {

    public BlockUpgrader(Properties builder) {
        super(builder);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityUpgrader();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return createNewTileEntity(world);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            INamedContainerProvider provider = this.getContainer(state, worldIn, pos);
            if (provider != null) {
                if (player instanceof ServerPlayerEntity) {
                    ServerPlayerEntity playerEntity = (ServerPlayerEntity) player;
                    NetworkHooks.openGui(playerEntity, provider, (packetBuffer -> {
                        packetBuffer.writeBlockPos(pos);
                    }));
                }
            }
            return ActionResultType.CONSUME;
        }
    }
}
