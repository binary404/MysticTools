package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;

public class ExplodingArrow implements IUniqueEffect {

    @Override
    public void arrowImpact(Entity shooter, Entity arrow) {
        shooter.level.explode(shooter, arrow.getX() + 0.5, arrow.getY() + 0.5, arrow.getZ() + 0.5, 4, Explosion.BlockInteraction.NONE);
    }
}
