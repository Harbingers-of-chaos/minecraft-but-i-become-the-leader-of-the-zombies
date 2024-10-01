package su.harbingers_of_chaos.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CommandBlockScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import su.harbingers_of_chaos.interfaces.MinecraftClientInterface;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static su.harbingers_of_chaos.Leaderofthezombies.LOGGER;
import static su.harbingers_of_chaos.Leaderofthezombies.MOD_ID;

@Environment(EnvType.CLIENT)
public class TestScreen extends Screen {
    protected int backgroundWidth = 176;
    protected int backgroundHeight = 166;
    public static final Identifier BACKGROUND_TEXTURE = Identifier.of(MOD_ID,"textures/gui/container/inventory.png");

    public TestScreen() {
        super(Text.of("test"));
    }
    @Override
    protected void init() {
        super.init();
//        this.addDrawableChild(widget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawBackground(context,delta,mouseX,mouseY);
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(BACKGROUND_TEXTURE, 67, 67, 0, 0, this.backgroundWidth, this.backgroundHeight);
        final int[] x = {75, 75};
        ((MinecraftClientInterface) this.client).getZombesUUID().forEach((key, value) -> {
            if (this.client.getServer().getOverworld().getEntity(value) instanceof MobEntity mobEntity) {
                if (x[0] == 915) {x[0] = 75;x[1] += 82;}
                this.addDrawableChild(new EntityWidget(x[0], x[1], 50, 70, mobEntity, key, client, value));
                x[0] += 70;
            }
        });
    }
}