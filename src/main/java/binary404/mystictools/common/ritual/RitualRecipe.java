package binary404.mystictools.common.ritual;

import binary404.mystictools.common.core.ModRecipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
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
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class RitualRecipe implements Recipe<RecipeWrapper> {
    private final ResourceLocation id;
    private final ResourceLocation ritual;
    private final NonNullList<Ingredient> ingredients;

    public RitualRecipe(ResourceLocation id, ResourceLocation ritual, NonNullList<Ingredient> ingredients) {
        this.id = id;
        this.ritual = ritual;
        this.ingredients = ingredients;
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
        StackedContents contents = new StackedContents();
        int i = 0;
        for(int j = 0; j < wrapper.getContainerSize(); j++) {
            ItemStack stack = wrapper.getItem(j);
            if(!stack.isEmpty()) {
                i++;
                contents.accountStack(stack);
            }
        }
        return i == this.ingredients.size() && contents.canCraft(this, null);
    }

    @Override
    public ItemStack assemble(RecipeWrapper pContainer) {
        return null;
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
            NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(pJson, "ingredients"));

            ResourceLocation ritual = new ResourceLocation(GsonHelper.getAsString(pJson, "ritual"));

            return new RitualRecipe(pRecipeId, ritual, ingredients);
        }

        @Override
        public @Nullable RitualRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int i = pBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            for(int j = 0; j < ingredients.size(); j++) {
                ingredients.set(j, Ingredient.fromNetwork(pBuffer));
            }

            ResourceLocation ritual = new ResourceLocation(pBuffer.readUtf());
            return new RitualRecipe(pRecipeId, ritual, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, RitualRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.ingredients.size());
            for(Ingredient ingredient : pRecipe.ingredients) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeUtf(pRecipe.ritual.toString());
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < pIngredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i));
                if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }
    }
}
