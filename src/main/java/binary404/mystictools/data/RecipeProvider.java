package binary404.mystictools.data;

import binary404.mystictools.common.blocks.ModBlocks;
import binary404.mystictools.common.items.ModItems;
import net.minecraft.block.Blocks;
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
                .addCriterion("has_item", hasItem(Blocks.CAULDRON))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.upgrader)
                .key('A', Blocks.ANVIL)
                .key('I', Tags.Items.INGOTS_IRON)
                .key('S', ModItems.shard)
                .key('D', Tags.Items.GEMS_DIAMOND)
                .patternLine("IAI")
                .patternLine("SDS")
                .patternLine("IDI")
                .setGroup("mystictools:upgrader")
                .addCriterion("has_item", hasItem(ModItems.shard))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.charm)
                .key('S', ModItems.shard)
                .key('E', Tags.Items.GEMS_EMERALD)
                .key('C', ModItems.loot_case)
                .patternLine("SES")
                .patternLine("ECE")
                .patternLine("SES")
                .setGroup("mystictools:charm")
                .addCriterion("has_item", hasItem(ModItems.shard))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModItems.dice)
                .key('D', Blocks.DIAMOND_BLOCK)
                .key('E', Blocks.EMERALD_BLOCK)
                .key('C', ModItems.loot_case)
                .patternLine("DCD")
                .patternLine("CEC")
                .patternLine("DCD")
                .setGroup("mystictools:dice")
                .addCriterion("has_item", hasItem(ModItems.loot_case))
                .build(consumer);
    }

    @Override
    public String getName() {
        return "Mystic Tools recipes";
    }
}
