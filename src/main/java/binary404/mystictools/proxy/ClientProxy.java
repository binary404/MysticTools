package binary404.mystictools.proxy;

import binary404.mystictools.client.RenderCauldron;
import binary404.mystictools.common.core.ClientHandler;
import binary404.mystictools.common.items.ILootItem;
import binary404.mystictools.common.items.ItemLootBow;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.tile.ModTiles;
import net.minecraft.client.Minecraft;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

    @Override
    public void registerHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            ClientHandler.KeyBindings.init();
            registerPropertyGetters();
        });
        ClientRegistry.bindTileEntityRenderer(ModTiles.CAULDRON, RenderCauldron::new);
    }

    private static void registerPropertyGetter(IItemProvider item, ResourceLocation id, IItemPropertyGetter propGetter) {
        ItemModelsProperties.func_239418_a_(item.asItem(), id, propGetter);
    }

    private static void registerPropertyGetters() {
        IItemPropertyGetter lootGetter = (stack, world, entity) -> {
            ILootItem item = ((ILootItem) stack.getItem());
            return item.getModel(stack);
        };
        registerPropertyGetter(ModItems.loot_sword, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_bow, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_shovel, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_pickaxe, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_axe, new ResourceLocation("model"), lootGetter);

        IItemPropertyGetter pulling = (stack, world, entity) -> {
            ItemLootBow bow = ((ItemLootBow) stack.getItem());
            return bow.getPulling(stack, world, entity);
        };

        IItemPropertyGetter pull = (stack, world, entity) -> {
            ItemLootBow bow = ((ItemLootBow) stack.getItem());
            return bow.getPull(stack, world, entity);
        };

        registerPropertyGetter(ModItems.loot_bow, new ResourceLocation("ml_pull"), pull);
        registerPropertyGetter(ModItems.loot_bow, new ResourceLocation("ml_pulling"), pulling);
    }

    @Override
    public World getWorld() {
        return Minecraft.getInstance().world;
    }
}
