package binary404.mystictools.data;

import com.google.common.collect.Lists;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public class RitualRecipeBuilder {

    private final ResourceLocation result;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public RitualRecipeBuilder(ResourceLocation result) {
        this.result = result;
    }

    public static RitualRecipeBuilder builder(ResourceLocation ritual) {
        return new RitualRecipeBuilder(ritual);
    }

    public RitualRecipeBuilder requires(TagKey<Item> pTag) {
        return this.requires(Ingredient.of(pTag));
    }

    public RitualRecipeBuilder requires(ItemLike pItem) {
        return this.requires(pItem, 1);
    }

    public RitualRecipeBuilder requires(ItemLike pItem, int pQuantity) {
        for (int i = 0; i < pQuantity; i++) {
            this.requires(Ingredient.of(pItem));
        }

        return this;
    }

    public RitualRecipeBuilder requires(Ingredient pIngredient) {
        return this.requires(pIngredient, 1);
    }

    public RitualRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
        for (int i = 0; i < pQuantity; i++) {
            this.ingredients.add(pIngredient);
        }

        return this;
    }

    public RitualRecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    public ResourceLocation getResult() {
        return result;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
        pFinishedRecipeConsumer.accept(new FinishedRitualRecipe(pRecipeId, this.result, this.ingredients, this.advancement, pRecipeId.withPrefix("recipes/")));
    }
}
