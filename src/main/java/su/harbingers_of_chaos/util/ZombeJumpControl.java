package su.harbingers_of_chaos.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ZombeJumpControl extends JumpControl {
    private final ClientPlayerEntity player;
    private MobEntity entity;

    public ZombeJumpControl(MobEntity mobEntity, ClientPlayerEntity player) {
        super(mobEntity);
        this.player = player;
        this.entity = mobEntity;
    }
    public void tick() {
        if (((MobEntityInterface) entity).isControl()) {
            if (player != null && player.input != null) {
//                LOGGER.info("jump:"+entity);
                this.entity.setJumping(player.input.jumping);
            }
        }else super.tick();
    }
}
