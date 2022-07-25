package binary404.mystictools.common.network;

import binary404.mystictools.common.items.ModItems;
import binary404.mystictools.common.loot.effects.IEffectAction;
import binary404.mystictools.common.loot.effects.LootEffect;
import binary404.mystictools.common.loot.effects.LootEffectInstance;
import com.google.common.collect.Sets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class PacketToggle {

    private static final Set<Item> tools = Sets.newHashSet(ModItems.loot_axe.get(), ModItems.loot_pickaxe.get(), ModItems.loot_shovel.get());


    public PacketToggle() {

    }

    public static PacketToggle decode(FriendlyByteBuf buffer) {
        return new PacketToggle();
    }

    public static void encode(PacketToggle msg, FriendlyByteBuf buffer) {

    }

    public static void handle(PacketToggle msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                ItemStack tool = player.getMainHandItem();

                if (tools.contains(tool.getItem())) {
                    List<LootEffectInstance> effects = LootEffect.getEffectList(tool);

                    for (LootEffectInstance effect : effects) {
                        IEffectAction action = effect.getEffect().getAction();
                        if(action != null && action.hasResponseMessage(player, tool)) {
                            action.toggleAction(player, tool);
                            player.sendSystemMessage(action.modificationResponseMessage(player, tool));
                        }
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

}
