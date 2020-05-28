package binary404.mystictools.common.loot;

import java.util.*;

public class LootSet {
    public static final Map<String, LootSet> REGISTRY = new HashMap<>();

    public final int model;

    protected String id;

    public static final String[] SWORD_NAMES = {
            "Blade of a Thousand Cuts",
            "Last Rites",
            "Willbreaker",
            "Fragile Blade",
            "Bloodvenom Claymore",
            "Lightning Gold Sabre",
            "Thunder Skeletal Greatsword",
            "Draughtbane, Token of Subtlety",
            "Limbo, Memory of the End",
            "Blackout, Bond of Power",
            "Stormbringer",
            "Wit's End",
            "Stinger",
            "Burnished Quickblade",
            "Extinction Katana",
            "Proud Copper Reaver",
            "Polished Adamantite Greatsword",
            "Oathbreaker, Heirloom of the Gladiator",
            "Arondite, Last Stand of the World"
    };

    public static final Map<LootSetType, String[]> LOOT_NAMES = new HashMap<>();

    static {
        LOOT_NAMES.put(LootSetType.SWORD, SWORD_NAMES);
    }

    protected LootSet(int model) {
        this.model = model;
    }

    protected static LootSet get(String name) {
        LootSet set = new LootSet(REGISTRY.size() + 1);
        set.id = name;

        REGISTRY.put(name, set);
        return set;
    }

    public static LootSet getRandom(Random rand) {
        List<String> keys = new ArrayList<>(REGISTRY.keySet());

        String setId = keys.get(rand.nextInt(keys.size()));

        return getById(setId);
    }

    public static LootSet getById(String id) {
        LootSet lootSet;

        if (id.length() > 0 && REGISTRY.containsKey(id))
            lootSet = REGISTRY.get(id);
        else
            lootSet = null;
        return lootSet;
    }

    public String getId() {
        return this.id;
    }

    public static String getNameForType(LootSetType type, Random rand) {
        if (!LOOT_NAMES.containsKey(type))
            return "";

        String[] names = LOOT_NAMES.get(type);

        return names[rand.nextInt(names.length)];
    }

    public enum LootSetType {
        SWORD("sword", 76);

        public final int models;
        private String id;

        private LootSetType(String typeId, int totalModels) {
            this.id = typeId;
            this.models = totalModels;
        }

        public String getId() {
            return this.id;
        }
    }

}
