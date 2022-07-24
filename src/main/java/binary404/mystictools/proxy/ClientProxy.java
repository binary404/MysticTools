package binary404.mystictools.proxy;

import binary404.mystictools.client.render.RenderCauldron;
import binary404.mystictools.client.fx.FXBlock;
import binary404.mystictools.common.core.ClientHandler;
import binary404.mystictools.common.items.ILootItem;
import binary404.mystictools.common.items.ItemLootBow;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.tile.ModTiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy implements IProxy {

    @Override
    public void registerHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientHandler.KeyBindings.init();
            registerPropertyGetters();
        });
        BlockEntityRenderers.register(ModTiles.CAULDRON, RenderCauldron::new);
    }

    private static void registerPropertyGetter(ItemLike item, ResourceLocation id, ItemPropertyFunction propGetter) {
        ItemProperties.register(item.asItem(), id, propGetter);
    }

    private static void registerPropertyGetters() {
        ItemPropertyFunction lootGetter = (stack, world, entity, index) -> {
            ILootItem item = ((ILootItem) stack.getItem());
            return item.getModel(stack);
        };

        registerPropertyGetter(ModItems.loot_sword, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_bow, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_shovel, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_pickaxe, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_axe, new ResourceLocation("model"), lootGetter);

        registerPropertyGetter(ModItems.loot_boots, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_leggings, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_chestplate, new ResourceLocation("model"), lootGetter);
        registerPropertyGetter(ModItems.loot_helmet, new ResourceLocation("model"), lootGetter);

        ItemPropertyFunction pulling = (stack, world, entity, index) -> {
            ItemLootBow bow = ((ItemLootBow) stack.getItem());
            return bow.getPulling(stack, world, entity);
        };

        ItemPropertyFunction pull = (stack, world, entity, index) -> {
            ItemLootBow bow = ((ItemLootBow) stack.getItem());
            return bow.getPull(stack, world, entity);
        };

        registerPropertyGetter(ModItems.loot_bow, new ResourceLocation("ml_pull"), pull);
        registerPropertyGetter(ModItems.loot_bow, new ResourceLocation("ml_pulling"), pulling);
    }

    @Override
    public void blockFX(BlockPos pos) {
        FXBlock data = new FXBlock(Minecraft.getInstance().level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, true, 600, getWorld().getBlockState(pos).getBlock());
        Minecraft.getInstance().particleEngine.add(data);
    }

    @Override
    public void scheduleDelayed(Runnable r, int delay) {
        System.out.println("Scheduling runnable on client");
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
