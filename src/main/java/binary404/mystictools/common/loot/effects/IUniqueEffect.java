package binary404.mystictools.common.loot.effects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IUniqueEffect {

    default void tick(Entity entity, ItemStack stack) {

    }

    default void hit(LivingEntity target, LivingEntity attacker, ItemStack stack) {

    }

    default void rightClick(LivingEntity entity, ItemStack stack) {

    }

    default void breakBlock(BlockPos pos, World world, PlayerEntity player, ItemStack stack) {

    }

    default void arrowImpact(Entity entity) {

    }
}
