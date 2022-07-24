package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

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

    public static Item dice;
    public static Item charm;

    public static Item pickaxe_case;
    public static Item axe_case;
    public static Item shovel_case;
    public static Item bow_case;
    public static Item sword_case;

    public static List<Item> rarityCases = new ArrayList<>();

    public static Item loot_boots;
    public static Item loot_leggings;
    public static Item loot_chestplate;
    public static Item loot_helmet;

    public static Item artifact;

    public static Item peridot;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        loot_sword = register(r, new ItemLootSword(), "loot_sword");
        loot_axe = register(r, new ItemLootAxe(), "loot_axe");
        loot_shovel = register(r, new ItemLootShovel(), "loot_shovel");
        loot_pickaxe = register(r, new ItemLootPickaxe(), "loot_pickaxe");
        loot_bow = register(r, new ItemLootBow(), "weaponbow");

        loot_boots = register(r, new ItemLootArmor(EquipmentSlot.FEET, "Boots"), "loot_boots");
        loot_leggings = register(r, new ItemLootArmor(EquipmentSlot.LEGS, "Leggings"), "loot_leggings");
        loot_chestplate = register(r, new ItemLootArmor(EquipmentSlot.CHEST, "Chestplate"), "loot_chestplate");
        loot_helmet = register(r, new ItemLootArmor(EquipmentSlot.HEAD, "Helmet"), "loot_helmet");

        register(r, new ItemCase(), "loot_case");

        pickaxe_case = register(r, new ItemSelectTypeCase(loot_pickaxe), "pickaxe_case");
        axe_case = register(r, new ItemSelectTypeCase(loot_axe), "axe_case");
        shovel_case = register(r, new ItemSelectTypeCase(loot_shovel), "shovel_case");
        bow_case = register(r, new ItemSelectTypeCase(loot_bow), "bow_case");
        sword_case = register(r, new ItemSelectTypeCase(loot_sword), "sword_case");

        artifact = register(r, new ItemArtifact(new Item.Properties()), "artifact");

        shard = register(r, new Item(new Item.Properties().tab(MysticTools.tab)), "shard");
        dice = register(r, new Item(new Item.Properties().tab(MysticTools.tab)), "dice");
        charm = register(r, new Item(new Item.Properties().tab(MysticTools.tab)), "charm");

        //register(r, new Item(new Item.Properties().tab(MysticTools.tab)), "peridot");
    }

}
