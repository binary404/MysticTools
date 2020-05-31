package binary404.mystictools.common.loot.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public interface IUniqueEffect {

    default void tick(LivingEntity entity, ItemStack stack) {

    }

    default void hit(LivingEntity target, LivingEntity attacker, ItemStack stack) {

    }

    default void rightClick(LivingEntity entity, ItemStack stack) {

    }

    default CompoundNBT write(CompoundNBT nbt, LivingEntity entity) {
        return nbt;
    }

    default void read(CompoundNBT nbt, LivingEntity entity) {

    }

}
