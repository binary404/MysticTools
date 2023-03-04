package binary404.mystictools.client.fx;

import binary404.fx_lib.fx.effects.FXGen;
import binary404.fx_lib.util.Vector3;

public class ParticleCloudController extends FXSourceCloud {

    FXGen generator;

    public ParticleCloudController(Vector3 pos, FXGen generator) {
        super(pos);
        this.generator = generator;
    }

    public FXGen getGenerator() {
        return generator;
    }

    public ParticleCloudController setGenerator(FXGen generator) {
        this.generator = generator;
        return this;
    }

    @Override
    public void spawnParticle(Vector3 point) {
        this.generator.generateFXs(point, this.pos, new Vector3(1.0d, 1.0d, 1.0d));
    }
}
