package binary404.mystictools.common.ritual;

import binary404.mystictools.MysticTools;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

//Basically a "recipe" for rituals
public record Ritual(int activationTime, int duration, List<ItemStack> cost, ConfiguredRitualType<?, ?> ritual) {

    public static final Codec<Ritual> CODEC = ExtraCodecs.lazyInitializedCodec(() -> RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("activationTime").forGetter(Ritual::activationTime),
            Codec.INT.fieldOf("duration").forGetter(Ritual::duration),
            ItemStack.CODEC.listOf().fieldOf("cost").forGetter(Ritual::cost),
            ConfiguredRitualType.CODEC.fieldOf("ritual").forGetter(Ritual::ritual)
    ).apply(instance, Ritual::new)));

    public static final ResourceKey<Registry<Ritual>> RITUAL_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MysticTools.modid, "rituals"));
    public static final DeferredRegister<Ritual> RITUAL_DEFERRED_REGISTER = DeferredRegister.create(RITUAL_REGISTRY_KEY, MysticTools.modid);
    public static Supplier<IForgeRegistry<Ritual>> RITUAL_REGISTRY = RITUAL_DEFERRED_REGISTER.makeRegistry(() -> new RegistryBuilder<Ritual>().dataPackRegistry(CODEC));

    public static Optional<Ritual> findRitual(List<ItemStack> items, Level level) {
        return level.registryAccess().registryOrThrow(RITUAL_REGISTRY_KEY).stream().filter((r) -> {
            return r.matches(items);
        }).findFirst();
    }

    public boolean matches(List<ItemStack> items) {
        return this.cost.equals(items);
    }
}
