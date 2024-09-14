package su.harbingers_of_chaos.mixin.textures;

import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;

import java.util.Map;

import static su.harbingers_of_chaos.Leaderofthezombies.MOD_ID;

@Mixin(HorseEntityRenderer.class)
public class HorseEntityRendererMixin {
    @Shadow
    private static Map<HorseColor, Identifier> TEXTURES;
    private static final Identifier TEXTURE_ZOMBE = Identifier.of(MOD_ID,"textures/entity/horse.png");

    @Inject(method = "getTexture", at = @At("RETURN"), cancellable = true)
    private void injected(HorseEntity entity, CallbackInfoReturnable<Identifier> cir) {
        cir.setReturnValue(((MobEntityInterface)entity).isZombe()?TEXTURE_ZOMBE:TEXTURES.get(entity.getVariant()));
//        cir.setReturnValue(TEXTURE);
    }
}
