package su.harbingers_of_chaos.util;

import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;

class ZombeMoveControl extends MoveControl {
    private final MobEntity panda;
    private final PlayerEntity player;
    public ZombeMoveControl(MobEntity panda, PlayerEntity player) {
        super(panda);
        this.panda = panda;
        this.player = player;
    }
    public void tick() {
        panda.forwardSpeed = player.forwardSpeed;
    }
}
