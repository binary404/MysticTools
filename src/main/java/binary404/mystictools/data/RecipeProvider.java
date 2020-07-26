package binary404.mystictools.data;

import binary404.mystictools.common.blocks.ModBlocks;
import binary404.mystictools.common.items.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {

    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapelessRecipe(ModItems.loot_case)
                .addIngredient(ModItems.shard, 9)
                .setGroup("mystictools:case")
                .addCriterion("has_item", hasItem(ModItems.shard))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.cauldron)
                .key('I', Tags.Items.INGOTS_IRON)
                .key('E', Tags.Items.GEMS_EMERALD)
                .patternLine("IEI")
                .patternLine("IEI")
                .patternLine("III")
                .setGroup("mystictools:cauldron")
                .addCriterion("has_item", hasItem(ModBlocks.cauldron))
                .build(consumer);
    }

    @Override
    public String getName() {
        return "Mystic Tools recipes";
    }
}
