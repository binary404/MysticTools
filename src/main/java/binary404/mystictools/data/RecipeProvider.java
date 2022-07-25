package binary404.mystictools.data;

import binary404.mystictools.common.blocks.ModBlocks;
import binary404.mystictools.common.items.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.loot_case.get())
                .requires(ModItems.shard.get(), 9)
                .group("mystictools:case")
                .unlockedBy("has_item", has(ModItems.shard.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModBlocks.cauldron.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('E', Tags.Items.GEMS_EMERALD)
                .pattern("IEI")
                .pattern("IEI")
                .pattern("III")
                .group("mystictools:cauldron")
                .unlockedBy("has_item", has(Blocks.CAULDRON))
                .save(consumer);
    }

    @Override
    public String getName() {
        return "Mystic Tools recipes";
    }
}
