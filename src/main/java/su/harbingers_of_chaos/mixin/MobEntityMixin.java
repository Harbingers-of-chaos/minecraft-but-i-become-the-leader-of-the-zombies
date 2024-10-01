package su.harbingers_of_chaos.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.GhastEntityRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracked;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.command.DamageCommand;
import net.minecraft.util.Arm;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.harbingers_of_chaos.Leaderofthezombies;
import su.harbingers_of_chaos.interfaces.MinecraftClientInterface;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;
import su.harbingers_of_chaos.util.ModTags;

import static net.minecraft.entity.MovementType.SELF;
import static su.harbingers_of_chaos.Leaderofthezombies.LOGGER;
import static su.harbingers_of_chaos.Leaderofthezombies.collected;
import static su.harbingers_of_chaos.Leaderofthezombies.stages;
import static su.harbingers_of_chaos.LeaderofthezombiesClient.MC;
import static su.harbingers_of_chaos.util.ModTags.INTELLIGENT_BEINGS;

@Mixin(MobEntity.class)
public class MobEntityMixin implements MobEntityInterface  {
    @Unique
    private long time_zom = 0;
    @Shadow
    private static TrackedData<Byte> MOB_FLAGS;
    @Shadow
    private MoveControl moveControl;

    //    private static final TrackedData<Boolean> ZOMBE = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BOOLEAN);;

    @Shadow protected MoveControl moveControl;
    @Shadow private boolean persistent;
    private static final byte time_tran = 0;
    @Unique
    private MobEntity mobEntity = (MobEntity) (Object) this;

//    @Inject(method = "getControllingPassenger", at = @At("HEAD"), cancellable = true)
//    public void getControllingPassenger(CallbackInfoReturnable<LivingEntity> cir) {
//        if(isControl()) cir.setReturnValue(player);
//    }
//    @Unique
//    public void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
//        Vec2f vec2f = this.getControlledRotation(controllingPlayer);
//        this.setRotation(vec2f.y, vec2f.x);
//        mobEntity.prevYaw = mobEntity.bodyYaw = mobEntity.headYaw = mobEntity.getYaw();
//        if (mobEntity.isLogicalSideForUpdatingMovement()) {
////            if (movementInput.z <= 0.0) {
////                mobEntity.soundTicks = 0;
////            }
//
////            if (mobEntity.isOnGround()) {
////                mobEntity.setInAir(false);
////                if (mobEntity.jumpStrength > 0.0F && !this.isInAir()) {
////                    mobEntity.jump(this.jumpStrength, movementInput);
////                }
////
////                mobEntity.jumpStrength = 0.0F;
////            }
//        }
//    }
//    @Unique
//    protected Vec2f getControlledRotation(LivingEntity controllingPassenger) {
//        return new Vec2f(controllingPassenger.getPitch() * 0.5F, controllingPassenger.getYaw());
//    }
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    public void setTarget(@Nullable LivingEntity target, CallbackInfo callback) {
        if (target != null) {
                if (target.getType() == EntityType.PLAYER && mobEntity instanceof Monster &&
                        (stages >= 5 ? !mobEntity.getType().isIn(ModTags.NOOVERMOB) : stages >= 2 ? mobEntity.getType().isIn(EntityTypeTags.ZOMBIES) ||
                                mobEntity.getType().isIn(EntityTypeTags.SKELETONS) : false)) callback.cancel();
        }
    }

