package binary404.mystictools;

import binary404.mystictools.common.blocks.ModBlocks;
import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.core.GenerateLootCommand;
import binary404.mystictools.common.core.config.ModConfigs;
import binary404.mystictools.common.effect.ModPotions;
import binary404.mystictools.common.entity.ModEntities;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.ItemTypeRegistry;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.modifiers.LootModifiers;
import binary404.mystictools.common.loot.serializer.ModLootModifiers;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.common.tile.ModTiles;
import binary404.mystictools.proxy.ClientProxy;
import binary404.mystictools.proxy.IProxy;
import binary404.mystictools.proxy.ServerProxy;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mystictools")
public class MysticTools {

    public static MysticTools instance;
    public static IProxy proxy = new IProxy() {
    };

    public static final Logger LOGGER = LogManager.getLogger("mystictools");

    public static final String modid = "mystictools";

    public static CreativeModeTab tab = new CreativeModeTab("mystictools") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.loot_case.get());
        }
    };

    public MysticTools() {
        instance = this;
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
        proxy.registerHandlers();
        proxy.attachEventHandlers(MinecraftForge.EVENT_BUS);
        proxy.attachEventHandlers(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::parallelDispatch);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.ITEM_BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModTiles.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLootModifiers.GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLootModifiers.CONDITION.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModPotions.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void parallelDispatch(ParallelDispatchEvent event) {
    }

    private void registerCommands(RegisterCommandsEvent event) {
        //GenerateLootCommand.register(event.getDispatcher());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LootRarity.init();
        PotionEffect.init();
        LootEffect.init();
        LootModifiers.ArtifactModifiers.init();
        ModConfigs.register();

        NetworkHandler.init();

        event.enqueueWork(() -> {
            ItemTypeRegistry.register(ModItems.loot_sword.get(), LootSet.LootSetType.SWORD);
            ItemTypeRegistry.register(ModItems.loot_axe.get(), LootSet.LootSetType.AXE);
            ItemTypeRegistry.register(ModItems.loot_pickaxe.get(), LootSet.LootSetType.PICKAXE);
            ItemTypeRegistry.register(ModItems.loot_shovel.get(), LootSet.LootSetType.SHOVEL);
            ItemTypeRegistry.register(ModItems.loot_bow.get(), LootSet.LootSetType.BOW);
            ItemTypeRegistry.register(ModItems.loot_boots.get(), LootSet.LootSetType.ARMOR_BOOTS);
            ItemTypeRegistry.register(ModItems.loot_leggings.get(), LootSet.LootSetType.ARMOR_LEGGINGS);
            ItemTypeRegistry.register(ModItems.loot_chestplate.get(), LootSet.LootSetType.ARMOR_CHESTPLATE);
            ItemTypeRegistry.register(ModItems.loot_helmet.get(), LootSet.LootSetType.ARMOR_HELMET);
            ModConfigs.RARITIES.uploadRaritiesToRegistry();
            ModConfigs.EFFECTS.uploadEffectsToRegistry();
        });
    }

}
