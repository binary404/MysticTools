package binary404.mystictools.common.core;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.ritual.RitualRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MysticTools.modid);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MysticTools.modid);

    public static final RegistryObject<RitualRecipe.Serializer> RITUAL_SERIALIZER =
            SERIALIZERS.register("ritual", RitualRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<RitualRecipe>> RITUAL_TYPE =
            TYPES.register("ritual", () -> RecipeType.simple(new ResourceLocation("mystictools:ritual")));

}
