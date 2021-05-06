package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;

public class ExplodingArrow implements IUniqueEffect {

    @Override
    public void arrowImpact(Entity shooter, Entity arrow) {
        shooter.world.createExplosion(shooter, arrow.getPosX() + 0.5, arrow.getPosY() + 0.5, arrow.getPosZ() + 0.5, 4, Explosion.Mode.NONE);
    }
}
