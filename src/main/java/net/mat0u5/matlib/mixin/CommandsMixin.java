package net.mat0u5.matlib.mixin;

import com.mojang.brigadier.CommandDispatcher;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.command.CommandManager;
import net.mat0u5.matlib.events.common.ServerCommandEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public abstract class CommandsMixin {
    @Shadow
    @Final
    private CommandDispatcher<CommandSourceStack> dispatcher;

//? if !forge {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V"), method = "<init>")
//?} else {
    /*//? if <= 1.20.5 {
    /^@Inject(method = "<init>", at = @At("RETURN"))
    ^///?} else {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V", unsafe = true), method = "<init>")
    //?}
*///?}
    private void addCommands(Commands.CommandSelection selection, CommandBuildContext buildContext, CallbackInfo ci) {
        ServerCommandEvents.VANILLA_REGISTER.invoker().onRegister(this.dispatcher, buildContext, selection);
        CommandManager.registerCustomCommands(this.dispatcher, buildContext, selection);
    }
}
