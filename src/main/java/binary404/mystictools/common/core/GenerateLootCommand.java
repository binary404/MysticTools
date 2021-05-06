package binary404.mystictools.common.core;

import binary404.mystictools.common.core.util.LootSetArgument;
import binary404.mystictools.common.core.util.LootSetInput;
import binary404.mystictools.common.core.util.RarityArgument;
import binary404.mystictools.common.core.util.RarityInput;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootSet;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.Random;

public class GenerateLootCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> commandBuilder = Commands.literal("generateLoot")
                .requires(s -> s.hasPermissionLevel(3))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("rarity", RarityArgument.rarity())
                                .executes((p) -> {
                                    return generateItemFromRarity(p.getSource(), RarityArgument.getRarity(p, "rarity"), EntityArgument.getPlayers(p, "targets"));
                                })
                                .then(Commands.argument("set", LootSetArgument.set())
                                        .executes((p) -> {
                                            return generateItemFromRarityAndSet(p.getSource(), RarityArgument.getRarity(p, "rarity"), LootSetArgument.getType(p, "set"), EntityArgument.getPlayers(p, "targets"));
                                        }))
                        ));
        LiteralCommandNode<CommandSource> command = dispatcher.register(commandBuilder);
    }

    private static int generateItemFromRarity(CommandSource source, RarityInput input, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            Random random = new Random();
            ItemStack itemStack = LootItemHelper.getRandomLoot(random);
            LootSet.LootSetType type = LootItemHelper.getItemType(itemStack.getItem());

            if (type == null)
                type = LootSet.LootSetType.SWORD;

            itemStack = LootItemHelper.generateLoot(input.getRarity(), type, itemStack);
            boolean flag = player.inventory.addItemStackToInventory(itemStack);
            if (flag && itemStack.isEmpty()) {
                itemStack.setCount(1);
                ItemEntity itemEntity = player.dropItem(itemStack, false);
                if (itemEntity != null) {
                    itemEntity.makeFakeItem();
                }

                player.container.detectAndSendChanges();
            } else {
                ItemEntity itemEntity = player.dropItem(itemStack, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickupDelay();
                    itemEntity.setOwnerId(player.getUniqueID());
                }
            }
        }

        return targets.size();
    }

    private static int generateItemFromRarityAndSet(CommandSource source, RarityInput input, LootSetInput set, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            Random random = new Random();
            LootSet.LootSetType type = set.getType();

            if (type == null)
                type = LootSet.LootSetType.SWORD;

            ItemStack itemStack = new ItemStack(type.getItem());

            itemStack = LootItemHelper.generateLoot(input.getRarity(), type, itemStack);
            boolean flag = player.inventory.addItemStackToInventory(itemStack);
            if (flag && itemStack.isEmpty()) {
                itemStack.setCount(1);
                ItemEntity itemEntity = player.dropItem(itemStack, false);
                if (itemEntity != null) {
                    itemEntity.makeFakeItem();
                }

                player.container.detectAndSendChanges();
            } else {
                ItemEntity itemEntity = player.dropItem(itemStack, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickupDelay();
                    itemEntity.setOwnerId(player.getUniqueID());
                }
            }
        }

        return targets.size();
    }

}
