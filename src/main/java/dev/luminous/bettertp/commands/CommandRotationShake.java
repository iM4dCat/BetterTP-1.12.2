package dev.luminous.bettertp.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.*;

public class CommandRotationShake extends CommandBase {
    public CommandRotationShake() {
    }

    static Random rd = new Random();

    public String getName() {
        return "rotationshake";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender p_71518_1_) {
        return "/rotationshake <entity> <yaw> <pitch>";
    }

    public List<String> getTabCompletions(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_) {
        return p_184883_3_.length != 1 && p_184883_3_.length != 2 ? Collections.emptyList() : getListOfStringsMatchingLastWord(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
    }

    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return p_82358_2_ == 0;
    }

    public void execute(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException {
        if (p_184881_3_.length != 3) {
            throw new WrongUsageException(getUsage(null));
        } else {
            Entity lvt_5_2_;
            lvt_5_2_ = getEntity(p_184881_1_, p_184881_2_, p_184881_3_[0]);
            if (lvt_5_2_.world != null) {
                int lvt_6_2_ = 1;
                double shakeYaw = parseDouble(p_184881_3_[lvt_6_2_++]);
                double shakePitch = parseDouble(p_184881_3_[lvt_6_2_++]);

                addEntityRotation(lvt_5_2_, shakePitch * rd.nextDouble() * (rd.nextBoolean() ? -1 : 1), shakeYaw * rd.nextDouble()* (rd.nextBoolean() ? -1 : 1));
                notifyCommandListener(p_184881_2_, this, "successful", lvt_5_2_.getName());
            }
        }
    }

    private static void addEntityRotation(Entity p_189863_0_, double shakePitch, double shakeYaw) {
        float lvt_7_1_;
        if (p_189863_0_ instanceof EntityPlayerMP) {
            Set<SPacketPlayerPosLook.EnumFlags> lvt_6_1_ = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);

            lvt_7_1_ = (float) shakeYaw;
            lvt_7_1_ += p_189863_0_.rotationYaw;
            lvt_7_1_ = MathHelper.wrapDegrees(lvt_7_1_);

            float lvt_8_1_ = (float) shakePitch;
            lvt_8_1_ += p_189863_0_.rotationPitch;
            lvt_8_1_ = MathHelper.wrapDegrees(lvt_8_1_);

            ((EntityPlayerMP) p_189863_0_).connection.setPlayerLocation(p_189863_0_.posX,
                    p_189863_0_.posY,
                    p_189863_0_.posZ,
                    lvt_7_1_,
                    lvt_8_1_,
                    lvt_6_1_);
            p_189863_0_.setRotationYawHead(lvt_7_1_);
        } else {
            float lvt_6_2_ = (float) MathHelper.wrapDegrees(shakePitch);
            lvt_7_1_ = (float) MathHelper.wrapDegrees(shakeYaw);
            lvt_7_1_ = MathHelper.clamp(lvt_7_1_, -90.0F, 90.0F);
            p_189863_0_.setLocationAndAngles(p_189863_0_.posX, p_189863_0_.posY, p_189863_0_.posZ, lvt_6_2_, lvt_7_1_);
            p_189863_0_.setRotationYawHead((float) shakeYaw);
        }

        if (!(p_189863_0_ instanceof EntityLivingBase) || !((EntityLivingBase) p_189863_0_).isElytraFlying()) {
            p_189863_0_.motionY = 0.0;
            p_189863_0_.onGround = true;
        }
    }
}
