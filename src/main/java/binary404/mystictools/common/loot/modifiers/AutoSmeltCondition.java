package binary404.mystictools.common.loot.modifiers;

import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.effect.LootEffectAutoSmelt;
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

public class AutoSmeltCondition implements ILootCondition {

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack tool = lootContext.get(LootParameters.TOOL);
        List<Item> tools = new ArrayList<Item>();
        tools.add(ModItems.loot_axe);
        tools.add(ModItems.loot_pickaxe);
        tools.add(ModItems.loot_shovel);

        if (tool != null)
            if (tools.contains(tool.getItem())) {
                List<LootEffect> effects = LootEffect.getEffectList(tool);

                for (LootEffect effect : effects) {
                    if (effect == LootEffect.AUTO_SMELT) {
                        return ((LootEffectAutoSmelt) effect.getAction()).active(tool);
                    }
                }
            }
        return false;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return ModLootModifiers.AUTOSMELT;
    }

    public static class Serializer implements ILootSerializer<AutoSmeltCondition> {
        @Override
        public void func_230424_a_(JsonObject p_230424_1_, AutoSmeltCondition p_230424_2_, JsonSerializationContext p_230424_3_) {

        }

        @Override
        public AutoSmeltCondition func_230423_a_(JsonObject p_230423_1_, JsonDeserializationContext p_230423_2_) {
            return new AutoSmeltCondition();
        }
    }
}
