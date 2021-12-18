package binary404.mystictools.common.loot.effects.unique;

import binary404.fx_lib.fx.effects.EffectRegistrar;
import binary404.fx_lib.fx.effects.FXGen;
import binary404.fx_lib.fx.effects.FXSourceOrbital;
import binary404.fx_lib.fx.effects.ParticleOrbitalController;
import binary404.fx_lib.util.Vector3;
import binary404.mystictools.MysticTools;
import binary404.mystictools.client.fx.FXHelper;
import binary404.mystictools.common.loot.effects.IUniqueEffect;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.network.PacketRemoveSpirals;
import binary404.mystictools.common.network.PacketSparkle;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

public class TreeChopper implements IUniqueEffect {

    private static final int BLOCK_SWAP_RATE = 3;
    private static final int LEAF_BLOCK_RANGE = 5;

    private static final Map<ResourceKey<Level>, Set<BlockSwapper>> blockSwappers = new HashMap<>();

    public static final Map<ResourceKey<Level>, Set<FXSourceOrbital>> orbitals = new HashMap<>();

    public static final List<Material> materialsAxe = Arrays.asList(Material.LEAVES, Material.PLANT, Material.WOOD, Material.VEGETABLE);

    public TreeChopper() {
        MinecraftForge.EVENT_BUS.addListener(this::tickEnd);
    }

    private void tickEnd(TickEvent.WorldTickEvent event) {
        if (event.world.isClientSide) {
            return;
        }
        if (event.phase == TickEvent.Phase.END && event.world.getGameTime() % 5 == 0) {
            ResourceKey<Level> dim = event.world.dimension();
            if (blockSwappers.containsKey(dim)) {
                Set<BlockSwapper> swappers = blockSwappers.get(dim);
                swappers.removeIf((next) -> {
                    if (next == null || !next.tick()) {
                        NetworkHandler.sendToNearby(event.world, next.origin, new PacketRemoveSpirals());
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    @Override
    public void breakBlock(BlockPos pos, Level world, Player player, ItemStack stack, BlockState stateBroken) {
        if (materialsAxe.contains(stateBroken.getMaterial()))
            addBlockSwapper(world, player, stack, pos, 32, true);
    }

    private static void addBlockSwapper(Level world, Player player, ItemStack stack, BlockPos origCoords, int steps, boolean leaves) {
        BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords, steps, leaves);
        ResourceKey<Level> dim = world.dimension();

        if (world.isClientSide) {
            List<FXSourceOrbital> orbitalsToAdd = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                FXSourceOrbital orbital = FXHelper.spiralGenerator(origCoords, Vector3.random()).refresh((t) -> true);
                EffectRegistrar.register(orbital);
                orbitalsToAdd.add(orbital);
            }
            orbitals.computeIfAbsent(dim, d -> new HashSet<>()).addAll(orbitalsToAdd);
            return;
        }
        blockSwappers.computeIfAbsent(dim, d -> new HashSet<>()).add(swapper);
    }

    public static void removeBlockWithDrops(Player player, ItemStack stack, Level world, BlockPos pos,
                                            Predicate<BlockState> filter,
                                            boolean dispose, boolean particles) {
        if (!world.hasChunkAt(pos)) {
            return;
        }

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!world.isClientSide && filter.test(state)
                && !(block instanceof AirBlock) && state.getDestroyProgress(player, world, pos) > 0
                && state.canHarvestBlock(player.level, pos, player)) {
            int exp = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayer) player).gameMode.getGameModeForPlayer(), (ServerPlayer) player, pos);
            if (exp == -1) {
                return;
            }

            if (!player.getAbilities().instabuild) {
                BlockEntity tile = world.getBlockEntity(pos);

                if (block.onDestroyedByPlayer(state, world, pos, player, true, world.getFluidState(pos))) {
                    block.destroy(world, pos, state);
                    if (!dispose) {
                        block.playerDestroy(world, player, pos, state, tile, stack);
                        if (world instanceof ServerLevel)
                            block.popExperience((ServerLevel) world, pos, exp);
                    }
                }
            } else {
                world.removeBlock(pos, false);
            }

            if (particles) {
                NetworkHandler.sendToNearby(world, player, new PacketSparkle(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.1F, 0.96F, 0.1F));
                world.levelEvent(2001, pos, Block.getId(state));
            }
        }
    }

    private static class BlockSwapper {
        public static final int SINGLE_BLOCK_RADIUS = 1;
        private final Level world;
        private final Player player;

        private final ItemStack stack;

        private final BlockPos origin;

        private final boolean treatLeavesSpecial;

        private final int range;

        private final PriorityQueue<SwapCandidate> candidateQueue;

        private final Set<BlockPos> completedCoords;

        public BlockSwapper(Level world, Player player, ItemStack truncator, BlockPos origCoords, int range, boolean leaves) {
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

                        coords.add(original.offset(dx, dy, dz));
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
