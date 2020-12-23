package binary404.mystictools.mixin;

import net.minecraft.entity.ai.brain.task.GiveHeroGiftsTask;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(GiveHeroGiftsTask.class)
public interface HeroGiftTaskAccess {

    @Accessor("GIFTS")
    static Map<VillagerProfession, ResourceLocation> getGifts() {
        throw new UnsupportedOperationException("Replaced by Mixin");
    }

}
