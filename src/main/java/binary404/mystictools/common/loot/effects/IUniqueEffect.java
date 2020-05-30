package binary404.mystictools.common.loot.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IUniqueEffect {

    void tick(LivingEntity entity, ItemStack stack);

    void hit(LivingEntity target, LivingEntity attacker, ItemStack stack);

    void rightClick(LivingEntity entity, ItemStack stack);

}
