package binary404.mystictools.data;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.blocks.ModBlocks;
import binary404.mystictools.common.items.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ModItems.loot_case.get())
                .requires(ModItems.shard.get(), 9)
                .group("mystictools:case")
                .unlockedBy("has_item", has(ModItems.shard.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.cauldron.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('E', Tags.Items.GEMS_EMERALD)
                .pattern("IEI")
                .pattern("IEI")
                .pattern("III")
                .group("mystictools:cauldron")
                .unlockedBy("has_item", has(Blocks.CAULDRON))
                .save(consumer);
        RitualRecipeBuilder.builder(new ResourceLocation(MysticTools.modid, "test"))
                .requires(ModItems.shard.get(), 9)
                .requires(Items.DIAMOND, 2)
                .unlockedBy("has_item", has(ModItems.shard.get()))
                .save(consumer, new ResourceLocation(MysticTools.modid, "test"));
    }
}
