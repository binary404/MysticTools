package binary404.mystictools.common.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.antlr.v4.codegen.model.Sync;

public class EffectCloudEntity extends Entity {

    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(EffectCloudEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EffectCloudEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IGNORE_RADIUS = SynchedEntityData.defineId(EffectCloudEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ParticleOptions> PARTICLE = SynchedEntityData.defineId(EffectCloudEntity.class, EntityDataSerializers.PARTICLE);

}
