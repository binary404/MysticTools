package binary404.mystictools.client.fx;

import binary404.fx_lib.fx.EffectRegistry;
import binary404.fx_lib.fx.effects.impl.resource.DrawableTexture;
import binary404.fx_lib.fx.effects.impl.resource.TextureSheet;
import javafx.scene.effect.Effect;

import static binary404.fx_lib.fx.effects.impl.resource.AssetLibrary.loadTexture;
import static binary404.fx_lib.fx.effects.impl.resource.AssetLoader.TextureLocation.EFFECT;

public class ModEffects {

    public static DrawableTexture tex_shockwave;

    public static TextureSheet sheet_shockwave;

    public static void init() {
        EffectRegistry.addTextureRunnable(ModEffects::initTextures);
        EffectRegistry.addSpriteRunnable(ModEffects::initSheet);
    }

    public static void initTextures() {
        tex_shockwave = loadTexture(EFFECT, "mystictools", "shockwave");
    }

    public static void initSheet() {
        sheet_shockwave = new TextureSheet(tex_shockwave, 6, 8);
    }

}
