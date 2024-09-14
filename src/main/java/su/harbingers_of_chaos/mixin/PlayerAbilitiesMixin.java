package su.harbingers_of_chaos.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.harbingers_of_chaos.interfaces.Stages;

import static su.harbingers_of_chaos.Leaderofthezombies.*;


@Mixin(PlayerAbilities.class)
public class PlayerAbilitiesMixin implements Stages {



    @Shadow(remap = true)
    public boolean invulnerable;

    @Shadow(remap = true)
    public boolean flying;

    @Shadow(remap = true)
    public boolean allowFlying;

    @Shadow(remap = true)
    public boolean creativeMode;

    @Shadow(remap = true)
    public boolean allowModifyWorld;

    @Shadow(remap = true)
    private float flySpeed;

    @Shadow(remap = true)
    private float walkSpeed;

    @Inject(method = "writeNbt", at = @At ( "HEAD" ) , cancellable =  true)
    private void onWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putBoolean("invulnerable", this.invulnerable);
        nbtCompound.putBoolean("flying", this.flying);
        nbtCompound.putBoolean("mayfly", this.allowFlying);
        nbtCompound.putBoolean("instabuild", this.creativeMode);
        nbtCompound.putBoolean("mayBuild", this.allowModifyWorld);
        nbtCompound.putFloat("flySpeed", this.flySpeed);
        nbtCompound.putFloat("walkSpeed", this.walkSpeed);
        nbtCompound.putByte("stages", stages);
        nbtCompound.putByte("collected", collected);
        nbt.put("abilities", nbtCompound);
        if (SharedConstants.isDevelopment) LOGGER.info("writeNbt: " + nbt.toString());
        ci.cancel();
    }

    @Inject(method = "readNbt", at = @At ( "HEAD" ) , cancellable =  true)
    private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("abilities", 10)) {
            NbtCompound nbtCompound = nbt.getCompound("abilities");
            this.invulnerable = nbtCompound.getBoolean("invulnerable");
            this.flying = nbtCompound.getBoolean("flying");
            this.allowFlying = nbtCompound.getBoolean("mayfly");
            this.creativeMode = nbtCompound.getBoolean("instabuild");
            if(nbtCompound.getByte("stages")!=0){
                stages = nbtCompound.getByte("stages");
                collected = nbtCompound.getByte("collected");
            }
            if (nbtCompound.contains("flySpeed", 99)) {
                this.flySpeed = nbtCompound.getFloat("flySpeed");
                this.walkSpeed = nbtCompound.getFloat("walkSpeed");
            }

            if (nbtCompound.contains("mayBuild", 1)) {
                this.allowModifyWorld = nbtCompound.getBoolean("mayBuild");
            }
        }
        if (SharedConstants.isDevelopment) LOGGER.info("readNbt: " + nbt.toString());
        ci.cancel();
    }

    @Override
    public byte getStages() {
        return stages;
    }

    @Override
    public void setStage(byte value) {
        stages = value;
    }
}
