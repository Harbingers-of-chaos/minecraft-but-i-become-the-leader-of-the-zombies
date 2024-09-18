package su.harbingers_of_chaos.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.GhastEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.data.DataTracked;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.harbingers_of_chaos.Leaderofthezombies;
import su.harbingers_of_chaos.interfaces.MinecraftClientInterface;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;

import static su.harbingers_of_chaos.Leaderofthezombies.LOGGER;
import static su.harbingers_of_chaos.Leaderofthezombies.collected;
import static su.harbingers_of_chaos.Leaderofthezombies.stages;

@Mixin(MobEntity.class)
public class MobEntityMixin implements MobEntityInterface                                                            {
    @Unique
    private long time_zom = 0;
    @Shadow(remap = true)
    private static TrackedData<Byte> MOB_FLAGS;
//    private static final TrackedData<Boolean> ZOMBE = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BOOLEAN);;
    private MinecraftClient client;
    private static final byte time_tran = 0;
    @Unique
    private MobEntity mobEntity = (MobEntity) (Object) this;


    @Inject(method = "tick",at = @At("TAIL"))
    void tick(CallbackInfo ci) {
        if(!isZombe() &&time_zom!=0 && (time_tran * 1200) <= mobEntity.getWorld().getTime() - time_zom){
            LOGGER.info("Entity tick:"+mobEntity.getName());
            LOGGER.info("getId:" + mobEntity.getId());
            LOGGER.info("getUuid:" + mobEntity.getUuid());
            mobEntity.removeCommandTag("infections");
            setZombe(true);
            if(client.getServer().getOverworld().getEntity(mobEntity.getUuid()) instanceof MobEntity){
                MobEntity entity = (MobEntity) client.getServer().getOverworld().getEntity(mobEntity.getUuid());
                ((MobEntityInterface)entity).setZombe(true);
            }
            ((MinecraftClientInterface) client).addZombee(mobEntity);
        }
    }
    @Inject(method = "writeCustomDataToNbt",at = @At("TAIL"))
    void writeCustomDataToNbt(NbtCompound nbt,CallbackInfo ci) {
//        LOGGER.info("write zombie:"+isZombe());
//        if(isZombe()) LOGGER.info("zombie:"+mobEntity);
        nbt.putBoolean("zombie", isZombe());
    }

    @Inject(method = "readCustomDataFromNbt",at = @At("TAIL"))
    void readCustomDataFromNbt(NbtCompound nbt,CallbackInfo ci) {
//        LOGGER.info("read zombie:"+nbt.getBoolean("zombie"));
        this.setZombe(nbt.getBoolean("zombie"));
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
    public boolean isZombe() {
        return ((Byte)mobEntity.getDataTracker().get(MOB_FLAGS) & 8) != 0;
    }
    @Override
    public void Infections(MinecraftClient client) {
        this.time_zom = mobEntity.getWorld().getTime();
        if (mobEntity.addCommandTag("infections")) {
            this.client = client;
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
