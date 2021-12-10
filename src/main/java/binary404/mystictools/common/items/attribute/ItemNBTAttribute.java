package binary404.mystictools.common.items.attribute;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;
import java.util.function.Supplier;

public class ItemNBTAttribute<T, I extends ItemNBTAttribute.Instance<T>> {

    private final ResourceLocation id;
    private final Supplier<I> instance;
    private final List<ItemNBTAttribute<T, I>> modifiers;

    public ItemNBTAttribute(ResourceLocation id, Supplier<I> instance, ItemNBTAttribute<T, I>... modifiers) {
        this.id = id;
        this.instance = instance;
        this.modifiers = new ArrayList<>(Arrays.asList(modifiers));
    }

    public ResourceLocation getId() {
        return id;
    }

    protected String getTagKey() {
        return "NBTAttributes";
    }

    public Optional<I> get(ItemStack stack) {
        CompoundTag nbt = stack.getTagElement("MysticTools");
        if (nbt == null || !nbt.contains(this.getTagKey(), 9))
            return Optional.empty();

        ListTag attributes = nbt.getList(this.getTagKey(), 10);

        for (Tag element : attributes) {
            CompoundTag tag = (CompoundTag) element;

            if (tag.getString("Id").equals(this.getId().toString())) {
                I instance = this.instance.get();
                instance.parent = this;
                instance.delegate = tag;
                instance.read(tag);
                return Optional.of(instance);
            }
        }

        return Optional.empty();
    }

    public boolean exists(ItemStack stack) {
        return this.get(stack).isPresent();
    }

    public I getOrDefault(ItemStack stack, T value) {
        return this.getOrDefault(stack, () -> value);
    }

    public I getOrDefault(ItemStack stack, Random random, Instance.Generator<T> generator) {
        return this.getOrDefault(stack, () -> generator.generate(stack, random));
    }

    public I getOrDefault(ItemStack stack, Supplier<T> value) {
        return this.get(stack).orElse((I) this.instance.get().setBaseValue(value.get()));
    }

    public I getOrCreate(ItemStack stack, T value) {
        return this.getOrCreate(stack, () -> value);
    }

    public I getOrCreate(ItemStack stack, Random random, Instance.Generator<T> generator) {
        return this.getOrCreate(stack, () -> generator.generate(stack, random));
    }

    public I getOrCreate(ItemStack stack, Supplier<T> value) {
        return this.get(stack).orElseGet(() -> this.create(stack, value));
    }

    public I create(ItemStack stack, T value) {
        return this.create(stack, () -> value);
    }

    public I create(ItemStack stack, Random random, Instance.Generator<T> generator) {
        return this.create(stack, () -> generator.generate(stack, random));
    }

    public I create(ItemStack stack, Supplier<T> value) {
        CompoundTag nbt = stack.getOrCreateTagElement("MysticTools");
        if (!nbt.contains(this.getTagKey(), 9))
            nbt.put(this.getTagKey(), new ListTag());
        ListTag attributesList = nbt.getList(this.getTagKey(), 10);

        CompoundTag attributeNBT = attributesList.stream()
                .map(element -> (CompoundTag) element)
                .filter(tag -> tag.getString("Id").equals(this.getId().toString()))
                .findFirst()
                .orElseGet(() -> {
                    CompoundTag tag = new CompoundTag();
                    attributesList.add(tag);
                    return tag;
                });

        I instance = this.instance.get();
        instance.parent = this;
        instance.delegate = attributeNBT;
        instance.setBaseValue(value.get());
        return instance;
    }

    @FunctionalInterface
    public interface Modifier<T> {
        T apply(ItemStack stack, ItemNBTAttribute.Instance<T> parent, T value);
    }

    public static abstract class Instance<T> implements INBTSerializable<CompoundTag>, Modifier<T> {
        protected ItemNBTAttribute<T, ? extends Instance<T>> parent;

        protected T baseValue;
        private Modifier<T> modifier;

        protected CompoundTag delegate;

        protected Instance() {

        }

        protected Instance(Modifier<T> modifier) {
            this.modifier = modifier;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("Id", this.parent.id.toString());
            this.write(nbt);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.read(nbt);
        }

        public abstract void write(CompoundTag nbt);

        public abstract void read(CompoundTag nbt);

        public T getBaseValue() {
            return this.baseValue;
        }

        public Instance<T> setBaseValue(T baseValue) {
            this.baseValue = baseValue;
            this.updateNBT();
            return this;
        }

        public T getValue(ItemStack stack) {
            T value = this.getBaseValue();
            if (this.parent == null)
                return value;

            for (ItemNBTAttribute<T, ? extends Instance<T>> modifier : this.parent.modifiers) {
                Optional<? extends Instance<T>> instance = modifier.get(stack);

                if (instance.isPresent()) {
                    value = instance.get().apply(stack, instance.get(), value);
                }
            }

            return value;
        }

        @Override
        public T apply(ItemStack stack, Instance<T> parent, T value) {
            return this.modifier == null ? value : this.modifier.apply(stack, parent, value);
        }

        protected void updateNBT() {
            if (this.delegate == null)
                return;
            CompoundTag nbt = this.serializeNBT();

            for (String key : nbt.getAllKeys()) {
                Tag value = nbt.get(key);

                if (value != null) {
                    this.delegate.put(key, value);
                }
            }
        }

        @FunctionalInterface
        public interface Generator<T> {
            T generate(ItemStack stack, Random random);
        }
    }

}
