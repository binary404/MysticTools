package binary404.mystictools.data;

import binary404.mystictools.common.core.ModRecipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FinishedRitualRecipe implements FinishedRecipe {

    private final ResourceLocation id;
    private final ResourceLocation ritual;
    private final List<Ingredient> ingredients;
    private final Advancement.Builder advancement;
    private final ResourceLocation advancementId;

    public FinishedRitualRecipe(ResourceLocation id, ResourceLocation ritual, List<Ingredient> ingredients, Advancement.Builder advancement, ResourceLocation advancementId) {
        this.id = id;
        this.ritual = ritual;
        this.ingredients = ingredients;
        this.advancement = advancement;
        this.advancementId = advancementId;
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
        JsonArray array = new JsonArray();
        for (Ingredient ingredient : ingredients) {
            array.add(ingredient.toJson());
        }

        pJson.add("ingredients", array);
        pJson.addProperty("ritual", ritual.toString());
    }

    @Override
    public RecipeSerializer<?> getType() {
        return ModRecipes.RITUAL_SERIALIZER.get();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return this.advancement.serializeToJson();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return this.advancementId;
    }
}
