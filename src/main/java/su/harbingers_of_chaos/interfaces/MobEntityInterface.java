package su.harbingers_of_chaos.interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public interface MobEntityInterface {
    public void Infections(MinecraftClient client);
    public boolean isZombe();
    public void setZombe(boolean zombe);
    public void setPlayer(PlayerEntity player);
    public boolean isControl();
    public void setControl(boolean control);
}
