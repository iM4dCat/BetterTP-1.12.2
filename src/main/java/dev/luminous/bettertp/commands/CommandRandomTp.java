package dev.luminous.bettertp.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;

public class CommandRandomTp extends CommandBase {
    public CommandRandomTp() {
    }

    static Random rd = new Random();
    public String getName() {
        return "randomtp";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender p_71518_1_) {
        return "/randomtp <entity> <x> <y> <z>";
    }

    public List<String> getTabCompletions(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_) {
        return p_184883_3_.length != 1 && p_184883_3_.length != 2 ? Collections.emptyList() : getListOfStringsMatchingLastWord(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
    }

    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return p_82358_2_ == 0;
    }

    public void execute(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException {
        if (p_184881_3_.length != 4) {
            throw new WrongUsageException(getUsage(null));
        } else {
            Entity chooseEntity = getEntity(p_184881_1_, p_184881_2_, p_184881_3_[0]);
            if (chooseEntity.world != null) {
                int lvt_6_2_ = 1;
                String x = p_184881_3_[lvt_6_2_++];
                String y = p_184881_3_[lvt_6_2_++];
                String z = p_184881_3_[lvt_6_2_++];
                double targetX = chooseEntity.posX + parseDouble(x) * rd.nextDouble() * (rd.nextBoolean() ? 1 : -1);
                double targetY = chooseEntity.posY + parseDouble(y) * rd.nextDouble() * (rd.nextBoolean() ? 1 : -1);
                double targetZ = chooseEntity.posZ + parseDouble(z) * rd.nextDouble() * (rd.nextBoolean() ? 1 : -1);
                if (chooseEntity instanceof EntityPlayerMP) {
                    Set<SPacketPlayerPosLook.EnumFlags> lvt_6_1_ = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);
                    EntityPlayerMP player = ((EntityPlayerMP) chooseEntity);
                    player.connection.setPlayerLocation(targetX, targetY, targetZ, chooseEntity.rotationYaw, chooseEntity.rotationPitch, lvt_6_1_);
                } else {
                    chooseEntity.setLocationAndAngles(targetX, targetY, targetZ, chooseEntity.rotationYaw, chooseEntity.rotationPitch);
                }
                notifyCommandListener(p_184881_2_, this, "successful", chooseEntity.getName());
            }
        }
    }

}
