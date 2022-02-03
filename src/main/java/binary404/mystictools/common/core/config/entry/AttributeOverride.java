package binary404.mystictools.common.core.config.entry;

import com.google.gson.annotations.Expose;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class AttributeOverride {

    @Expose
    public String name;

    public AttributeOverride(Attribute attribute) {
        this.name = attribute.getRegistryName().toString();
    }

    public Attribute getAttribute() {
        return Registry.ATTRIBUTE.getOptional(new ResourceLocation(name)).orElse(null);
    }

}
