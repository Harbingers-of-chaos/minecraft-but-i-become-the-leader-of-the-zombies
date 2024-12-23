package su.harbingers_of_chaos.interfaces;

import net.minecraft.entity.mob.MobEntity;

import java.util.LinkedHashMap;
import java.util.UUID;

public interface MinecraftClientInterface {
//    public LinkedHashMap<String, MobEntity> getZombes();
    public LinkedHashMap<String, UUID> getZombesUUID();
    public void addZombee(MobEntity zombee);
    public boolean isControlling();
    public void setControlling(boolean control,MobEntityInterface uuid);
    public void setControlling(boolean control);
    public MobEntityInterface getControlled();
}
