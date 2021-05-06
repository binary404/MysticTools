package binary404.mystictools.common.core;

import binary404.mystictools.common.world.UniqueSave;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class UniqueCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> commandBuilder = Commands.literal("unique")
                .requires(s -> s.hasPermissionLevel(0))
                .executes(ctx -> {
                    UniqueSave save = UniqueSave.forWorld(ctx.getSource().getWorld());

                    int found = 0;

                    for (UniqueSave.UniqueInfo unique : save.uniques) {
                        if (unique.found)
                            found++;
                    }

                    ctx.getSource().sendFeedback(new StringTextComponent( "(" + found + "/5) uniques found"), false);
                    return Command.SINGLE_SUCCESS;
                });

        LiteralCommandNode<CommandSource> command = dispatcher.register(commandBuilder);
    }

}
