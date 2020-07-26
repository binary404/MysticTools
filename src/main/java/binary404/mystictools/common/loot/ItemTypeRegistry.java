package binary404.mystictools.common.loot;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemTypeRegistry {

    public static final Map<Item, LootSet.LootSetType> ITEMTYPEREGISTRY = new HashMap<Item, LootSet.LootSetType>();

    public static void register(Item item, LootSet.LootSetType type)
    {
        ITEMTYPEREGISTRY.put(item, type);
    }

    public static LootSet.LootSetType get(Item item)
    {
        return ITEMTYPEREGISTRY.get(item);
    }

}
