package net.mat0u5.matlib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public abstract class Command {
    public abstract boolean isAllowed();
    public abstract Component getBannedText();
    public abstract void register(CommandDispatcher<CommandSourceStack> dispatcher);

    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {
        register(dispatcher);
    }

    public boolean checkBanned(CommandSourceStack source) {
        if (isAllowed()) return false;
        sendCommandFailure(source, getBannedText());
        return true;
    }

    /*
    public boolean isAllowed(ServerCommandSource source) {
        return isAllowed();
    }
    */

    public static LiteralArgumentBuilder<CommandSourceStack> literal(String string) {
        return Commands.literal(string);
    }

    public static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
        return Commands.argument(name, type);
    }

    public static void sendCommandFeedback(CommandSourceStack source, Component text) {
        if (source == null || text == null) return;
        if (text.getString().isEmpty()) return;
        source.sendSuccess(() -> text, true);
    }

    public static void sendCommandFeedbackQuiet(CommandSourceStack source, Component text) {
        if (source == null || text == null) return;
        if (text.getString().isEmpty()) return;
        source.sendSuccess(() -> text, false);
    }
    public static void sendCommandFailure(CommandSourceStack source, Component text) {
        sendCommandFailure(source, text, false);
    }
    public static void sendCommandFailure(CommandSourceStack source, Component text, boolean keepFormatting) {
        if (keepFormatting) {
            source.sendFailure(text);
        }
        else {
            source.sendFailure(Component.literal(text.getString()));
        }
    }
}
