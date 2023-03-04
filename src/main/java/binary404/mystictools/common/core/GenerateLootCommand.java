package binary404.mystictools.common.core;

import binary404.mystictools.common.core.helper.util.LootSetArgument;
import binary404.mystictools.common.core.helper.util.LootSetInput;
import binary404.mystictools.common.core.helper.util.RarityArgument;
import binary404.mystictools.common.core.helper.util.RarityInput;
import binary404.mystictools.common.loot.LootItemHelper;
import binary404.mystictools.common.loot.LootSet;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public class GenerateLootCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> commandBuilder = Commands.literal("generateLoot")
                .requires(s -> s.hasPermission(3))
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
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register(commandBuilder);
    }

    private static int generateItemFromRarity(CommandSourceStack source, RarityInput input, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            RandomSource random = RandomSource.create();
            ItemStack itemStack = LootItemHelper.getRandomLoot(random);
            LootSet.LootSetType type = LootItemHelper.getItemType(itemStack.getItem());

            if (type == null)
                type = LootSet.LootSetType.SWORD;

            itemStack = LootItemHelper.generateLoot(input.getRarity(), type, itemStack);
            boolean flag = player.getInventory().add(itemStack);
            if (flag && itemStack.isEmpty()) {
                itemStack.setCount(1);
                ItemEntity itemEntity = player.drop(itemStack, false);
                if (itemEntity != null) {
                    itemEntity.makeFakeItem();
                }

                player.inventoryMenu.broadcastChanges();
            } else {
                ItemEntity itemEntity = player.drop(itemStack, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickUpDelay();
                    itemEntity.setOwner(player.getUUID());
                }
            }
        }

        return targets.size();
    }

    private static int generateItemFromRarityAndSet(CommandSourceStack source, RarityInput input, LootSetInput set, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            Random random = new Random();
            LootSet.LootSetType type = set.getType();

            if (type == null)
                type = LootSet.LootSetType.SWORD;

            ItemStack itemStack = new ItemStack(type.getItem());

            itemStack = LootItemHelper.generateLoot(input.getRarity(), type, itemStack);
            boolean flag = player.getInventory().add(itemStack);
            if (flag && itemStack.isEmpty()) {
                itemStack.setCount(1);
                ItemEntity itemEntity = player.drop(itemStack, false);
                if (itemEntity != null) {
                    itemEntity.makeFakeItem();
                }

                player.inventoryMenu.broadcastChanges();
            } else {
                ItemEntity itemEntity = player.drop(itemStack, false);
                if (itemEntity != null) {
                    itemEntity.setNoPickUpDelay();
                    itemEntity.setOwner(player.getUUID());
                }
            }
        }

        return targets.size();
    }

}
