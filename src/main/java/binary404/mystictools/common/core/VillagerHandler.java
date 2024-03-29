package binary404.mystictools.common.core;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VillagerHandler {

    /*
    public static PoiType POI_CORROSION_CAULDRON;

    public static VillagerProfession MERCHANT;

    public static void init() {
        PlainsVillagePools.init();
        SnowyVillagePools.init();
        SavannaVillagePools.init();
        DesertVillagePools.init();
        TaigaVillagePools.init();

        for (String biome : new String[]{"plains", "snowy", "savanna", "desert", "taiga"})
            addToPool(new ResourceLocation("village/" + biome + "/houses"),
                    rl("village/houses/merchant"), 6);

        HeroGiftTaskAccess.getGifts().put(MERCHANT, rl("gameplay/hero_of_the_village/merchant"));
    }

    private static JigsawPiece createWorkstation(String name) {
        return SingleJigsawAccess.construct(
                Either.left(rl(name)),
                () -> ProcessorLists.field_244101_a,
                JigsawPattern.PlacementBehaviour.RIGID
        );
    }

    private static void addToPool(ResourceLocation pool, ResourceLocation toAdd, int weight) {
        JigsawPattern old = WorldGenRegistries.JIGSAW_POOL.getOrDefault(pool);

        // Fixed seed to prevent inconsistencies between different worlds
        List<JigsawPiece> shuffled;
        if (old != null)
            shuffled = old.getShuffledPieces(new Random(0));
        else
            shuffled = ImmutableList.of();
        Object2IntMap<JigsawPiece> newPieces = new Object2IntLinkedOpenHashMap<>();
        for (JigsawPiece p : shuffled)
            newPieces.computeInt(p, (JigsawPiece pTemp, Integer i) -> (i == null ? 0 : i) + 1);
        newPieces.put(SingleJigsawAccess.construct(
                Either.left(toAdd), () -> ProcessorLists.field_244101_a, JigsawPattern.PlacementBehaviour.RIGID
        ), weight);
        List<Pair<JigsawPiece, Integer>> newPieceList = newPieces.object2IntEntrySet().stream()
                .map(e -> Pair.of(e.getKey(), e.getIntValue()))
                .collect(Collectors.toList());

        ResourceLocation name = old.getName();
        Registry.register(WorldGenRegistries.JIGSAW_POOL, pool, new JigsawPattern(pool, name, newPieceList));
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation("mystictools", path);
    }

    @SubscribeEvent
    public static void registerPOI(RegistryEvent.Register<PointOfInterestType> event) {
        POI_CORROSION_CAULDRON = register(event.getRegistry(), createPOI("corrosion_cauldron", ModBlocks.cauldron.getDefaultState()), "corrosion_cauldron");
    }

    @SubscribeEvent
    public static void registerProf(RegistryEvent.Register<VillagerProfession> event) {
        MERCHANT = register(event.getRegistry(), createProf(new ResourceLocation("mystictools", "merchant"), POI_CORROSION_CAULDRON, SoundEvents.BLOCK_WATER_AMBIENT), "merchant");
    }

    private static PointOfInterestType createPOI(String name, BlockState blockState) {
        PointOfInterestType type = new PointOfInterestType("mystictools:" + name, ImmutableSet.of(blockState), 1, 1);
        PoITypeAccess.callRegisterBlockStates(type);
        return type;
    }

    private static VillagerProfession createProf(ResourceLocation name, PointOfInterestType poi, SoundEvent sound) {
        return new VillagerProfession(name.toString(),
                poi,
                ImmutableSet.<Item>builder().build(),
                ImmutableSet.<Block>builder().build(),
                sound);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Trades {

        public static final ResourceLocation MERCHANT = new ResourceLocation("mystictools", "merchant");

        @SubscribeEvent
        public static void registerTrades(VillagerTradesEvent event) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            if (MERCHANT.equals(event.getType().getRegistryName())) {
                trades.get(1).add(new EmeraldForItems(new ItemStack(ModItems.shard), new PriceInterval(4, 26), 4, 2));
                trades.get(1).add(new EmeraldForItems(new ItemStack(ModItems.loot_case), new PriceInterval(1, 4), new PriceInterval(4, 10), 8, 1));
                trades.get(1).add(new ItemsForEmerald(new ItemStack(ModItems.loot_case), new PriceInterval(10, 24), 4, 1, 0.05F));
                trades.get(1).add(new EmeraldForItems(new ItemStack(ModItems.loot_shovel), new PriceInterval(1, 1), 8, 2));
                trades.get(1).add(new EmeraldForItems(new ItemStack(ModItems.loot_axe), new PriceInterval(1, 1), 8, 2));
                trades.get(1).add(new ItemsForEmerald(new ItemStack(ModItems.common_case), new PriceInterval(4, 10), 8, 1, 0.008F));
                trades.get(1).add(new ItemsForEmerald(new ItemStack(ModItems.uncommon_case), new PriceInterval(10, 24), 8, 1, 0.006F));

                trades.get(2).add(new EmeraldForItems(new ItemStack(ModItems.loot_pickaxe), new PriceInterval(1, 1), 8, 4));
                trades.get(2).add(new EmeraldForItems(new ItemStack(ModItems.loot_bow), new PriceInterval(1, 1), 8, 6));
                trades.get(2).add(new ItemsForEmerald(ModItems.shovel_case, new PriceInterval(12, 38), 1, 6, 0.05F));
                trades.get(2).add(new ItemsForEmerald(ModItems.axe_case, new PriceInterval(18, 44), 1, 4, 0.05F));

                trades.get(3).add(new ItemsForEmerald(ModItems.pickaxe_case, new PriceInterval(20, 46), 1, 10, 0.05F));
                trades.get(3).add(new ItemsForEmerald(ModItems.bow_case, new PriceInterval(25, 55), 1, 7, 0.05F));
                trades.get(3).add(new ItemsForEmerald(ModItems.rare_case, new PriceInterval(20, 30), 8, 10, 0.05F));

                trades.get(4).add(new ItemsForEmerald(ModItems.sword_case, new PriceInterval(25, 58), 1, 15, 0.05F));
                trades.get(4).add(new ItemsForEmerald(ModItems.epic_case, new PriceInterval(30, 50), 8, 15, 0.04F));

                trades.get(5).add(new ItemsForEmeraldBlock(ModItems.unique_case, new PriceInterval(40, 64), 1, 30, 0.03F));
            }
        }
    }

    private static class EmeraldForItems implements VillagerTrades.ITrade {

        public ItemStack buyingItem;
        public PriceInterval buyAmounts;
        final int maxUses;
        final int xp;
        public PriceInterval emeralds;

        public EmeraldForItems(@Nonnull ItemStack item, @Nonnull PriceInterval buyAmounts, int maxUses, int xp) {
            this(item, buyAmounts, new PriceInterval(1, 1), maxUses, xp);
        }

        public EmeraldForItems(@Nonnull ItemStack item, @Nonnull PriceInterval buyAmounts, PriceInterval emeralds, int maxUses, int xp) {
            this.buyingItem = item;
            this.buyAmounts = buyAmounts;
            this.maxUses = maxUses;
            this.xp = xp;
            this.emeralds = emeralds;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            return new MerchantOffer(
                    Utils.copyStackWithAmount(this.buyingItem, this.buyAmounts.getPrice(rand)),
                    new ItemStack(Items.EMERALD, emeralds.getPrice(rand)),
                    maxUses, xp, 0.05F
            );
        }
    }

    private static class ItemsForEmerald implements VillagerTrades.ITrade {
        public ItemStack sellingItem;
        public PriceInterval priceInfo;
        final int maxUses;
        final int xp;
        final float priceMult;

        public ItemsForEmerald(IItemProvider item, PriceInterval priceInfo, int maxUses, int xp, float priceMult) {
            this(new ItemStack(item), priceInfo, maxUses, xp, priceMult);
        }

        public ItemsForEmerald(ItemStack par1Item, PriceInterval priceInfo, int maxUses, int xp, float priceMult) {
            this.sellingItem = par1Item;
            this.priceInfo = priceInfo;
            this.maxUses = maxUses;
            this.xp = xp;
            this.priceMult = priceMult;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            int i = 1;
            if (this.priceInfo != null)
                i = this.priceInfo.getPrice(rand);
            ItemStack buying;
            ItemStack selling;
            if (i < 0) {
                buying = new ItemStack(Items.EMERALD);
                selling = Utils.copyStackWithAmount(sellingItem, -i);
            } else {
                buying = new ItemStack(Items.EMERALD, i);
                selling = sellingItem;
            }
            return new MerchantOffer(buying, selling, maxUses, xp, priceMult);
        }
    }

    private static class ItemsForEmeraldBlock implements VillagerTrades.ITrade {
        public ItemStack sellingItem;
        public PriceInterval priceInfo;
        final int maxUses;
        final int xp;
        final float priceMult;

        public ItemsForEmeraldBlock(IItemProvider item, PriceInterval priceInfo, int maxUses, int xp, float priceMult) {
            this(new ItemStack(item), priceInfo, maxUses, xp, priceMult);
        }

        public ItemsForEmeraldBlock(ItemStack par1Item, PriceInterval priceInfo, int maxUses, int xp, float priceMult) {
            this.sellingItem = par1Item;
            this.priceInfo = priceInfo;
            this.maxUses = maxUses;
            this.xp = xp;
            this.priceMult = priceMult;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            int i = 1;
            if (this.priceInfo != null)
                i = this.priceInfo.getPrice(rand);
            ItemStack buying;
            ItemStack selling;
            if (i < 0) {
                buying = new ItemStack(Items.EMERALD_BLOCK);
                selling = Utils.copyStackWithAmount(sellingItem, -i);
            } else {
                buying = new ItemStack(Items.EMERALD_BLOCK, i);
                selling = sellingItem;
            }
            return new MerchantOffer(buying, selling, maxUses, xp, priceMult);
        }
    }

    private static class PriceInterval {
        private final int min;
        private final int max;

        private PriceInterval(int min, int max) {
            this.min = min;
            this.max = max;
        }

        int getPrice(Random rand) {
            return min >= max ? min : min + rand.nextInt(max - min + 1);
        }
    }

    */
}
