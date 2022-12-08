package binary404.mystictools.common.blocks;

import binary404.mystictools.common.core.UniqueHandler;
import binary404.mystictools.common.items.ILootItem;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketFX;
import binary404.mystictools.common.tile.CauldronBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BlockCauldron extends Block implements EntityBlock {

    private static final VoxelShape INSIDE = box(2.0D, 8.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);

    public BlockCauldron(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isClientSide && entityIn instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entityIn;
            Item item = itemEntity.getItem().getItem();
            if (item instanceof ILootItem) {
                ItemStack stack;
                LootRarity rarity = LootRarity.fromId(ModAttributes.LOOT_RARITY.getOrDefault(itemEntity.getItem(), "common").getValue(itemEntity.getItem()));
                if (rarity == LootRarity.COMMON) {
                    stack = new ItemStack(ModItems.shard.get(), Mth.nextInt(worldIn.random, 3, 7));
                } else if (rarity == LootRarity.UNCOMMON) {
                    stack = new ItemStack(ModItems.shard.get(), Mth.nextInt(worldIn.random, 6, 10));
                } else if (rarity == LootRarity.RARE) {
                    stack = new ItemStack(ModItems.shard.get(), Mth.nextInt(worldIn.random, 9, 13));
                } else if (rarity == LootRarity.EPIC) {
                    stack = new ItemStack(ModItems.shard.get(), Mth.nextInt(worldIn.random, 12, 15));
                } else if (rarity == LootRarity.UNIQUE) {
                    stack = new ItemStack(ModItems.shard.get(), Mth.nextInt(worldIn.random, 15, 18));
                    if (worldIn instanceof ServerLevel)
                        UniqueHandler.resetUniqueItems((ServerLevel) worldIn);
                } else {
                    stack = new ItemStack(ModItems.shard.get(), Mth.nextInt(worldIn.random, 3, 7));
                }
                NetworkHandler.sendToNearby(worldIn, entityIn, new PacketFX(entityIn.getX(), entityIn.getY(), entityIn.getZ(), 0));
                itemEntity.setItem(stack);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CauldronBlockEntity(p_153215_, p_153216_);
    }
}
