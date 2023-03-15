package binary404.mystictools.common.core.helper.util;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RecipeUtils {

    public static boolean matches(List<Ingredient> inputs, Container inv, @Nullable IntSet usedSlots, boolean doCheck) {
        List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack input = inv.getItem(i);
            if (input.isEmpty()) {
                break;
            }

            int stackIndex = -1;

            for (int k = 0; k < input.getCount(); k++) {
                for (int j = 0; j < ingredientsMissing.size(); j++) {
                    Ingredient ingr = ingredientsMissing.get(j);
                    if (ingr.test(input)) {
                        stackIndex = j;
                        if (doCheck && usedSlots != null) {
                            usedSlots.add(i);
                        }
                        break;
                    }
                }

                if (stackIndex != -1) {
                    ingredientsMissing.remove(stackIndex);
                    stackIndex = -1;
                } else {
                    return false;
                }
            }
        }

        return ingredientsMissing.isEmpty();
    }

    public static void consume(List<Ingredient> inputs, Container inv, @Nullable IntSet usedSlots) {
        List<Ingredient> ingredientsUsed = new ArrayList<>(inputs);

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack input = inv.getItem(i);
            if (input.isEmpty()) {
                break;
            }

            for (int k = 0; k < input.getCount(); k++) {
                Iterator<Ingredient> iterator = ingredientsUsed.iterator();
                while (iterator.hasNext()) {
                    Ingredient ingr = iterator.next();
                    if (ingr.test(input)) {
                        input.shrink(1);
                        iterator.remove();
                    }
                }
            }
        }
    }
}
