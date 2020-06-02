package binary404.mystictools.common.loot.effects.unique;

import binary404.mystictools.common.loot.effects.IUniqueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;

public class ExplodingArrow implements IUniqueEffect {

    @Override
    public void arrowImpact(Entity entity) {
        entity.world.createExplosion(entity, entity.getPosX() + 0.5, entity.getPosY() + 0.5, entity.getPosZ() + 0.5, 4, Explosion.Mode.NONE);
    }
}
