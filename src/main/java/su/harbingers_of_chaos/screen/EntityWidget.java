package su.harbingers_of_chaos.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import su.harbingers_of_chaos.interfaces.MinecraftClientInterface;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;
import su.harbingers_of_chaos.interfaces.ServerPlayerEntityInterface;

import java.util.Objects;
import java.util.UUID;

import static su.harbingers_of_chaos.Leaderofthezombies.LOGGER;

public class EntityWidget  extends ClickableWidget {
    MobEntity models;
    String name;
    MinecraftClient client;
    UUID uuid;
    Screen screen;
    public EntityWidget(int x, int y, int width, int height, MobEntity models, String name, MinecraftClient client, UUID uuid, Screen screen) {
        super(x, y, width, height, ScreenTexts.EMPTY);
        this.models = models;
        this.name = name;
        this.client = client;
        this.uuid = uuid;
        this.screen = screen;
    }
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {

        context.drawText(MinecraftClient.getInstance().textRenderer,name, getX(),getY()+height+2,16711935,true);
//        LOGGER.info("2:"+models.getUuidAsString());
        float g = (float)(getX() + getX()+width) / 2.0F;
        float h = (float)(getY() + getY()+height) / 2.0F;
        context.enableScissor(getX() , getY(), getX()+width, getY()+height);
        float i = (float)Math.atan((double)((g - mouseX) / 40.0F));
        float j = (float)Math.atan((double)((h - mouseY) / 40.0F));
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(j * 20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        float k = models.bodyYaw;
        float l = models.getYaw();
        float m = models.getPitch();
        float n = models.prevHeadYaw;
        float o = models.headYaw;
        models.bodyYaw = 180.0F + i * 20.0F;
        models.setYaw(180.0F + i * 40.0F);
        models.setPitch(-j * 20.0F);
        models.headYaw = models.getYaw();
        models.prevHeadYaw = models.getYaw();
        float p = models.getScale();
        Vector3f vector3f = new Vector3f(0.0F, models.getHeight() / 2.0F + 0.0625F * p, 0.0F);
        float q = (float)30 / p;
        drawEntity(context, g, h, q, vector3f, quaternionf, quaternionf2, models);
        models.bodyYaw = k;
        models.setYaw(l);
        models.setPitch(m);
        models.prevHeadYaw = n;
        models.headYaw = o;
        context.disableScissor();
    }
    public static void drawEntity(DrawContext context, float x, float y, float size, Vector3f vector3f, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity entity) {
        context.getMatrices().push();
        context.getMatrices().translate((double)x, (double)y, 50.0);
        context.getMatrices().scale(size, size, -size);
        context.getMatrices().translate(vector3f.x, vector3f.y, vector3f.z);
        context.getMatrices().multiply(quaternionf);
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        if (quaternionf2 != null) {
            entityRenderDispatcher.setRotation(quaternionf2.conjugate(new Quaternionf()).rotateY(3.1415927F));
        }

        entityRenderDispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.getMatrices(), context.getVertexConsumers(), 15728880);
        });
        context.draw();
        entityRenderDispatcher.setRenderShadows(true);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }
//    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
////        this.xRotation = MathHelper.clamp(this.xRotation - (float)deltaY * 2.5F, -50.0F, 50.0F);
////        this.yRotation += (float)deltaX * 2.5F;
//    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);

        if (Objects.requireNonNull(this.client.getServer()).getOverworld().getEntity(uuid) instanceof MobEntityInterface mobEntity) {
            mobEntity.setControl(true);
            ((MinecraftClientInterface) this.client).setControlling(true, uuid);
            if (this.client.getServer().getOverworld().getEntity(this.client.player.getUuid()) instanceof ServerPlayerEntityInterface serverPlayerEntity) serverPlayerEntity.setControlled((MobEntity) mobEntity);
            screen.close();
        }

    }
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        super.mouseClicked(mouseX, mouseY, button);
//        LOGGER.info("Mouse clicked on " + this.name);
//        return true;
//    }
}
