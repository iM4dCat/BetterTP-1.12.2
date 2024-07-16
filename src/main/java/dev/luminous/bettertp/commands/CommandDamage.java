package dev.luminous.bettertp.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandDamage extends CommandBase {
    public CommandDamage() {
    }

    public String getName() {
        return "damage";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender p_71518_1_) {
        return "/damage <entity> <amount> or /damage <entity> <amount> <magic/generic/[entity]>";
    }

    public List<String> getTabCompletions(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_) {
        return p_184883_3_.length != 1 && p_184883_3_.length != 2 ? Collections.emptyList() : getListOfStringsMatchingLastWord(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
    }

    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return p_82358_2_ == 0;
    }

    public void execute(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException {
        if (p_184881_3_.length != 2 && p_184881_3_.length != 3) {
            throw new WrongUsageException(getUsage(null));
        } else {
            Entity target = getEntity(p_184881_1_, p_184881_2_, p_184881_3_[0]);
            double amount = parseDouble(p_184881_3_[1]);
            DamageSource damageSource;
            if (p_184881_3_.length == 3) {
                if (p_184881_3_[2].equals("magic")) {
                    damageSource = DamageSource.MAGIC;
                } else if (p_184881_3_[2].equals("generic")) {
                    damageSource = DamageSource.GENERIC;
                } else {
                    Entity entity = getEntity(p_184881_1_, p_184881_2_, p_184881_3_[2]);
                    if (entity instanceof EntityPlayer) {
                        damageSource = DamageSource.causePlayerDamage((EntityPlayer) entity);
                        if (target instanceof EntityLivingBase && target.world != null) {
                            target.attackEntityFrom(damageSource, (float) amount);
                            notifyCommandListener(p_184881_2_, this, "successful", target.getName());
                        }
                    } else if (entity instanceof EntityLivingBase) {
                        damageSource = DamageSource.causeMobDamage((EntityLivingBase) entity);
                        if (target instanceof EntityLivingBase && target.world != null) {
                            target.attackEntityFrom(damageSource, (float) amount);
                            notifyCommandListener(p_184881_2_, this, "successful", target.getName());
                        }
                    }
                    return;
                }
            } else {
                damageSource = DamageSource.causePlayerDamage(null);
            }
            if (target instanceof EntityLivingBase && target.world != null) {
                target.attackEntityFrom(damageSource, (float) amount);
                notifyCommandListener(p_184881_2_, this, "successful", target.getName());
            }
        }
    }
}
