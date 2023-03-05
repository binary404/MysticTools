package binary404.mystictools.common.ritual;

import binary404.mystictools.common.core.ModRecipes;
import binary404.mystictools.common.core.helper.util.RecipeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RitualRecipe implements Recipe<RecipeWrapper> {
    private final ResourceLocation id;
    private final ResourceLocation ritual;
    private final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;

    public RitualRecipe(ResourceLocation id, ResourceLocation ritual, Ingredient... inputs) {
        this.id = id;
        this.ritual = ritual;
        this.ingredients = NonNullList.of(Ingredient.EMPTY, inputs);
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.RITUAL_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.RITUAL_TYPE.get();
    }

    @Override
    public boolean matches(RecipeWrapper wrapper, Level pLevel) {
        return RecipeUtils.matches(this.ingredients, wrapper, null, false);
    }

    @Override
    public ItemStack assemble(RecipeWrapper pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public ResourceLocation getRitual() {
        return ritual;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public static class Serializer implements RecipeSerializer<RitualRecipe> {

        @Override
        public RitualRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            JsonArray ingrs = GsonHelper.getAsJsonArray(pJson, "ingredients");
            List<Ingredient> inputs = new ArrayList<>();
            for(JsonElement e : ingrs) {
                inputs.add(Ingredient.fromJson(e));
            }
            ResourceLocation ritual = new ResourceLocation(GsonHelper.getAsString(pJson, "ritual"));

            return new RitualRecipe(pRecipeId, ritual, inputs.toArray(new Ingredient[0]));
        }

        @Override
        public @Nullable RitualRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int i = pBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < ingredients.size(); j++) {
                ingredients.set(j, Ingredient.fromNetwork(pBuffer));
            }

            ResourceLocation ritual = new ResourceLocation(pBuffer.readUtf());
            return new RitualRecipe(pRecipeId, ritual, ingredients.toArray(new Ingredient[0]));
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, RitualRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.ingredients.size());
            for (Ingredient ingredient : pRecipe.ingredients) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeUtf(pRecipe.ritual.toString());
        }
    }
}
