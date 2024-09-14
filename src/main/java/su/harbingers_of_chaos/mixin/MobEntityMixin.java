package su.harbingers_of_chaos.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.GhastEntityRenderer;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
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
public class MobEntityMixin implements MobEntityInterface {
    @Unique
    private long time_zom = 0;
    private boolean zombe = false;
    private MinecraftClientInterface client;
    private static final byte time_tran = 0;
    @Unique
    private MobEntity mobEntity = (MobEntity) (Object) this;


    @Inject(method = "tick",at = @At("TAIL"))
    void tick(CallbackInfo ci) {
        if(!zombe&&time_zom!=0 && (time_tran * 1200) <= mobEntity.getWorld().getTime() - time_zom){
            LOGGER.info("Entity tick:"+mobEntity.getName());
            LOGGER.info("getId:" + mobEntity.getId());
            LOGGER.info("getUuid:" + mobEntity.getUuid());
            mobEntity.removeCommandTag("infections");
            zombe = true;
            client.addZombee(mobEntity);
        }
    }

    @Override
    public boolean isZombe() {
        return zombe;
    }

    @Override
    public void Infections(MinecraftClient client) {
        this.time_zom = mobEntity.getWorld().getTime();
        if (mobEntity.addCommandTag("infections")) {
            this.client = (MinecraftClientInterface) client;
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
