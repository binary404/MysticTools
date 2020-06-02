package binary404.mystictools.common.core;

import binary404.mystictools.common.loot.effects.UniqueEffect;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "mystictools", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityHandler {

    @SubscribeEvent
    public static void entityHit(ProjectileImpactEvent.Arrow event) {
        if (event.getArrow().getPersistentData().contains("unique")) {
            CompoundNBT compound = event.getArrow().getPersistentData().getCompound("unique");
            String id = compound.getString("id");
            UniqueEffect effect = UniqueEffect.getById(id);
            effect.arrowImpact(event.getEntity());
        }
    }

}
