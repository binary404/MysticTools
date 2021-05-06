package binary404.mystictools.common.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class VoidModifier extends LootModifier {

    protected VoidModifier(ILootCondition[] conditionsIn) {
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
        public VoidModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new VoidModifier(ailootcondition);
        }

        @Override
        public JsonObject write(VoidModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
