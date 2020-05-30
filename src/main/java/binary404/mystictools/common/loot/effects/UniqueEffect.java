package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.world.cabability.IUniqueSave;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniqueEffect {

    public static final Map<String, UniqueEffect> REGISTRY = new HashMap<>();

    private String id;
    private IUniqueEffect effect;

    private List<LootSet.LootSetType> applyToItems = new ArrayList<>();

    private UniqueEffect(IUniqueEffect effect) {
        this.effect = effect;
    }

    public boolean applyToItemType(LootSet.LootSetType type) {
        return applyToItems.contains(type);
    }

    protected UniqueEffect setItemTypes(LootSet.LootSetType... itemTypes) {
        for (LootSet.LootSetType type : itemTypes) {
            applyToItems.add(type);
        }

        return this;
    }

    public static UniqueEffect getById(String id) {
        UniqueEffect effect = REGISTRY.get(id);

        return effect;
    }

    public String getId() {
        return this.id;
    }

    public CompoundNBT getNbt() {
        CompoundNBT tag = new CompoundNBT();

        tag.putString("id", this.getId());
        tag.putInt("lvl", 1);

        return tag;
    }

    public static UniqueEffect getUniqueEffect(ItemStack stack) {
        CompoundNBT tag = LootNbtHelper.getLootCompound(stack, LootTags.LOOT_TAG_UNIQUE);
        return UniqueEffect.getById(tag.getString("id"));
    }

    protected static UniqueEffect create(String id, IUniqueEffect effect) {
        UniqueEffect effect1 = new UniqueEffect(effect);

        effect1.id = id;
        REGISTRY.put(id, effect1);
        return effect1;
    }

}
