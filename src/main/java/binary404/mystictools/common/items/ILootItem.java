package binary404.mystictools.common.items;

import binary404.mystictools.common.items.attribute.ModAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public interface ILootItem {

    default float getModel(ItemStack stack) {
        return ModAttributes.LOOT_MODEL.getOrDefault(stack, -1).getValue(stack);
    }
}
