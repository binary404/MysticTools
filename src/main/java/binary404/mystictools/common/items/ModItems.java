package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    private static final Item.Properties properties = new Item.Properties().tab(MysticTools.tab);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MysticTools.modid);

    public static final RegistryObject<Item> loot_sword = ITEMS.register("loot_sword", () -> new ItemLootSword());
    public static final RegistryObject<Item> loot_axe = ITEMS.register("loot_axe", () -> new ItemLootAxe());
    public static final RegistryObject<Item> loot_pickaxe = ITEMS.register("loot_pickaxe", () -> new ItemLootPickaxe());
    public static final RegistryObject<Item> loot_shovel = ITEMS.register("loot_shovel", () -> new ItemLootShovel());
    public static final RegistryObject<Item> loot_bow = ITEMS.register("weaponbow", () -> new ItemLootBow());

    public static final RegistryObject<Item> loot_case = ITEMS.register("loot_case", () -> new ItemCase());
    public static final RegistryObject<Item> shard = ITEMS.register("shard", () -> new Item(properties));
    public static final RegistryObject<Item> artifact = ITEMS.register("artifact", () -> new ItemArtifact(properties));

    public static final RegistryObject<Item> pickaxe_case = ITEMS.register("pickaxe_case", () -> new ItemSelectTypeCase(loot_pickaxe.get()));
    public static final RegistryObject<Item> axe_case = ITEMS.register("axe_case", () -> new ItemSelectTypeCase(loot_axe.get()));
    public static final RegistryObject<Item> shovel_case = ITEMS.register("shovel_case", () -> new ItemSelectTypeCase(loot_shovel.get()));
    public static final RegistryObject<Item> sword_case = ITEMS.register("sword_case", () -> new ItemSelectTypeCase(loot_sword.get()));
    public static final RegistryObject<Item> bow_case = ITEMS.register("bow_case", () -> new ItemSelectTypeCase(loot_bow.get()));

    public static final RegistryObject<Item> loot_boots = ITEMS.register("loot_boots", () -> new ItemLootArmor(EquipmentSlot.FEET, "Boots"));
    public static final RegistryObject<Item> loot_leggings = ITEMS.register("loot_leggings", () -> new ItemLootArmor(EquipmentSlot.LEGS, "Leggings"));
    public static final RegistryObject<Item> loot_chestplate = ITEMS.register("loot_chestplate", () -> new ItemLootArmor(EquipmentSlot.CHEST, "Chestplate"));
    public static final RegistryObject<Item> loot_helmet = ITEMS.register("loot_helmet", () -> new ItemLootArmor(EquipmentSlot.HEAD, "Helmet"));

}
