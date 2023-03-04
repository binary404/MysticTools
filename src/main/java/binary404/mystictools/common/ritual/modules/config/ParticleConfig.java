package binary404.mystictools.common.ritual.modules.config;

import binary404.fx_lib.fx.ParticleGenerator;
import binary404.fx_lib.fx.effects.FXGen;
import binary404.fx_lib.fx.effects.FXSource;
import binary404.fx_lib.fx.effects.ParticleOrbitalController;
import binary404.fx_lib.fx.effects.function.FXMotionController;
import binary404.fx_lib.util.Vector3;
import binary404.mystictools.client.fx.FXHelper;
import binary404.mystictools.client.fx.ParticleCloudController;
import binary404.mystictools.common.ritual.RitualModuleConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;

public class ParticleConfig implements RitualModuleConfiguration {

    public static final Codec<ParticleConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ParticleGenerator.CODEC.listOf().fieldOf("generators").forGetter(ParticleConfig::getGenerators),
            ControllerType.CODEC.fieldOf("type").forGetter(ParticleConfig::getType)
    ).apply(instance, ParticleConfig::new));
    List<ParticleGenerator> generators;
    ControllerType type;

    public ParticleConfig(List<ParticleGenerator> generators, ControllerType type) {
        this.generators = generators;
        this.type = type;
    }

    public List<ParticleGenerator> getGenerators() {
        return generators;
    }

    public ControllerType getType() {
        return type;
    }

    public enum ControllerType implements StringRepresentable {
        SPIRAL("spiral"),
        AREA("area");

        public static final StringRepresentable.EnumCodec<ControllerType> CODEC = StringRepresentable.fromEnum(ControllerType::values);
        String name;

        ControllerType(String name) {
            this.name = name;
        }

        public FXSource getSource(List<ParticleGenerator> generators, double radius, Vector3 pos) {
            switch (this) {
                case SPIRAL -> {
                    List<FXGen.ParticleComponent> components = new ArrayList<>();
                    for (ParticleGenerator generator : generators) {
                        FXGen.ParticleComponent component = new FXGen.ParticleComponent(generator, 1, (mot, ppos, sourcePos, taxis) -> {
                            return mot;
                        }, (ppos, sourcePos, velocity, motion) -> {
                            return FXMotionController.STATIC;
                        });
                        components.add(component);
                    }
                    FXGen gen = new FXGen(components.toArray(new FXGen.ParticleComponent[0]));
                    return new ParticleOrbitalController(pos, gen)
                            .setOrbitAxis(Vector3.RotAxis.Y_AXIS)
                            .setOrbitRadius(radius)
                            .setBranches(3)
                            .setTicksPerRotation(60);
                }
                case AREA -> {
                    List<FXGen.ParticleComponent> components = new ArrayList<>();
                    for (ParticleGenerator generator : generators) {
                        FXGen.ParticleComponent component = new FXGen.ParticleComponent(generator, 1, (mot, ppos, sourcePos, taxis) -> {
                            double x = Mth.nextDouble(FXHelper.getRandomSource(), -1, 1);
                            double y = Mth.nextDouble(FXHelper.getRandomSource(), -0.25, 0.25);
                            double z = Mth.nextDouble(FXHelper.getRandomSource(), -1, 1);
                            Vector3 direction = new Vector3(x, y, z);
                            return direction.normalize().multiply(0.75F);
                        }, (ppos, sourcePos, velocity, motion) -> {
                            return FXMotionController.decelerate(() -> motion);
                        });
                        components.add(component);
                    }
                    FXGen gen = new FXGen(components.toArray(new FXGen.ParticleComponent[0]));
                    return new ParticleCloudController(pos, gen)
                            .setRadius(radius)
                            .setCount(5);
                }
                default -> {
                    return null;
                }
            }
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

}
