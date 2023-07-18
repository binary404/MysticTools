package binary404.mystictools.proxy;

import binary404.fx_lib.util.ClientTickHandler;
import binary404.mystictools.MysticTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

    @Override
    public void registerHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItemColors);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerPropertyGetters();
        });
    }

    public void registerItemColors(RegisterColorHandlersEvent.Item event) {
    }

    private static void registerPropertyGetter(ItemLike item, ResourceLocation id, ItemPropertyFunction propGetter) {
        ItemProperties.register(item.asItem(), id, propGetter);
    }

    private static void registerPropertyGetters() {
 /*       ItemPropertyFunction lootGetter = (stack, world, entity, index) -> {
            ILootItem item = ((ILootItem) stack.getItem());
            return item.getModel(stack);
        };

        registerPropertyGetter(ModItems.loot_sword.get(), new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_bow.get(), new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_shovel.get(), new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_pickaxe.get(), new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_axe.get(), new ResourceLocation("model"), lootGetter);

        registerPropertyGetter(ModItems.loot_boots.get(), new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_leggings.get(), new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_chestplate.get(), new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_helmet.get(), new ResourceLocation("model"), lootGetter);

        ItemPropertyFunction pulling = (stack, world, entity, index) -> {
            ItemLootBow bow = ((ItemLootBow) stack.getItem());
            return bow.getPulling(stack, world, entity);
        };

        ItemPropertyFunction pull = (stack, world, entity, index) -> {
            ItemLootBow bow = ((ItemLootBow) stack.getItem());
            return bow.getPull(stack, world, entity);
        };

        registerPropertyGetter(ModItems.loot_bow.get(), new ResourceLocation("ml_pull"), pull);
        registerPropertyGetter(ModItems.loot_bow.get(), new ResourceLocation("ml_pulling"), pulling);*/
    }

    @Override
    public void scheduleDelayed(Runnable r, int delay) {
        if (MysticTools.fxlibLoaded()) {
            ClientTickHandler.addRunnable(r, delay);
        }
    }

    @Override
    public Level getWorld() {
        return Minecraft.getInstance().level;
    }

    @Override
    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }
}
