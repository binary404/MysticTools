package binary404.mystictools.common.items.attribute;

import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.LootEffectInstance;
import binary404.mystictools.common.loot.effects.PotionEffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {

    public static Map<ResourceLocation, ItemNBTAttribute<?, ?>> REGISTRY = new HashMap<>();

    public static ItemNBTAttribute<Double, DoubleAttribute> ADD_DAMAGE;
    public static ItemNBTAttribute<Float, FloatAttribute> ADD_SPEED;
    public static ItemNBTAttribute<Double, DoubleAttribute> ADD_EFFICIENCY;
    public static ItemNBTAttribute<Integer, IntegerAttribute> ADD_DURABILITY;
    public static ItemNBTAttribute<Double, DoubleAttribute> ADD_DRAWSPEED;
    public static ItemNBTAttribute<Double, DoubleAttribute> ADD_POWER;
    public static ItemNBTAttribute<Double, DoubleAttribute> ADD_ARMOR;
    public static ItemNBTAttribute<Double, DoubleAttribute> ADD_TOUGHNESS;

    public static ItemNBTAttribute<Integer, IntegerAttribute> LOOT_MODEL;
    public static ItemNBTAttribute<String, StringAttribute> LOOT_UUID;
    public static ItemNBTAttribute<String, StringAttribute> LOOT_RARITY;
    public static ItemNBTAttribute<Double, DoubleAttribute> LOOT_DAMAGE;
    public static ItemNBTAttribute<Float, FloatAttribute> LOOT_SPEED;
    public static ItemNBTAttribute<Double, DoubleAttribute> LOOT_EFFICIENCY;
    public static ItemNBTAttribute<Integer, IntegerAttribute> LOOT_DURABILITY;
    public static ItemNBTAttribute<Double, DoubleAttribute> LOOT_DRAWSPEED;
    public static ItemNBTAttribute<Double, DoubleAttribute> LOOT_POWER;
    public static ItemNBTAttribute<Double, DoubleAttribute> LOOT_ARMOR;
    public static ItemNBTAttribute<Double, DoubleAttribute> LOOT_TOUGHNESS;
    public static ItemNBTAttribute<Integer, IntegerAttribute> LOOT_EFFECT_LEVEL;
    public static ItemNBTAttribute<List<PotionEffectInstance>, PotionEffectAttribute> LOOT_POTION_EFFECTS;
    public static ItemNBTAttribute<List<LootEffectInstance>, LootEffectAttribute> LOOT_EFFECTS;

    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        ADD_DAMAGE = register(new ResourceLocation("mystictools", "add_damage"), () -> new DoubleAttribute((stack, parent, value) -> parent.getBaseValue() + value));
        ADD_SPEED = register(new ResourceLocation("mystictools", "add_speed"), () -> new FloatAttribute((stack, parent, value) -> parent.getBaseValue() + value));
        ADD_EFFICIENCY = register(new ResourceLocation("mystictools", "add_efficiency"), () -> new DoubleAttribute((stack, parent, value) -> parent.getBaseValue() + value));
        ADD_DURABILITY = register(new ResourceLocation("mystictools", "add_durability"), () -> new IntegerAttribute((stack, parent, value) -> parent.getBaseValue() + value));
        ADD_DRAWSPEED = register(new ResourceLocation("mystictools", "add_drawspeed"), () -> new DoubleAttribute((stack, parent, value) -> parent.getBaseValue() + value));
        ADD_POWER = register(new ResourceLocation("mystictools", "add_power"), () -> new DoubleAttribute((stack, parent, value) -> parent.getBaseValue() + value));
        ADD_ARMOR = register(new ResourceLocation("mystictools", "add_armor"), () -> new DoubleAttribute((stack, parent, value) -> parent.getBaseValue() + value));
        ADD_TOUGHNESS = register(new ResourceLocation("mystictools", "add_toughness"), () -> new DoubleAttribute((stack, parent, value) -> parent.getBaseValue() + value));

        LOOT_MODEL = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_MODEL), IntegerAttribute::new);
        LOOT_UUID = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_UUID), StringAttribute::new);
        LOOT_RARITY = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_RARITY), StringAttribute::new);
        LOOT_DAMAGE = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_DAMAGE), DoubleAttribute::new, ADD_DAMAGE);
        LOOT_SPEED = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_SPEED), FloatAttribute::new, ADD_SPEED);
        LOOT_EFFICIENCY = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_EFFICIENCY), DoubleAttribute::new, ADD_EFFICIENCY);
        LOOT_DURABILITY = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_DURABILITY), IntegerAttribute::new, ADD_DURABILITY);
        LOOT_DRAWSPEED = register(new ResourceLocation("mystcictools", LootTags.LOOT_TAG_DRAWSPEED), DoubleAttribute::new, ADD_DRAWSPEED);
        LOOT_POWER = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_POWER), DoubleAttribute::new, ADD_POWER);
        LOOT_ARMOR = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_ARMOR), DoubleAttribute::new, ADD_ARMOR);
        LOOT_TOUGHNESS = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_TOUGHNESS), DoubleAttribute::new, ADD_TOUGHNESS);
        LOOT_EFFECT_LEVEL = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_EFFECT_LEVEL), IntegerAttribute::new);
        LOOT_POTION_EFFECTS = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_POTIONLIST), PotionEffectAttribute::new);
        LOOT_EFFECTS = register(new ResourceLocation("mystictools", LootTags.LOOT_TAG_EFFECTLIST), LootEffectAttribute::new);
    }

    private static <T, I extends ItemNBTAttribute.Instance<T>> ItemNBTAttribute<T, I> register(ResourceLocation id, Supplier<I> instance, ItemNBTAttribute<T, I>... modifiers) {
        ItemNBTAttribute<T, I> attribute = new ItemNBTAttribute<>(id, instance, modifiers);
        REGISTRY.put(id, attribute);
        return attribute;
    }

}
