package binary404.mystictools.client.render;

import binary404.fx_lib.fx.effects.EffectRegistrar;
import binary404.fx_lib.fx.effects.FXSourceOrbital;
import binary404.fx_lib.util.Vector3;
import binary404.mystictools.client.fx.FXHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;

public class TreeChopperClient {

    public static final Map<ResourceKey<Level>, Set<FXSourceOrbital>> orbitals = new HashMap<>();

    public static void addOrbital(BlockPos origCoords, ResourceKey<Level> dim) {
        if (FXHelper.fxlibLoaded()) {
            List<FXSourceOrbital> orbitalsToAdd = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                FXSourceOrbital orbital = FXHelper.spiralGenerator(origCoords, Vector3.random()).refresh((t) -> true);
                EffectRegistrar.register(orbital);
                orbitalsToAdd.add(orbital);
            }
            orbitals.computeIfAbsent(dim, d -> new HashSet<>()).addAll(orbitalsToAdd);
        }
    }

}
