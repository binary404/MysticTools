package binary404.mystictools.common.core.config.entry;

import com.google.gson.annotations.Expose;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemEntry {

    @Expose
    public String ITEM;

    public ItemEntry(String item) {
        this.ITEM = item;
    }

    public ItemEntry(Item item) {
        this.ITEM = item.getRegistryName().toString();
    }

    public Item toItem() {
        return Registry.ITEM.getOptional(new ResourceLocation(this.ITEM)).orElse(null);
    }

}
