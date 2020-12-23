package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.loot.effects.IUniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

public class TreeChopper implements IUniqueEffect {

    private static final int BLOCK_SWAP_RATE = 3;
    private static final int LEAF_BLOCK_RANGE = 5;

    private static final Map<DimensionType, Set<BlockSwapper>> blockSwappers = new HashMap<>();

    public static final List<Material> materialsAxe = Arrays.asList(Material.CORAL, Material.LEAVES, Material.PLANTS, Material.WOOD, Material.GOURD);

    public TreeChopper() {
        MinecraftForge.EVENT_BUS.addListener(this::tickEnd);
    }

    private void tickEnd(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) {
            return;
        }
        if (event.phase == TickEvent.Phase.END && event.world.getGameTime() % 5 == 0) {
            DimensionType dim = event.world.getDimensionType();
            if (blockSwappers.containsKey(dim)) {
                Set<BlockSwapper> swappers = blockSwappers.get(dim);
                swappers.removeIf(next -> next == null || !next.tick());
            }
        }
    }

    @Override
    public void breakBlock(BlockPos pos, World world, PlayerEntity player, ItemStack stack) {
        addBlockSwapper(world, player, stack, pos, 32, true);
    }

    private static void addBlockSwapper(World world, PlayerEntity player, ItemStack stack, BlockPos origCoords, int steps, boolean leaves) {
        BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords, steps, leaves);

        if (world.isRemote) {
            return;
        }
        DimensionType dim = world.getDimensionType();
        blockSwappers.computeIfAbsent(dim, d -> new HashSet<>()).add(swapper);
    }

    public static void removeBlockWithDrops(PlayerEntity player, ItemStack stack, World world, BlockPos pos,
                                            Predicate<BlockState> filter,
                                            boolean dispose, boolean particles) {
        if (!world.isBlockLoaded(pos)) {
            return;
        }

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!world.isRemote && filter.test(state)
                && !block.isAir(state, world, pos) && state.getPlayerRelativeBlockHardness(player, world, pos) > 0
                && state.canHarvestBlock(player.world, pos, player)) {
            int exp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).interactionManager.getGameType(), (ServerPlayerEntity) player, pos);
            if (exp == -1) {
                return;
            }

            if (!player.abilities.isCreativeMode) {
                TileEntity tile = world.getTileEntity(pos);

                if (block.removedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                    block.onPlayerDestroy(world, pos, state);
                    if (!dispose) {
                        block.harvestBlock(world, player, pos, state, tile, stack);
                        if (world instanceof ServerWorld)
                            block.dropXpOnBlockBreak((ServerWorld) world, pos, exp);
                    }
                }
            } else {
                world.removeBlock(pos, false);
            }

            if (particles) {
                NetworkHandler.sendToNearby(world, player, new PacketSparkle(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.1F, 0.96F, 0.1F));
                world.playEvent(2001, pos, Block.getStateId(state));
            }
        }
    }

    private static class BlockSwapper {
        public static final int SINGLE_BLOCK_RADIUS = 1;
        private final World world;
        private final PlayerEntity player;

        private final ItemStack stack;

        private final BlockPos origin;

        private final boolean treatLeavesSpecial;

        private final int range;

        private final PriorityQueue<SwapCandidate> candidateQueue;

        private final Set<BlockPos> completedCoords;

        public BlockSwapper(World world, PlayerEntity player, ItemStack truncator, BlockPos origCoords, int range, boolean leaves) {
            this.world = world;
            this.player = player;
            this.stack = truncator;
            origin = origCoords;
            this.range = range;
            treatLeavesSpecial = leaves;

            candidateQueue = new PriorityQueue<>();
            completedCoords = new HashSet<>();

            // Add the origin to our candidate queue with the original range
            candidateQueue.offer(new SwapCandidate(origin, this.range));
        }

        public boolean tick() {
            // If empty, this swapper is done.
            if (candidateQueue.isEmpty()) {
                return false;
            }

            int remainingSwaps = BLOCK_SWAP_RATE;
            while (remainingSwaps > 0 && !candidateQueue.isEmpty()) {
                SwapCandidate cand = candidateQueue.poll();

                // If we've already completed this location, move along, as this
                // is just a suboptimal one.
                if (completedCoords.contains(cand.coordinates)) {
                    continue;
                }

                // If this candidate is out of range, discard it.
                if (cand.range <= 0) {
                    continue;
                }

                // Otherwise, perform the break and then look at the adjacent tiles.
                // This is a ridiculous function call here.
                removeBlockWithDrops(player, stack, world,
                        cand.coordinates,
                        state -> materialsAxe.contains(state.getMaterial()),
                        false, treatLeavesSpecial);

                remainingSwaps--;

                completedCoords.add(cand.coordinates);

                // Then, go through all of the adjacent blocks and look if
                // any of them are any good.
                for (BlockPos adj : adjacent(cand.coordinates)) {
                    Block block = world.getBlockState(adj).getBlock();

                    boolean isWood = BlockTags.LOGS.contains(block);
                    boolean isLeaf = BlockTags.LEAVES.contains(block);

                    // If it's not wood or a leaf, we aren't interested.
                    if (!isWood && !isLeaf) {
                        continue;
                    }

                    // If we treat leaves specially and this is a leaf, it gets
                    // the minimum of the leaf range and the current range - 1.
                    // Otherwise, it gets the standard range - 1.
                    int newRange = treatLeavesSpecial && isLeaf ? Math.min(LEAF_BLOCK_RANGE, cand.range - 1) : cand.range - 1;

                    candidateQueue.offer(new SwapCandidate(adj, newRange));
                }
            }

            // If we did any iteration, then hang around until next tick.
            return true;
        }

        public List<BlockPos> adjacent(BlockPos original) {
            List<BlockPos> coords = new ArrayList<>();
            for (int dx = -SINGLE_BLOCK_RADIUS; dx <= SINGLE_BLOCK_RADIUS; dx++) {
                for (int dy = -SINGLE_BLOCK_RADIUS; dy <= SINGLE_BLOCK_RADIUS; dy++) {
                    for (int dz = -SINGLE_BLOCK_RADIUS; dz <= SINGLE_BLOCK_RADIUS; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) {
                            continue;
                        }

                        coords.add(original.add(dx, dy, dz));
                    }
                }
            }

            return coords;
        }

        /**
         * Represents a potential candidate for swapping/removal. Sorted by
         * range (where a larger range is more preferable). As we're using
         * a priority queue, which is a min-heap internally, larger ranges
         * are considered "smaller" than smaller ranges (so they show up in the
         * min-heap first).
         */
        public static final class SwapCandidate implements Comparable<SwapCandidate> {
            /**
             * The location of this swap candidate.
             */
            public final BlockPos coordinates;

            /**
             * The remaining range of this swap candidate.
             */
            public final int range;

            /**
             * Constructs a new Swap Candidate with the provided
             * coordinates and range.
             *
             * @param coordinates The coordinates of this candidate.
             * @param range       The remaining range of this candidate.
             */
            public SwapCandidate(BlockPos coordinates, int range) {
                this.coordinates = coordinates;
                this.range = range;
            }

            @Override
            public int compareTo(@Nonnull SwapCandidate other) {
                // Aka, a bigger range implies a smaller value, meaning
                // bigger ranges will be preferred in a min-heap
                return other.range - range;
            }

            @Override
            public boolean equals(Object other) {
                if (!(other instanceof SwapCandidate)) {
                    return false;
                }

                SwapCandidate cand = (SwapCandidate) other;
                return coordinates.equals(cand.coordinates) && range == cand.range;
            }

            @Override
            public int hashCode() {
                return Objects.hash(coordinates, range);
            }
        }
    }

}
