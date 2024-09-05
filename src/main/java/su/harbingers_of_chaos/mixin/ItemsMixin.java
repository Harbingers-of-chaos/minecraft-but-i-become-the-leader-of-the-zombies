package su.harbingers_of_chaos.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.command.TitleCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.harbingers_of_chaos.Leaderofthezombies;
import su.harbingers_of_chaos.util.ModTags;

import java.util.Optional;

import static net.minecraft.item.Items.register;
import static su.harbingers_of_chaos.Leaderofthezombies.*;

@Mixin(PlayerEntity.class)
public class ItemsMixin {
    @Shadow
    public HungerManager getHungerManager() {
        return this.hungerManager;
    }
    @Shadow
    public void incrementStat(Stat<?> stat) {
        this.increaseStat((Stat)stat, 1);
    }
    @Shadow
    public void increaseStat(Stat<?> stat, int amount) {}
    @Shadow
    protected HungerManager hungerManager;


    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void onEatFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        LOGGER.info("Stages:"+String.valueOf(stages));
        LOGGER.info("Exercise:"+String.valueOf(collected));
        if(stack.isIn(ItemTags.MEAT) && !stack.isIn(ModTags.COOKED_MEAT)){
            if (stages == 1){
                collected++;
                if (collected == 30){
                    Leaderofthezombies.setStages(0);
                }
            }
            if (SharedConstants.isDevelopment) LOGGER.info("MEAT not cooked");
        }else if(stack.isIn(ModTags.COOKED_MEAT)){
            if (SharedConstants.isDevelopment) LOGGER.info("COOKED");
        }
    }
}
