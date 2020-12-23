package binary404.mystictools.mixin;

import net.minecraft.village.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PointOfInterestType.class)
public interface PoITypeAccess {

    @Invoker
    static PointOfInterestType callRegisterBlockStates(PointOfInterestType poit) {
        throw new UnsupportedOperationException("Replaced by mixin");
    }

}
