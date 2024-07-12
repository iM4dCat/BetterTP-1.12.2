package dev.luminous.bettertp.asm.mixins;

import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.EnumSet;
import java.util.Set;

@Mixin(CommandTP.class)
public abstract class MixinCommandTP extends CommandBase  {

    /**
     * @author luminous
     * @reason bettertp
     */
    @Overwrite
    public void execute(MinecraftServer p_184881_1_, ICommandSender p_184881_2_, String[] p_184881_3_) throws CommandException {
        if (p_184881_3_.length < 1) {
            throw new WrongUsageException("commands.tp.usage");
        } else {
            int lvt_4_1_ = 0;
            Entity lvt_5_2_;
            if (p_184881_3_.length != 2 && p_184881_3_.length != 4 && p_184881_3_.length != 6) {
                lvt_5_2_ = getCommandSenderAsPlayer(p_184881_2_);
            } else {
                lvt_5_2_ = getEntity(p_184881_1_, p_184881_2_, p_184881_3_[0]);
                lvt_4_1_ = 1;
            }

            if (p_184881_3_.length != 1 && p_184881_3_.length != 2) {
                if (lvt_5_2_.world != null) {
                    int lvt_6_2_ = lvt_4_1_;
                    String x = p_184881_3_[lvt_6_2_++];
                    String y = p_184881_3_[lvt_6_2_++];
                    String z = p_184881_3_[lvt_6_2_++];
                    CoordinateArg targetX;
                    CoordinateArg targetY;
                    CoordinateArg targetZ;
                    CoordinateArg targetYaw = parseCoordinate(lvt_5_2_.rotationYaw, p_184881_3_.length > lvt_6_2_ ? p_184881_3_[lvt_6_2_++] : "~", false);
                    CoordinateArg targetPitch = parseCoordinate(lvt_5_2_.rotationPitch, p_184881_3_.length > lvt_6_2_ ? p_184881_3_[lvt_6_2_] : "~", false);
                    float yaw = (float) (targetYaw.isRelative() ? lvt_5_2_.rotationYaw + targetYaw.getAmount() : targetYaw.getAmount());
                    float pitch = (float) (targetPitch.isRelative() ? lvt_5_2_.rotationPitch + targetPitch.getAmount() : targetPitch.getAmount());
                    String xNumber = x.replaceFirst("\\^", "");
                    String yNumber = y.replaceFirst("\\^", "");
                    String zNumber = z.replaceFirst("\\^", "");
                    if (xNumber.isEmpty()) {
                        xNumber = "0";
                    }
                    if (yNumber.isEmpty()) {
                        yNumber = "0";
                    }
                    if (zNumber.isEmpty()) {
                        zNumber = "0";
                    }
                    boolean v = !y.startsWith("^") || (parseDouble(yNumber) == 0);
                    Vec3d forward = Vec3d.fromPitchYaw(v ? 0 : pitch, yaw);
                    if (x.startsWith("^")) {
                        double forwardX = forward.x * parseDouble(xNumber);
                        targetX = parseCoordinate(lvt_5_2_.posX, "~" + forwardX, false);
                    } else {
                        targetX = parseCoordinate(lvt_5_2_.posX, x, true);
                    }
                    if (y.startsWith("^")) {
                        double forwardY = forward.y * parseDouble(yNumber);
                        targetY = parseCoordinate(lvt_5_2_.posY, "~" + forwardY, false);
                    } else {
                        targetY = parseCoordinate(lvt_5_2_.posY, y, -4096, 4096, false);
                    }
                    if (z.startsWith("^")) {
                        double forwardZ = forward.z * parseDouble(zNumber);
                        targetZ = parseCoordinate(lvt_5_2_.posZ, "~" + forwardZ, false);
                    } else {
                        targetZ = parseCoordinate(lvt_5_2_.posZ, z, true);
                    }
                    teleportEntityToCoordinates(lvt_5_2_, targetX, targetY, targetZ, targetYaw, targetPitch);
                    notifyCommandListener(p_184881_2_, this, "commands.tp.success.coordinates", lvt_5_2_.getName(), targetX.getResult(), targetY.getResult(), targetZ.getResult());
                }
            } else {
                Entity lvt_6_1_ = getEntity(p_184881_1_, p_184881_2_, p_184881_3_[p_184881_3_.length - 1]);
                if (lvt_6_1_.world != lvt_5_2_.world) {
                    throw new CommandException("commands.tp.notSameDimension");
                } else {
                    lvt_5_2_.dismountRidingEntity();
                    if (lvt_5_2_ instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)lvt_5_2_).connection.setPlayerLocation(lvt_6_1_.posX, lvt_6_1_.posY, lvt_6_1_.posZ, lvt_6_1_.rotationYaw, lvt_6_1_.rotationPitch);
                    } else {
                        lvt_5_2_.setLocationAndAngles(lvt_6_1_.posX, lvt_6_1_.posY, lvt_6_1_.posZ, lvt_6_1_.rotationYaw, lvt_6_1_.rotationPitch);
                    }

                    notifyCommandListener(p_184881_2_, this, "commands.tp.success", lvt_5_2_.getName(), lvt_6_1_.getName());
                }
            }
        }
    }

    /**
     * @author luminous
     * @reason bettertp
     */
    @Overwrite
    private static void teleportEntityToCoordinates(Entity p_189863_0_, CommandBase.CoordinateArg p_189863_1_, CommandBase.CoordinateArg p_189863_2_, CommandBase.CoordinateArg p_189863_3_, CommandBase.CoordinateArg p_189863_4_, CommandBase.CoordinateArg p_189863_5_) {
        float lvt_7_1_;
        if (p_189863_0_ instanceof EntityPlayerMP) {
            Set<SPacketPlayerPosLook.EnumFlags> lvt_6_1_ = EnumSet.noneOf(SPacketPlayerPosLook.EnumFlags.class);

            lvt_7_1_ = (float)p_189863_4_.getAmount();
            if (p_189863_4_.isRelative()) {
                lvt_7_1_ += p_189863_0_.rotationYaw;
            }
            lvt_7_1_ = MathHelper.wrapDegrees(lvt_7_1_);

            float lvt_8_1_ = (float)p_189863_5_.getAmount();
            if (p_189863_5_.isRelative()) {
                lvt_8_1_ += p_189863_0_.rotationPitch;
            }
            lvt_8_1_ = MathHelper.wrapDegrees(lvt_8_1_);

            p_189863_0_.dismountRidingEntity();
            ((EntityPlayerMP)p_189863_0_).connection.setPlayerLocation(p_189863_1_.getAmount() + (p_189863_1_.isRelative() ? p_189863_0_.posX : 0),
                    p_189863_2_.getAmount() + (p_189863_2_.isRelative() ? p_189863_0_.posY : 0),
                    p_189863_3_.getAmount() + (p_189863_3_.isRelative() ? p_189863_0_.posZ : 0),
                    lvt_7_1_,
                    lvt_8_1_,
                    lvt_6_1_);
            p_189863_0_.setRotationYawHead(lvt_7_1_);
        } else {
            float lvt_6_2_ = (float)MathHelper.wrapDegrees(p_189863_4_.getResult());
            lvt_7_1_ = (float)MathHelper.wrapDegrees(p_189863_5_.getResult());
            lvt_7_1_ = MathHelper.clamp(lvt_7_1_, -90.0F, 90.0F);
            p_189863_0_.setLocationAndAngles(p_189863_1_.getResult(), p_189863_2_.getResult(), p_189863_3_.getResult(), lvt_6_2_, lvt_7_1_);
            p_189863_0_.setRotationYawHead(lvt_6_2_);
        }

        if (!(p_189863_0_ instanceof EntityLivingBase) || !((EntityLivingBase)p_189863_0_).isElytraFlying()) {
            p_189863_0_.motionY = 0.0;
            p_189863_0_.onGround = true;
        }
    }
}
