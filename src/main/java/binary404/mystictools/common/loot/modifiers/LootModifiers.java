package binary404.mystictools.common.loot.modifiers;

import binary404.mystictools.common.items.attribute.IntegerAttribute;
import binary404.mystictools.common.items.attribute.ItemNBTAttribute;
import binary404.mystictools.common.items.attribute.ModAttributes;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LootModifiers {

    public static class ArtifactModifiers {

        public static BaseModifiers SWORD;

        public static void init() {
            SWORD = new BaseModifiers();
            SWORD.ADD_DAMAGE = new Generators.DoubleGenerator(1.5, 5.0);
        }

        public static class BaseModifiers {
            public Generators.DoubleGenerator ADD_ARMOR;
            public Generators.DoubleGenerator ADD_TOUGHNESS;
            public Generators.DoubleGenerator ADD_DAMAGE;
            public Generators.DoubleGenerator ADD_SPEED;
            public Generators.IntegerGenerator ADD_DURABILITY;
            public Generators.IntegerGenerator ADD_EFFICIENCY;
            public Generators.FloatGenerator ADD_DRAWSPEED;
            public Generators.FloatGenerator ADD_POWER;

            public void initialize(ItemStack stack, Random random) {
                List<ItemNBTAttribute.Instance.Generator<?>> generators = Arrays.asList(
                        this.ADD_DAMAGE, this.ADD_SPEED, this.ADD_EFFICIENCY,
                        this.ADD_DURABILITY, this.ADD_DRAWSPEED, this.ADD_POWER,
                        this.ADD_ARMOR, this.ADD_TOUGHNESS);
                List<Boolean> existing = Arrays.asList(
                        ModAttributes.ADD_DAMAGE.exists(stack), ModAttributes.ADD_SPEED.exists(stack), ModAttributes.ADD_EFFICIENCY.exists(stack),
                        ModAttributes.ADD_DURABILITY.exists(stack), ModAttributes.ADD_DRAWSPEED.exists(stack), ModAttributes.ADD_POWER.exists(stack),
                        ModAttributes.ADD_ARMOR.exists(stack), ModAttributes.ADD_TOUGHNESS.exists(stack)
                );

                List<Integer> possible = IntStream.range(0, generators.size())
                        .filter(i -> generators.get(i) != null)
                        .filter(i -> !existing.get(i))
                        .boxed()
                        .collect(Collectors.toList());

                Collections.shuffle(possible, random);

                if (this.ADD_DAMAGE == generators.get(possible.get(0)))
                    ModAttributes.ADD_DAMAGE.create(stack, random, this.ADD_DAMAGE);
            }
        }

    }

    public static class Generators {
        public static class DoubleGenerator implements ItemNBTAttribute.Instance.Generator<Double> {
            private double min;
            private double max;

            public DoubleGenerator(double min, double max) {
                this.min = min;
                this.max = max;
            }

            @Override
            public Double generate(ItemStack stack, Random random) {
                return Mth.nextDouble(random, min, max);
            }
        }

        public static class IntegerGenerator implements ItemNBTAttribute.Instance.Generator<Integer> {
            private int min;
            private int max;

            public IntegerGenerator(int min, int max) {
                this.min = min;
                this.max = max;
            }

            @Override
            public Integer generate(ItemStack stack, Random random) {
                return Mth.nextInt(random, min, max);
            }
        }

        public static class FloatGenerator implements ItemNBTAttribute.Instance.Generator<Float> {
            private float min;
            private float max;

            public FloatGenerator(float min, float max) {
                this.min = min;
                this.max = max;
            }

            @Override
            public Float generate(ItemStack stack, Random random) {
                return Mth.nextFloat(random, min, max);
            }
        }
    }

}
