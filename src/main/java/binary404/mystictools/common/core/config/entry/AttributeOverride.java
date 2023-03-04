package binary404.mystictools.common.core.config.entry;

import com.google.gson.annotations.Expose;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;

public class AttributeOverride {

    @Expose
    public String name;
    @Expose
    public String modifierType;
    @Expose
    public long salt;

    public AttributeOverride(Attribute attribute, String type, long salt) {
        this.name = ForgeRegistries.ATTRIBUTES.getKey(attribute).toString();
        this.modifierType = type;
        this.salt = salt;
    }

    public Attribute getAttribute() {
        return ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(name));
    }

}