    private Vec3d addAngleToVector(double angle, double y, float x, float z) {

        float f = (float) (angle * 0.017453292F);
        double d = (double)MathHelper.sin(f);
        double e = (double)MathHelper.cos(f);
        double newX = z * e - x * d;
        double newZ = x * e + z * d;
        return new Vec3d(newX, y, newZ);
    }
    @Inject(method = "tick",at = @At("TAIL"))
    void tick(CallbackInfo ci) {
//        if (mobEntity.isAlive() && isControl() && MC.player != null&& MC.player.input != null) {
//
//            mobEntity.setAngles(MC.player.getYaw(),MC.player.getPitch());
//            mobEntity.setHeadYaw(MC.player.getYaw());
//
//            if (MC.player.input.jumping && !mobEntity.isOnGround()) mobEntity.jump();
//            LOGGER.info("isOnGround() "+mobEntity.isOnGround());
//            LOGGER.info("jumping "+MC.player);
////            LOGGER.info("yaw: " + MC.player.getYaw() % 360.0F);
//            LOGGER.info("move f:"+MC.player.getMovementDirection().getVector().toString());
////            LOGGER.info("move f:"+addAngleToVector(MC.player.getYaw(),mobEntity.getVelocity().y,MC.player.input.movementForward,MC.player.input.movementSideways).toString());
//            mobEntity.move(SELF,addAngleToVector(MC.player.getYaw() % 360.0F,mobEntity.getVelocity().y,MC.player.input.movementForward,MC.player.input.movementSideways));
//        }
        if(!isZombe() &&time_zom!=0 && (time_tran * 1200) <= mobEntity.getWorld().getTime() - time_zom){
            LOGGER.info("Entity tick:"+mobEntity.getName());
            LOGGER.info("getId:" + mobEntity.getId());
            LOGGER.info("getUuid:" + mobEntity.getUuid());
            mobEntity.removeCommandTag("infections");
            moveControl =
            setZombe(true);
            if(MC.getServer().getOverworld().getEntity(mobEntity.getUuid()) instanceof MobEntity){
                MobEntity entity = (MobEntity) MC.getServer().getOverworld().getEntity(mobEntity.getUuid());
                ((MobEntityInterface)entity).setZombe(true);

            }
            ((MinecraftClientInterface) MC).addZombee(mobEntity);
        }
    }
    @Inject(method = "writeCustomDataToNbt",at = @At("TAIL"))
    void writeCustomDataToNbt(NbtCompound nbt,CallbackInfo ci) {
//        LOGGER.info("write zombie:"+isZombe());
//        if(isZombe()) LOGGER.info("zombie:"+mobEntity);
        nbt.putBoolean("zombie", isZombe());
        nbt.putBoolean("control", isControl());
    }

    @Inject(method = "readCustomDataFromNbt",at = @At("TAIL"))
    void readCustomDataFromNbt(NbtCompound nbt,CallbackInfo ci) {
//        LOGGER.info("read zombie:"+nbt.getBoolean("zombie"));
        this.setZombe(nbt.getBoolean("zombie"));
        this.setControl(nbt.getBoolean("control"));
    }
//    @Inject(method = "initDataTracker",at = @At("TAIL"))
//    void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
//        builder.add(MOB_FLAGS, false);
//    }
    @Override
    public void setZombe(boolean zombe) {
        byte b = (Byte)mobEntity.getDataTracker().get(MOB_FLAGS);
        mobEntity.getDataTracker().set(MOB_FLAGS, zombe ? (byte)(b | 8) : (byte)(b & -9));
    }
    @Override
    public void setControl(boolean control) {
        mobEntity.setAiDisabled(control);
        byte b = (Byte)mobEntity.getDataTracker().get(MOB_FLAGS);
        mobEntity.getDataTracker().set(MOB_FLAGS, control ? (byte)(b | 16) : (byte)(b & -15));
    }
    @Override
    public void setPlayer(PlayerEntity player) {
//        this.player = player;
    }
    @Override
    public boolean isZombe() {
        return ((Byte)mobEntity.getDataTracker().get(MOB_FLAGS) & 8) != 0;
    }
    @Override
    public boolean isControl() {
        return ((Byte)mobEntity.getDataTracker().get(MOB_FLAGS) & 16) != 0;
    }
    @Override
    public void Infections(MinecraftClient client) {
        this.time_zom = mobEntity.getWorld().getTime();
        if (mobEntity.addCommandTag("infections")) {

            if(client.getServer().getOverworld().getEntity(client.player.getUuid()) instanceof PlayerEntity ){
                PlayerEntity player = (PlayerEntity) client.getServer().getOverworld().getEntity(client.player.getUuid());
                ((MobEntityInterface)mobEntity).setPlayer(player);
            }
            Leaderofthezombies.LOGGER.info("Entity: infections");
            if (stages == 3){
                collected++;
                if (collected == 10){
                    Leaderofthezombies.setStages(0);
                }
            }
        }
    }

}
