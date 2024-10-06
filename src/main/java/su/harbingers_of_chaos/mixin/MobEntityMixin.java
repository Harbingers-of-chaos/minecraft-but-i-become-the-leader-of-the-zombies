package su.harbingers_of_chaos.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.EntityTypeTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.harbingers_of_chaos.Leaderofthezombies;
import su.harbingers_of_chaos.interfaces.MinecraftClientInterface;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;
import su.harbingers_of_chaos.util.ModTags;
import su.harbingers_of_chaos.util.ZombeJumpControl;
import su.harbingers_of_chaos.util.ZombeLookControl;
import su.harbingers_of_chaos.util.ZombeMoveControl;

import static su.harbingers_of_chaos.Leaderofthezombies.*;
import static su.harbingers_of_chaos.LeaderofthezombiesClient.MC;

@Mixin(MobEntity.class)
public class MobEntityMixin implements MobEntityInterface  {
    @Shadow
    private static TrackedData<Byte> MOB_FLAGS;
    @Shadow
    private MoveControl moveControl;
    @Shadow
    private LookControl lookControl;
    @Shadow
    private JumpControl jumpControl;

    @Unique
    private long time_zom = 0;
    @Unique
    private static final byte time_tran = 0;
    @Unique
    private MobEntity mobEntity = (MobEntity) (Object) this;

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    public void noTouch(@Nullable LivingEntity target, CallbackInfo callback) {
        if (target != null) {
                if (target.getType() == EntityType.PLAYER && mobEntity instanceof Monster &&
                        (stages >= 5 ? !mobEntity.getType().isIn(ModTags.NOOVERMOB) : stages >= 2 ? mobEntity.getType().isIn(EntityTypeTags.ZOMBIES) ||
                                mobEntity.getType().isIn(EntityTypeTags.SKELETONS) : false)) callback.cancel();
        }
    }
    @Inject(method = "<init>",at = @At("TAIL"))
    void init(CallbackInfo ci) {
        moveControl = new ZombeMoveControl(mobEntity,MC.player);
        jumpControl = new ZombeJumpControl(mobEntity,MC.player);
        lookControl = new ZombeLookControl(mobEntity,MC.player);
    }
    @Inject(method = "tick",at = @At("TAIL"))
    void tick(CallbackInfo ci) {
        if(!isZombe() &&time_zom!=0 && (time_tran * 1200) <= mobEntity.getWorld().getTime() - time_zom){
//            LOGGER.info("Entity tick:"+mobEntity.getName());
//            LOGGER.info("getId:" + mobEntity.getId());
//            LOGGER.info("getUuid:" + mobEntity.getUuid());
            mobEntity.removeCommandTag("infections");
            setZombe(true);
            if(MC.getServer().getOverworld().getEntity(mobEntity.getUuid()) instanceof MobEntityInterface entity){
                entity.setZombe(true);
            }
            ((MinecraftClientInterface)MC).addZombee(mobEntity);
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
        if (this.isControl() != nbt.getBoolean("zombie")) this.setZombe(nbt.getBoolean("zombie"));
        if (this.isControl() != nbt.getBoolean("control")) this.setControl(nbt.getBoolean("control"));
    }
    @Override
    public void setZombe(boolean zombe) {
        byte b = (Byte)mobEntity.getDataTracker().get(MOB_FLAGS);
        mobEntity.getDataTracker().set(MOB_FLAGS, zombe ? (byte)(b | 8) : (byte)(b & -9));
    }
    @Override
    public void setControl(boolean control) {
//        LOGGER.info(String.valueOf(control));
//        LOGGER.info("control:"+isControl());
//        LOGGER.info("zombie:"+isZombe());
        byte b = (Byte)mobEntity.getDataTracker().get(MOB_FLAGS);
//        LOGGER.info("byte:"+(b));
//        LOGGER.info("byte:"+(control ? (byte)(b | 16) : (byte)(b & -15)));
        mobEntity.getDataTracker().set(MOB_FLAGS, control ? (byte)(b | 16) : (byte)(b & -17));
//        LOGGER.info("control:"+isControl());
//        LOGGER.info("zombie:"+isZombe());
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
