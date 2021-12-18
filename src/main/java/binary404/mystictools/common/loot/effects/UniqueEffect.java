package binary404.mystictools.common.loot.effects;

import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.LootTags;
import binary404.mystictools.common.loot.effects.unique.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniqueEffect {

    public static final Map<String, UniqueEffect> REGISTRY = new HashMap<>();

    public static final UniqueEffect xray = create("xray", new XRay()).setItemTypes(LootSet.LootSetType.PICKAXE);
    public static final UniqueEffect emerald = create("emeralds", new Emerald()).setItemTypes(LootSet.LootSetType.PICKAXE);

    public static final UniqueEffect explode = create("explode", new ExplodingArrow()).setItemTypes(LootSet.LootSetType.BOW);

    public static final UniqueEffect vortex = create("vortex", new Vortex()).setItemTypes(LootSet.LootSetType.SWORD);

    public static final UniqueEffect treeChopper = create("treeChopper", new TreeChopper()).setItemTypes(LootSet.LootSetType.AXE);
    public static final UniqueEffect growthAura = create("growthAura", new GrowthAura()).setItemTypes(LootSet.LootSetType.AXE, LootSet.LootSetType.SHOVEL);

    public static final UniqueEffect arc = create("arc", new Arc()).setItemTypes(LootSet.LootSetType.SWORD);

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

    public CompoundTag getNbt() {
        CompoundTag tag = new CompoundTag();

        tag.putString("id", this.getId());
        tag.putInt("lvl", 1);

        return tag;
    }

    public static UniqueEffect getUniqueEffect(ItemStack stack) {
        CompoundTag tag = LootNbtHelper.getLootCompound(stack, LootTags.LOOT_TAG_UNIQUE);
        return UniqueEffect.getById(tag.getString("id"));
    }

    public void rightClick(LivingEntity entity, ItemStack stack) {
        this.effect.rightClick(entity, stack);
    }

    public void hit(LivingEntity target, LivingEntity attacker, ItemStack stack, double damage) {
        this.effect.hit(target, attacker, stack, damage);
    }

    public void breakBlock(BlockPos pos, Level world, Player player, ItemStack stack, BlockState blockBroken) {
        this.effect.breakBlock(pos, world, player, stack, blockBroken);
    }

    public void tick(Entity entity, ItemStack stack) {
        this.effect.tick(entity, stack);
    }

    public void arrowImpact(Entity shooter, Entity arrow) {
        this.effect.arrowImpact(shooter, arrow);
    }

    protected static UniqueEffect create(String id, IUniqueEffect effect) {
        UniqueEffect effect1 = new UniqueEffect(effect);

        effect1.id = id;
        REGISTRY.put(id, effect1);
        return effect1;
    }

}
