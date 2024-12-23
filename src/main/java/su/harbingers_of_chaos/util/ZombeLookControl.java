package su.harbingers_of_chaos.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ZombeLookControl extends LookControl {

    private final ClientPlayerEntity player;
    public ZombeLookControl(MobEntity entity, ClientPlayerEntity player) {
        super(entity);
        this.player = player;
    }

    @Override
    public void tick() {
        if (((MobEntityInterface) entity).isControl()) {
            if (player != null && player.input != null) {
//                LOGGER.info("look");
//                this.entity.setYaw(player.getYaw());
                this.entity.setAngles(player.getYaw(),player.getPitch());
                this.entity.headYaw = player.getYaw();
            }
        }else super.tick();
    }
}
