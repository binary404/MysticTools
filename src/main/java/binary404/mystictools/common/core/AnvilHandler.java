package binary404.mystictools.common.core;

import binary404.mystictools.common.items.ILootItem;
import binary404.mystictools.common.items.ItemLootSword;
import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.modifiers.LootModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilHandler {

    @SubscribeEvent
    public static void applyArtifact(AnvilUpdateEvent event) {
        if(event.getLeft().getItem() instanceof ItemLootSword && event.getRight().getItem() == ModItems.artifact) {
            ItemStack output = event.getLeft().copy();
            LootModifiers.ArtifactModifiers.SWORD.initialize(output, new Random());
            event.setOutput(output);
            event.setCost(2);
        }
    }
}
