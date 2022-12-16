package binary404.mystictools.common.ritual;

import binary404.mystictools.MysticTools;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * WARNING!
 *
 * THE CODE HERE IS VERY UGLY AND MAY CAUSE TRAUMA AND/OR STROKE(S)
 * READERS BEWARE, I PHYSICALLY DIED YESTERDAY WRITING THIS
 *
 * DO NOT ASK ME WHAT ANY OF THIS DOES
 * DO NOT CHANGE ANY OF THIS
 * IT WORKS HOW IT IS DO NOT "FIX IT"
 */
//Basically a "recipe" for rituals
public record Ritual(int activationTime, int duration, List<ItemStack> cost, ConfiguredRitualType<?, ?> ritual, List<ConfiguredRitualModule<?, ?>> modules) {
    public static final Codec<Ritual> CODEC = ExtraCodecs.lazyInitializedCodec(() -> RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("activationTime").forGetter(Ritual::activationTime),
            Codec.INT.fieldOf("duration").forGetter(Ritual::duration),
            ItemStack.CODEC.listOf().fieldOf("cost").forGetter(Ritual::cost),
            ConfiguredRitualType.CODEC.fieldOf("ritual").forGetter(Ritual::ritual),
            ConfiguredRitualModule.CODEC.listOf().fieldOf("modules").forGetter(Ritual::modules)
    ).apply(instance, Ritual::new)));

    public static final ResourceKey<Registry<Ritual>> RITUAL_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MysticTools.modid, "rituals"));
    public static final DeferredRegister<Ritual> RITUAL_DEFERRED_REGISTER = DeferredRegister.create(RITUAL_REGISTRY_KEY, MysticTools.modid);
    public static Supplier<IForgeRegistry<Ritual>> RITUAL_REGISTRY = RITUAL_DEFERRED_REGISTER.makeRegistry(() -> new RegistryBuilder<Ritual>().dataPackRegistry(CODEC));

    public static Optional<Ritual> findRitual(List<ItemStack> items, Level level) {
        return level.registryAccess().registryOrThrow(RITUAL_REGISTRY_KEY).stream().filter((r) -> {
            return r.matches(items);
        }).findFirst();
    }

    public static ResourceLocation getRegistryId(Ritual ritual, Level level) {
        return level.registryAccess().registryOrThrow(RITUAL_REGISTRY_KEY).getKey(ritual);
    }

    //PAIN AND SUFFERING
    //MAY OPEN A PORTAL DIRECTLY TO HELL
    public boolean matches(List<ItemStack> items) {
        boolean matches = true;
        for(ItemStack stack : this.cost) {
            Item item = stack.getItem();
            int count = stack.getCount();
            boolean foundMatch = false;
            for(ItemStack test : items) {
                if(item.equals(test.getItem()) && count <= test.getCount()) {
                    foundMatch = true;
                }
            }
            if(!foundMatch)
                matches = false;
        }
        return matches;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Ritual) obj;
        return this.activationTime == that.activationTime &&
                this.duration == that.duration &&
                Objects.equals(this.cost, that.cost) &&
                Objects.equals(this.ritual, that.ritual);
    }

    @Override
    public String toString() {
        return "Ritual[" +
                "activationTime=" + activationTime + ", " +
                "duration=" + duration + ", " +
                "cost=" + cost + ", " +
                "ritual=" + ritual + ']';
    }
}
