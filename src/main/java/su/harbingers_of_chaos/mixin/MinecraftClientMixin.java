package su.harbingers_of_chaos.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.command.SpectateCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.harbingers_of_chaos.interfaces.MinecraftClientInterface;
import su.harbingers_of_chaos.interfaces.Stages;

import java.util.LinkedHashMap;
import java.util.UUID;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientInterface {
    @Unique
    private static final String[] names = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38"};
//    private LinkedHashMap<String, MobEntity> zombies = new LinkedHashMap<>();
    @Unique
    private LinkedHashMap<String, UUID> zombiesUUID = new LinkedHashMap<>();
    @Unique
    private MinecraftClient minecraftClient = (MinecraftClient) (Object) this;
    @Unique
    private boolean controlling = false;
    @Unique
    private UUID controlled;

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void outlineEntities(Entity entity, CallbackInfoReturnable<Boolean> ci) {
        if (entity.getCommandTags().contains("infections")) ci.setReturnValue(true);
    }
    @Override
    public LinkedHashMap<String, UUID> getZombesUUID() {
        return zombiesUUID;
    }
    @Override
    public void addZombee(MobEntity zombee) {
//        zombies.put(names[zombies.size()],zombee);
        zombiesUUID.put(String.valueOf(zombiesUUID.size()), zombee.getUuid());
        ((Stages) minecraftClient.getServer().getPlayerManager().getPlayer(minecraftClient.player.getUuid()).getAbilities()).setZombies(String.valueOf(zombiesUUID.size()), zombee.getUuid());
    }
    @Override
    public void setControlling(boolean control,UUID uuid) {
        controlling = control;
        controlled = uuid;
    }
    @Override
    public boolean isControlling() {
        return controlling;
    }

    @Override
    public UUID getControlled() {
        return controlled;
    }
}
