package su.harbingers_of_chaos.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.harbingers_of_chaos.interfaces.MinecraftClientInterface;

import java.util.LinkedHashMap;
import java.util.UUID;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientInterface {
    private static final String[] names = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38"};
    private LinkedHashMap<String, UUID> zombies = new LinkedHashMap<>();
    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity.getCommandTags().contains("infections")) {
                ci.setReturnValue(true);
        }
    }

    @Override
    public LinkedHashMap<String, UUID> getZombes() {
        return zombies;
    }

    @Override
    public void addZombee(MobEntity zombee) {
        zombies.put(names[zombies.size()],zombee.getUuid());
    }
}
