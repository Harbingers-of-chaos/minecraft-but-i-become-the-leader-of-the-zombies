package su.harbingers_of_chaos.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ZombeMoveControl extends MoveControl {
    private final ClientPlayerEntity player;
    public ZombeMoveControl(MobEntity mobEntity, ClientPlayerEntity player) {
        super(mobEntity);
        this.player = player;
    }
    public void tick() {
        if (((MobEntityInterface) entity).isControl()) {
            if (player != null && player.input != null) {
//                LOGGER.info("move");
//                this.entity.setYaw(player.getYaw());
                ((LivingEntity)this.entity).setMovementSpeed((float)entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                this.entity.setForwardSpeed( player.input.movementForward  * 0.3f);
                this.entity.setSidewaysSpeed(player.input.movementSideways * 0.3f);
//                this.entity.setJumping(player.input.jumping);

//                this.entity.setMovementSpeed(0.1f);
//                this.entity.setSidewaysSpeed(0.1f);

            }
        }else super.tick();
    }
}
