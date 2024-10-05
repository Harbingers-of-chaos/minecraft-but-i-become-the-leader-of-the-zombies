package su.harbingers_of_chaos.interfaces;

import net.minecraft.entity.mob.MobEntity;

public interface ServerPlayerEntityInterface {
    public void setControlled(MobEntity entity);
    public void exitControlled();
}
