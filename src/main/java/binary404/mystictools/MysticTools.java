package binary404.mystictools;

import binary404.mystictools.common.core.ConfigHandler;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.ItemTypeRegistry;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.modifiers.ModLootModifiers;
import binary404.mystictools.common.network.NetworkHandler;
import binary404.mystictools.proxy.ClientProxy;
import binary404.mystictools.proxy.IProxy;
import binary404.mystictools.proxy.ServerProxy;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mystictools")
public class MysticTools {

    public static MysticTools instance;
    public static IProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger("mystictools");

    public static ItemGroup tab = new ItemGroup("mystictools") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.loot_case);
        }
    };

    public MysticTools() {
        instance = this;
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        proxy.registerHandlers();
        proxy.attachEventHandlers(MinecraftForge.EVENT_BUS);
        proxy.attachEventHandlers(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        ModLootModifiers.init();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        proxy.init();

        PotionEffect.init();
        LootRarity.init();

        NetworkHandler.init();

        DeferredWorkQueue.runLater(() -> {
            ItemTypeRegistry.register(ModItems.loot_sword, LootSet.LootSetType.SWORD);
            ItemTypeRegistry.register(ModItems.loot_axe, LootSet.LootSetType.AXE);
            ItemTypeRegistry.register(ModItems.loot_pickaxe, LootSet.LootSetType.PICKAXE);
            ItemTypeRegistry.register(ModItems.loot_shovel, LootSet.LootSetType.SHOVEL);
            ItemTypeRegistry.register(ModItems.loot_bow, LootSet.LootSetType.BOW);
        });
    }

}
