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
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.*;

public class CommandLookAt extends CommandBase {
    public CommandLookAt() {
    }

    public String getName() {
        return "lookat";
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getUsage(ICommandSender p_71518_1_) {
        return "/lookat <entity> <entity> or /lookat <entity> <x> <y> <z>";
    }

    public List<String> getTabCompletions(MinecraftServer p_184883_1_, ICommandSender p_184883_2_, String[] p_184883_3_, @Nullable BlockPos p_184883_4_) {
        return p_184883_3_.length != 1 && p_184883_3_.length != 2 ? Collections.emptyList() : getListOfStringsMatchingLastWord(p_184883_3_, p_184883_1_.getOnlinePlayerNames());
    }

    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return p_82358_2_ == 0;
    }

    public void execute(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException {
        if (p_184881_3_.length != 4 && p_184881_3_.length != 2) {
            throw new WrongUsageException(getUsage(null));
        } else {
            Entity chooseEntity = getEntity(p_184881_1_, p_184881_2_, p_184881_3_[0]);
            if (chooseEntity.world != null) {
                Vec3d target;
                if (p_184881_3_.length == 4) {
                    double x = parseDouble(p_184881_3_[1]);
                    double y = parseDouble(p_184881_3_[2]);
                    double z = parseDouble(p_184881_3_[3]);
                    target = new Vec3d(x, y, z);
                } else {
                    Entity lookEntity = getEntity(p_184881_1_, p_184881_2_, p_184881_3_[1]);
                    target = new Vec3d(lookEntity.posX, lookEntity.posY, lookEntity.posZ);
                }
                float[] rotations = getRotations(chooseEntity, target);
                setEntityRotation(chooseEntity, rotations[1], rotations[0]);
            }
        }
    }

    private static void setEntityRotation(Entity p_189863_0_, double pitch, double yaw) {
        float lvt_7_1_;
        if (p_189863_0_ instanceof EntityPlayerMP) {
            Set<SPacketPlayerPosLook.EnumFlags> lvt_6_1_ = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);

            lvt_7_1_ = (float) yaw;
            lvt_7_1_ = MathHelper.wrapDegrees(lvt_7_1_);

            float lvt_8_1_ = (float) pitch;
            lvt_8_1_ = MathHelper.wrapDegrees(lvt_8_1_);

            ((EntityPlayerMP) p_189863_0_).connection.setPlayerLocation(p_189863_0_.posX,
                    p_189863_0_.posY,
                    p_189863_0_.posZ,
                    lvt_7_1_,
                    lvt_8_1_,
                    lvt_6_1_);
            p_189863_0_.setRotationYawHead(lvt_7_1_);
        } else {
            float lvt_6_2_ = (float) MathHelper.wrapDegrees(pitch);
            lvt_7_1_ = (float) MathHelper.wrapDegrees(yaw);
            lvt_7_1_ = MathHelper.clamp(lvt_7_1_, -90.0F, 90.0F);
            p_189863_0_.setLocationAndAngles(p_189863_0_.posX, p_189863_0_.posY, p_189863_0_.posZ, lvt_7_1_, lvt_6_2_);
            p_189863_0_.setRotationYawHead((float) yaw);
        }

        if (!(p_189863_0_ instanceof EntityLivingBase) || !((EntityLivingBase) p_189863_0_).isElytraFlying()) {
            p_189863_0_.motionY = 0.0;
            p_189863_0_.onGround = true;
        }
    }

    public static float[] getRotations(Entity entity, Vec3d vec) {
        Vec3d eyesPos = entity.getPositionEyes(1);
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{entity.rotationYaw + MathHelper.wrapDegrees(yaw - entity.rotationYaw), entity.rotationPitch + MathHelper.wrapDegrees(pitch - entity.rotationPitch)};
    }
}
