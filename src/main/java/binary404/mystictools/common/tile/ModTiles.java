package binary404.mystictools.common.tile;

import binary404.mystictools.common.blocks.ModBlocks;
import binary404.mystictools.common.core.RegistryHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTiles {

    @ObjectHolder("mystictools:cauldron")
    public static BlockEntityType<TileEntityCauldron> CAULDRON;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<BlockEntityType<?>> event) {
        IForgeRegistry<BlockEntityType<?>> r = event.getRegistry();
        RegistryHelper.register(r, BlockEntityType.Builder.of(TileEntityCauldron::new, ModBlocks.cauldron).build(null), "cauldron");
    }

}
