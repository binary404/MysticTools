package binary404.mystictools.common.items;

import binary404.mystictools.MysticTools;
import binary404.mystictools.common.core.config.ModConfigs;
import binary404.mystictools.common.core.helper.util.WeightedList;
import binary404.mystictools.common.loot.LootRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final List<RegistryObject<Item>> TAB_ITEMS = new ArrayList<>();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MysticTools.modid);
    public static final RegistryObject<Item> loot_sword = register("loot_sword", () -> new ItemLootSword());
    public static final RegistryObject<Item> sword_case = register("sword_case", () -> new ItemSelectTypeCase(loot_sword.get()));
    public static final RegistryObject<Item> loot_axe = register("loot_axe", () -> new ItemLootAxe());
    public static final RegistryObject<Item> axe_case = register("axe_case", () -> new ItemSelectTypeCase(loot_axe.get()));
    public static final RegistryObject<Item> loot_pickaxe = register("loot_pickaxe", () -> new ItemLootPickaxe());
    public static final RegistryObject<Item> pickaxe_case = register("pickaxe_case", () -> new ItemSelectTypeCase(loot_pickaxe.get()));
    public static final RegistryObject<Item> loot_shovel = register("loot_shovel", () -> new ItemLootShovel());
    public static final RegistryObject<Item> shovel_case = register("shovel_case", () -> new ItemSelectTypeCase(loot_shovel.get()));
    public static final RegistryObject<Item> loot_bow = register("weaponbow", () -> new ItemLootBow());
    public static final RegistryObject<Item> bow_case = register("bow_case", () -> new ItemSelectTypeCase(loot_bow.get()));
    public static final RegistryObject<Item> loot_case = register("loot_case", () -> new ItemCase());
    public static final RegistryObject<Item> rarity_case = ITEMS.register("rarity_case", () -> new ItemSelectRarityCase());
    public static final RegistryObject<Item> loot_boots = register("loot_boots", () -> new ItemLootArmor(EquipmentSlot.FEET, "Boots"));
    public static final RegistryObject<Item> loot_leggings = register("loot_leggings", () -> new ItemLootArmor(EquipmentSlot.LEGS, "Leggings"));
    public static final RegistryObject<Item> loot_chestplate = register("loot_chestplate", () -> new ItemLootArmor(EquipmentSlot.CHEST, "Chestplate"));
    public static final RegistryObject<Item> loot_helmet = register("loot_helmet", () -> new ItemLootArmor(EquipmentSlot.HEAD, "Helmet"));
    private static final Item.Properties properties = new Item.Properties();
    public static final RegistryObject<Item> shard = register("shard", () -> new Item(properties));
    public static final RegistryObject<Item> artifact = register("artifact", () -> new ItemArtifact(properties));

    @SubscribeEvent
    public static void buildContents(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MysticTools.modid, "main"), builder -> {
            builder.title(Component.translatable("item_group." + MysticTools.modid + ".main"))
                    .icon(() -> new ItemStack(loot_case.get()))
                    .displayItems((enabledFlags, populator, hasPermissions) -> {
                        for (RegistryObject<Item> item : TAB_ITEMS) {
                            populator.accept(item.get());
                        }
                        for (WeightedList.Entry<LootRarity> rarity : ModConfigs.RARITIES.RARITIES) {
                            ItemStack stack = new ItemStack(rarity_case.get());
                            ItemSelectRarityCase.setLootRarity(rarity.value, stack);
                            populator.accept(stack);
                        }
                    });
        });
    }

    public static RegistryObject<Item> register(String name, Supplier<Item> item) {
        RegistryObject<Item> object = ITEMS.register(name, item);
        TAB_ITEMS.add(object);
        return object;
    }
}
