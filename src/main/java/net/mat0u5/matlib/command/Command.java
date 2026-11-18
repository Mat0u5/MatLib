package net.mat0u5.matlib.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.mat0u5.matlib.utils.player.PermissionManager;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

//? if <= 26.3 {
/*import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.SharedSuggestionProvider;
*///?} else {
import com.mojang.serialization.MapCodec;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DoubleArgument;
import net.minecraft.commands.arguments.IntegerArgument;
import net.minecraft.commands.arguments.StringArgument;
import net.minecraft.commands.suggestions.SuggestionContext;
import net.minecraft.commands.suggestions.SuggestionProviders;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.server.permissions.Permissions;
//?}

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
    //? if <= 26.3 {
	/*public static Predicate<CommandSourceStack> requireAdmin() {
		return PermissionManager::isAdmin;
	}

	public static Predicate<CommandSourceStack> requireIf(BooleanSupplier condition) {
		return source -> condition.getAsBoolean();
	}
	*///?} else {
    private static final PermissionCheck ADMIN_CHECK = new PermissionCheck.Require(Permissions.COMMANDS_ADMIN);

    public static Predicate<CommandSourceStack> requireAdmin() {
        return Commands.hasPermission(ADMIN_CHECK);
    }

    public static Predicate<CommandSourceStack> requireIf(BooleanSupplier condition) {
        return Commands.hasPermission(new ConditionCheck(condition));
    }

    private record ConditionCheck(BooleanSupplier condition) implements PermissionCheck {
        @Override
        public boolean check(PermissionSet permissions) {
            return condition.getAsBoolean();
        }

        @Override
        public MapCodec<ConditionCheck> codec() {
            return MapCodec.unit(this);
        }
    }
    //?}

    //? if <= 26.3 {
	/*public static ArgumentType<String> stringArg() {
		return StringArgumentType.string();
	}
	public static ArgumentType<String> greedyStringArg() {
		return StringArgumentType.greedyString();
	}
	public static ArgumentType<Integer> integerArg() {
		return IntegerArgumentType.integer();
	}
	public static ArgumentType<Integer> integerArg(int min) {
		return IntegerArgumentType.integer(min);
	}
	public static ArgumentType<Integer> integerArg(int min, int max) {
		return IntegerArgumentType.integer(min, max);
	}
	public static ArgumentType<Double> doubleArg(double min) {
		return DoubleArgumentType.doubleArg(min);
	}
	*///?} else {
    public static ArgumentType<String> stringArg() {
        return StringArgument.string();
    }
    public static ArgumentType<String> greedyStringArg() {
        return StringArgument.greedyString();
    }
    public static ArgumentType<Integer> integerArg() {
        return IntegerArgument.integer();
    }
    public static ArgumentType<Integer> integerArg(int min) {
        return IntegerArgument.integer(min);
    }
    public static ArgumentType<Integer> integerArg(int min, int max) {
        return IntegerArgument.integer(min, max);
    }
    public static ArgumentType<Double> doubleArg(double min) {
        return DoubleArgument.doubleArg(min);
    }
    //?}

    // Fixed list
    public static SuggestionProvider<CommandSourceStack> suggestList(List<String> suggestions) {
        return suggestList(() -> suggestions);
    }

    // Re-evaluated on every request
    public static SuggestionProvider<CommandSourceStack> suggestList(Supplier<? extends Iterable<String>> suggestions) {
        //? if <= 26.3 {
        //return suggestCustom(builder -> SharedSuggestionProvider.suggest(suggestions.get(), builder));
         //?} else {
        return suggestCustom(builder -> SuggestionContext.suggest(suggestions.get(), builder));
        //?}
    }

    // Re-evaluated on every request
    public static SuggestionProvider<CommandSourceStack> suggestCustom(Function<SuggestionsBuilder, CompletableFuture<Suggestions>> provider) {
        //? if <= 26.3 {
        //return (context, builder) -> provider.apply(builder);
         //?} else {
        return SuggestionProviders.serverOnly((server, context, builder) -> provider.apply(builder));
        //?}
    }
}
