package binary404.mystictools.common.items;

import binary404.mystictools.common.items.attribute.ModAttributes;
import net.minecraft.world.item.ItemStack;

public interface ILootItem {

    default float getModel(ItemStack stack) {
        return ModAttributes.LOOT_MODEL.getOrDefault(stack, -1).getValue(stack);
    }

}
