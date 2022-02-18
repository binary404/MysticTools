package binary404.mystictools.common.loot.effects.effect;

import binary404.mystictools.common.entity.EffectCloudEntity;
import binary404.mystictools.common.items.attribute.ModAttributes;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootNbtHelper;
import binary404.mystictools.common.loot.LootSet;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.PotionEffect;
import binary404.mystictools.common.loot.effects.PotionEffectInstance;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootEffectPotionCloud implements IEffectAction {

    @Override
    public void handleHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int id) {
        CompoundTag tag = LootNbtHelper.getLootCompound(stack, String.valueOf(id));
        if (tag != null && tag.contains("id")) {
            if (target.getRandom().nextInt(10) > 3) {
                PotionEffectInstance instance = PotionEffectInstance.deserialize(tag);
                EffectCloudEntity cloud = new EffectCloudEntity(target.level, target.getX(), target.getY(), target.getZ());
                cloud.addEffect(new MobEffectInstance(instance.getEffect(), instance.getDuration(), instance.getAmplifier()));
                cloud.setOwner(attacker);
                cloud.setPos(target.getX(), target.getY(), target.getZ());
                target.level.addFreshEntity(cloud);
            }
        }
    }

    @Override
    public boolean hasResponseMessage(Player player, ItemStack stack) {
        return false;
    }

    @Override
    public String getAdditionalTooltip(ItemStack stack, int id) {
        CompoundTag tag = LootNbtHelper.getLootAdditionalData(stack, String.valueOf(id));
        if (tag != null && tag.contains("id")) {
            PotionEffectInstance potion = PotionEffectInstance.deserialize(tag);
            return I18n.get(potion.getEffect().getDescriptionId());
        }
        return "";
    }

    @Override
    public CompoundTag addAdditionalData(ItemStack stack, LootSet.LootSetType type, Random random) {
        PotionEffect effect = LootItemHelper.getRandomPotionExcluding(random, type, new ArrayList<>());
        CompoundTag tag = new CompoundTag();
        if (effect != null) {
            tag = new PotionEffectInstance(effect.getEffect(), effect.getDuration(random), effect.getAmplifier(random)).serializeNBT();
        }
        return tag;
    }
}
