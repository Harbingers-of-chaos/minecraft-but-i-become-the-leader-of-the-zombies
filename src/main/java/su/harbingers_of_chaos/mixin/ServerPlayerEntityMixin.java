package su.harbingers_of_chaos.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.GameModeCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.harbingers_of_chaos.interfaces.ServerPlayerEntityInterface;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityInterface {
    GameModeCommand

    @Unique
    private Vec3d savePosition = new Vec3d(0, 0, 0);
    @Unique
    private ServerPlayerEntity serverPlayer = (ServerPlayerEntity) (Object) this;
    @Unique
    private boolean conntol = false;

    @Inject(method = "attack", at = @At ( "HEAD" ) , cancellable =  true)
    private void onAttack(Entity target, CallbackInfo ci) {
        ((PlayerEntity)serverPlayer).attack(target);
        ci.cancel();
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if(conntol && serverPlayer.isSneaking()){
            exitControlled();
        }
    }

    @Override
    public void setControlled(MobEntity entity) {
        conntol = true;
        serverPlayer.changeGameMode(GameMode.SPECTATOR);
        savePosition = serverPlayer.getPos();
        serverPlayer.setCameraEntity(entity);
    }

    @Override
    public void exitControlled() {
        conntol = false;
        serverPlayer.setPos(savePosition.x, savePosition.y, savePosition.z);
        serverPlayer.changeGameMode(GameMode.SURVIVAL);
        serverPlayer.setCameraEntity(serverPlayer);
    }
}
