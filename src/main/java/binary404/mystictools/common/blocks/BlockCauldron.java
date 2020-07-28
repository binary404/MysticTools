package binary404.mystictools.common.blocks;

import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.items.ILootItem;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketOpenCrateFX;
import binary404.mystictools.common.tile.TileEntityCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class BlockCauldron extends Block {

    private static final VoxelShape INSIDE = makeCuboidShape(2.0D, 8.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), IBooleanFunction.ONLY_FIRST);

    public BlockCauldron(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entityIn;
            Item item = itemEntity.getItem().getItem();
            if (item instanceof ILootItem) {
                ItemStack stack;
                LootRarity rarity = LootRarity.fromId(LootNbtHelper.getLootStringValue(itemEntity.getItem(), LootTags.LOOT_TAG_RARITY));
                if (rarity == LootRarity.COMMON) {
                    stack = new ItemStack(ModItems.shard, MathHelper.nextInt(worldIn.rand, 3, 7));
                } else if (rarity == LootRarity.UNCOMMON) {
                    stack = new ItemStack(ModItems.shard, MathHelper.nextInt(worldIn.rand, 6, 10));
                } else if (rarity == LootRarity.RARE) {
                    stack = new ItemStack(ModItems.shard, MathHelper.nextInt(worldIn.rand, 9, 13));
                } else if (rarity == LootRarity.EPIC) {
                    stack = new ItemStack(ModItems.shard, MathHelper.nextInt(worldIn.rand, 12, 15));
                } else if (rarity == LootRarity.UNIQUE) {
                    stack = new ItemStack(ModItems.shard, MathHelper.nextInt(worldIn.rand, 15, 18));
                    if (worldIn instanceof ServerWorld)
                        UniqueHandler.resetUniqueItems((ServerWorld) worldIn);
                } else {
                    stack = new ItemStack(ModItems.shard, MathHelper.nextInt(worldIn.rand, 3, 7));
                }
                NetworkHandler.sendToNearby(worldIn, entityIn, new PacketOpenCrateFX(entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ()));
                itemEntity.setItem(stack);
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileEntityCauldron();
    }
}
