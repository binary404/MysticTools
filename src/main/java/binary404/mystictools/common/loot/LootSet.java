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

    public static final String[] TOOL_NAMES = {
            "The Pawpaw"
            , "The Single Wing"
            , "The Godzilla"
            , "The Picker"
            , "The Little Green"
            , "The Big Buddy"
            , "The Guzzler"
            , "The Wiggle Stick"
            , "The Kicker"
            , "The Ground Crusher"
            , "The Speed Miner 3000"
            , "The Kicker"
            , "The Sludge"
            , "The Commander"
            , "The Flat Nose"
            , "The Toothpick"
            , "The Measurer"
            , "The Belly"
            , "The Clicker"
            , "The Parrot"
            , "The Bigwig"
            , "The Drag Bag"
            , "The Chicken Beak"
            , "The Whiskers"
            , "The Fury"
            , "The Pokey"
            , "The Wedger"
            , "The Friendly One"
            , "The Goofy Hook"
            , "The Gobbler"
            , "The Dislocator"
            , "The Winger"
            , "The Knockout"
            , "The Pully"
            , "The Prickle"
            , "The Killer"
            , "The Band Aid"
            , "The Punisher"
            , "The Spiker"
            , "The Weeping Bell"
    };

    public static final String[] BOW_NAMES = {
            "Long Bow"
            , "Venom"
            , "Nat Bow"
            , "Black Widow"
            , "Eagle"
            , "Drawling"
            , "Zach"
            , "Slimestrike"
            , "Thunderforce"
            , "Ghost"
            , "Angel"
            , "Starshot"
    };

    public static final Map<LootSetType, String[]> LOOT_NAMES = new HashMap<>();

    static {
        LOOT_NAMES.put(LootSetType.SWORD, SWORD_NAMES);
        LOOT_NAMES.put(LootSetType.AXE, TOOL_NAMES);
        LOOT_NAMES.put(LootSetType.PICKAXE, TOOL_NAMES);
        LOOT_NAMES.put(LootSetType.SHOVEL, TOOL_NAMES);
        LOOT_NAMES.put(LootSetType.BOW, BOW_NAMES);
        LOOT_NAMES.put(LootSetType.ARMOR_BOOTS, TOOL_NAMES);
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
            return "NAME";

        String[] names = LOOT_NAMES.get(type);

        return names[rand.nextInt(names.length)];
    }

    public enum LootSetType {
        SWORD("sword", 76),
        PICKAXE("pickaxe", 16),
        AXE("axe", 21),
        SHOVEL("shovel", 15),
        BOW("bow", 12),
        ARMOR_BOOTS("armor_boots", 8);

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
