package binary404.mystictools.common.blocks;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.items.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    private static final Item.Properties props = new Item.Properties().tab(MysticTools.tab);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MysticTools.modid);
    public static final DeferredRegister<Item> ITEM_BLOCKS = DeferredRegister.create(ForgeRegistries.ITEMS, MysticTools.modid);

    public static RegistryObject<Block> cauldron = BLOCKS.register("cauldron", () -> new BlockCauldron(BlockBehaviour.Properties.of(Material.STONE).strength(3.5f).sound(SoundType.ANVIL)));
    public static RegistryObject<Item> cauldronItem = ITEM_BLOCKS.register("cauldron", () -> new BlockItem(cauldron.get(), props));

    public static Block mysterious_stone;
    public static Block mysterious_bricks;

    public static Block peridot_ore;
}
