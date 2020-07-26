package binary404.mystictools.common.network;

import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.LootEffect;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class PacketToggle {

    private static final Set<Item> tools = Sets.newHashSet(ModItems.loot_axe, ModItems.loot_pickaxe, ModItems.loot_shovel);


    public PacketToggle() {

    }

    public static PacketToggle decode(PacketBuffer buffer) {
        return new PacketToggle();
    }

    public static void encode(PacketToggle msg, PacketBuffer buffer) {

    }

    public static void handle(PacketToggle msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                System.out.println("Handling");
                ServerPlayerEntity player = ctx.get().getSender();
                ItemStack tool = player.getHeldItemMainhand();

                if (tools.contains(tool.getItem())) {
                    List<LootEffect> effects = LootEffect.getEffectList(tool);

                    for (LootEffect effect : effects) {
                        IEffectAction action = effect.getAction();
                        if(action != null) {
                            action.toggleAction(player, tool);
                            player.sendMessage(action.modificationResponseMessage(player, tool));
                        }
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
