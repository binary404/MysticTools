package binary404.mystictools.common.loot.effects;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IUniqueEffect {

    default void tick(Entity entity, ItemStack stack) {

    }

    default void hit(LivingEntity target, LivingEntity attacker, ItemStack stack, double damage) {

    }

    default void rightClick(LivingEntity entity, ItemStack stack) {

    }

    default void breakBlock(BlockPos pos, Level world, Player player, ItemStack stack) {

    }

    default void arrowImpact(Entity shooter, Entity arrow) {

    }
}
