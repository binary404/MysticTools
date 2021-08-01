package binary404.mystictools.common.blocks;

import binary404.mystictools.MysticTools;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.mystictools.common.core.RegistryHelper.register;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    @ObjectHolder("mystictools:cauldron")
    public static Block cauldron;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();

        Block.Properties builder = Block.Properties.of(Material.STONE).strength(3.5F).sound(SoundType.ANVIL);

        register(r, new BlockCauldron(builder), "cauldron");
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        Item.Properties props = new Item.Properties().tab(MysticTools.tab);

        register(r, new BlockItem(cauldron, props), "cauldron");
    }

}
