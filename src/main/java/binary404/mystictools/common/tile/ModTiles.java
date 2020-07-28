package binary404.mystictools.common.tile;

import binary404.mystictools.common.blocks.ModBlocks;
import binary404.mystictools.common.core.RegistryHelper;
import binary404.mystictools.common.tile.TileEntityCauldron;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTiles {

    @ObjectHolder("mystictools:cauldron")
    public static TileEntityType<TileEntityCauldron> CAULDRON;

    @ObjectHolder("mystictools:upgrader")
    public static TileEntityType<TileEntityUpgrader> UPGRADER;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
        RegistryHelper.register(r, TileEntityType.Builder.create(TileEntityCauldron::new, ModBlocks.cauldron).build(null), "cauldron");
        RegistryHelper.register(r, TileEntityType.Builder.create(TileEntityUpgrader::new, ModBlocks.upgrader).build(null), "upgrader");
    }

}
