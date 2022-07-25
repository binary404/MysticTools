package binary404.mystictools.common.tile;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.*;
public class ModTiles {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MysticTools.modid);

    public static final RegistryObject<BlockEntityType<TileEntityCauldron>> CAULDRON = BLOCK_ENTITIES.register("cauldron",
            () -> BlockEntityType.Builder.of(TileEntityCauldron::new, ModBlocks.cauldron.get()).build(null));
}
