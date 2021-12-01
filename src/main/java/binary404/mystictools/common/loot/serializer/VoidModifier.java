package binary404.mystictools.common.loot.serializer;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class VoidModifier extends LootModifier {

    protected VoidModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        return ret;
    }

    public static class Serializer extends GlobalLootModifierSerializer<VoidModifier> {
        @Override
        public VoidModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            return new VoidModifier(ailootcondition);
        }

        @Override
        public JsonObject write(VoidModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
