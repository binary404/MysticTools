package binary404.mystictools.common.loot.modifiers;

import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.effect.LootEffectAutoSmelt;
import binary404.mystictools.common.loot.effects.effect.LootEffectVoid;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.ArrayList;
import java.util.List;

public class VoidCondition implements ILootCondition {

    @Override
    public boolean test(LootContext lootContext) {
        if (lootContext != null) {
            ItemStack tool = lootContext.get(LootParameters.TOOL);
            List<Item> tools = new ArrayList<Item>();
            tools.add(ModItems.loot_axe);
            tools.add(ModItems.loot_pickaxe);
            tools.add(ModItems.loot_shovel);

            if (tool != null)
                if (tools.contains(tool.getItem())) {
                    List<LootEffect> effects = LootEffect.getEffectList(tool);

                    for (LootEffect effect : effects) {
                        if (effect == LootEffect.VOID) {
                            return ((LootEffectVoid) effect.getAction()).active(tool);
                        }
                    }
                }
        }
        return false;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return ModLootModifiers.VOID;
    }

    public static class Serializer implements ILootSerializer<VoidCondition> {
        @Override
        public void serialize(JsonObject p_230424_1_, VoidCondition p_230424_2_, JsonSerializationContext p_230424_3_) {

        }

        @Override
        public VoidCondition deserialize(JsonObject p_230423_1_, JsonDeserializationContext p_230423_2_) {
            return new VoidCondition();
        }
    }
}
