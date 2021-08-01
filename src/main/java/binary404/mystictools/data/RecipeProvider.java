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
        ShapelessRecipeBuilder.shapeless(ModItems.loot_case)
                .requires(ModItems.shard, 9)
                .group("mystictools:case")
                .unlockedBy("has_item", has(ModItems.shard))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModBlocks.cauldron)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('E', Tags.Items.GEMS_EMERALD)
                .pattern("IEI")
                .pattern("IEI")
                .pattern("III")
                .group("mystictools:cauldron")
                .unlockedBy("has_item", has(Blocks.CAULDRON))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.charm)
                .define('S', ModItems.shard)
                .define('E', Tags.Items.GEMS_EMERALD)
                .define('C', ModItems.loot_case)
                .pattern("SES")
                .pattern("ECE")
                .pattern("SES")
                .group("mystictools:charm")
                .unlockedBy("has_item", has(ModItems.shard))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.dice)
                .define('D', Blocks.DIAMOND_BLOCK)
                .define('E', Blocks.EMERALD_BLOCK)
                .define('C', ModItems.loot_case)
                .pattern("DCD")
                .pattern("CEC")
                .pattern("DCD")
                .group("mystictools:dice")
                .unlockedBy("has_item", has(ModItems.loot_case))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.charm)
                .define('S', ModItems.shard)
                .define('E', Tags.Items.GEMS_EMERALD)
                .define('C', ModItems.loot_case)
                .pattern("SES")
                .pattern("ECE")
                .pattern("SES")
                .group("mystictools:charm")
                .unlockedBy("has_item", has(ModItems.shard))
                .save(consumer);
        ShapedRecipeBuilder.shaped(ModItems.dice)
                .define('D', Blocks.DIAMOND_BLOCK)
                .define('E', Blocks.EMERALD_BLOCK)
                .define('C', ModItems.loot_case)
                .pattern("DCD")
                .pattern("CEC")
                .pattern("DCD")
                .group("mystictools:dice")
                .unlockedBy("has_item", has(ModItems.loot_case))
                .save(consumer);
    }

    @Override
    public String getName() {
        return "Mystic Tools recipes";
    }
}
