package binary404.mystictools.common.loot.modifiers;

import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.effect.LootEffectAutoSmelt;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.ArrayList;
import java.util.List;

public class AutoSmeltCondition implements LootItemCondition {

    @Override
    public boolean test(LootContext lootContext) {
        if (lootContext != null) {
            ItemStack tool = lootContext.getParamOrNull(LootContextParams.TOOL);
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
        }
        return false;
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootModifiers.AUTOSMELT;
    }


    public static class AutoSmeltSerializer implements Serializer<AutoSmeltCondition> {

        @Override
        public void serialize(JsonObject p_230424_1_, AutoSmeltCondition p_230424_2_, JsonSerializationContext p_230424_3_) {

        }

        @Override
        public AutoSmeltCondition deserialize(JsonObject p_230423_1_, JsonDeserializationContext p_230423_2_) {
            return new AutoSmeltCondition();
        }
    }
}
