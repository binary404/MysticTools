package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.RegistryHelper;
import binary404.mystictools.common.loot.ItemTypeRegistry;
import binary404.mystictools.common.loot.LootRarity;
import binary404.mystictools.common.loot.LootSet;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.mystictools.common.core.RegistryHelper.register;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item loot_sword;

    public static Item loot_axe;

    public static Item loot_pickaxe;

    public static Item loot_shovel;

    @ObjectHolder("mystictools:loot_case")
    public static Item loot_case;

    public static Item loot_bow;

    @ObjectHolder("mystictools:shard")
    public static Item shard;

    @ObjectHolder("mystictools:dice")
    public static Item dice;

    @ObjectHolder("mystictools:charm")
    public static Item charm;

    public static Item pickaxe_case;
    public static Item axe_case;
    public static Item shovel_case;
    public static Item bow_case;
    public static Item sword_case;

    public static Item common_case;
    public static Item uncommon_case;
    public static Item rare_case;
    public static Item epic_case;
    public static Item unique_case;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        LootRarity.init();

        loot_sword = register(r, new ItemLootSword(), "loot_sword");
        loot_axe = register(r, new ItemLootAxe(), "loot_axe");
        loot_shovel = register(r, new ItemLootShovel(), "loot_shovel");
        loot_pickaxe = register(r, new ItemLootPickaxe(), "loot_pickaxe");
        loot_bow = register(r, new ItemLootBow(), "weaponbow");
        register(r, new ItemCase(), "loot_case");

        pickaxe_case = register(r, new ItemSelectTypeCase(loot_pickaxe), "pickaxe_case");
        axe_case = register(r, new ItemSelectTypeCase(loot_axe), "axe_case");
        shovel_case = register(r, new ItemSelectTypeCase(loot_shovel), "shovel_case");
        bow_case = register(r, new ItemSelectTypeCase(loot_bow), "bow_case");
        sword_case = register(r, new ItemSelectTypeCase(loot_sword), "sword_case");

        common_case = register(r, new ItemSelectRarityCase(LootRarity.COMMON), "common_case");
        uncommon_case = register(r, new ItemSelectRarityCase(LootRarity.UNCOMMON), "uncommon_case");
        rare_case = register(r, new ItemSelectRarityCase(LootRarity.RARE), "rare_case");
        epic_case = register(r, new ItemSelectRarityCase(LootRarity.EPIC), "epic_case");
        unique_case = register(r, new ItemSelectRarityCase(LootRarity.UNIQUE), "unique_case");

        register(r, new Item(new Item.Properties().group(MysticTools.tab)), "shard");
        register(r, new Item(new Item.Properties().group(MysticTools.tab)), "dice");
        register(r, new Item(new Item.Properties().group(MysticTools.tab)), "charm");

    }

}
