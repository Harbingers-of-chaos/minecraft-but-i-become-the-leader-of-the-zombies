package su.harbingers_of_chaos.interfaces;

import net.minecraft.entity.mob.MobEntity;

import java.util.LinkedHashMap;
import java.util.UUID;

public interface MinecraftClientInterface {
//    public LinkedHashMap<String, MobEntity> getZombes();
    public LinkedHashMap<String, UUID> getZombesUUID();
    public void addZombee(MobEntity zombee);
}
