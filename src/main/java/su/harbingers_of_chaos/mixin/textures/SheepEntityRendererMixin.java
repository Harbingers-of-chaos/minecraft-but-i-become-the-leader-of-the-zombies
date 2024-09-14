package su.harbingers_of_chaos.mixin.textures;

import net.minecraft.client.render.entity.SheepEntityRenderer;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;

import static su.harbingers_of_chaos.Leaderofthezombies.MOD_ID;

@Mixin(SheepEntityRenderer.class)
public class SheepEntityRendererMixin {
    @Shadow
    private static Identifier TEXTURE;
    private static final Identifier TEXTURE_ZOMBE = Identifier.of(MOD_ID,"textures/entity/sheep.png");

    @Inject(method = "getTexture", at = @At("RETURN"), cancellable = true)
    private void injected(SheepEntity entity, CallbackInfoReturnable<Identifier> cir) {
        cir.setReturnValue(((MobEntityInterface)entity).isZombe()?TEXTURE_ZOMBE:TEXTURE);
//        cir.setReturnValue(TEXTURE);
    }
}
