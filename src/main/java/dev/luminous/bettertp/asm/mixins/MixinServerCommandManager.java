package dev.luminous.bettertp.asm.mixins;

import dev.luminous.bettertp.commands.*;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandListener;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommandManager.class)
public abstract class MixinServerCommandManager extends CommandHandler implements ICommandListener {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(MinecraftServer p_i46985_1_, CallbackInfo ci) {
        registerCommand(new CommandRotationShake());
        registerCommand(new CommandLookAt());
        registerCommand(new CommandSetVelocity());
        registerCommand(new CommandRandomTp());
        registerCommand(new CommandSetForwardVelocity());
        registerCommand(new CommandResetFallDistance());
        registerCommand(new CommandFire());
        registerCommand(new CommandDamage());
        registerCommand(new CommandHeal());
        registerCommand(new CommandCopyHealth());
    }
}
