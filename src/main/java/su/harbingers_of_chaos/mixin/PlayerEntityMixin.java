package su.harbingers_of_chaos.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    private PlayerEntity player = (PlayerEntity) (Object) this;
    private Entity target = (Entity) (Object) this;
    @Inject(method = "tickMovement", at = @At( "HEAD" ))
    private void onTickMovement(CallbackInfo ci) {
        if (player.isAlive()) {
            boolean bl = this.isAffectedByDaylight();
            if (bl) {
                ItemStack itemStack = player.getEquippedStack(EquipmentSlot.HEAD);
                if (!itemStack.isEmpty()) {
                    if (itemStack.isDamageable()) {
                        Item item = itemStack.getItem();
                        itemStack.setDamage(itemStack.getDamage() + target.getRandom().nextInt(2));
                        if (itemStack.getDamage() >= itemStack.getMaxDamage()) {
                            player.sendEquipmentBreakStatus(item, EquipmentSlot.HEAD);
                            player.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    bl = false;
                }

                if (bl) {
                    player.setOnFireFor(8.0F);
                }
            }
        }
    }
    protected boolean isAffectedByDaylight() {
        if (player.getWorld().isDay() && !player.getWorld().isClient) {
            float f = player.getBrightnessAtEyes();
            BlockPos blockPos = BlockPos.ofFloored(player.getX(), player.getEyeY(), player.getZ());
            boolean bl = player.isWet() || player.inPowderSnow || player.wasInPowderSnow;
            if (f > 0.5F && target.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !bl && player.getWorld().isSkyVisible(blockPos)) {
                return true;
            }
        }

        return false;
    }
}
