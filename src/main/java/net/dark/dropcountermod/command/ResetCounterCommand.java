package net.dark.dropcountermod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.dark.dropcountermod.messageHandler.GameMessageHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ResetCounterCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("resetcounter")
                .requires(source -> source.hasPermissionLevel(0))
                .executes(ResetCounterCommand::resetCounter));
    }

    private static int resetCounter(CommandContext<ServerCommandSource> context) {
        GameMessageHandler.resetAllCounters();
        return 1;
    }
}