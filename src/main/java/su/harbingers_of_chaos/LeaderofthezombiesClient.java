package su.harbingers_of_chaos;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import org.lwjgl.glfw.GLFW;
import su.harbingers_of_chaos.interfaces.MinecraftClientInterface;
import su.harbingers_of_chaos.interfaces.MobEntityInterface;
import su.harbingers_of_chaos.screen.TestScreen;

import static su.harbingers_of_chaos.Leaderofthezombies.*;
import static su.harbingers_of_chaos.util.ModTags.INTELLIGENT_BEINGS;

public class LeaderofthezombiesClient implements ClientModInitializer {
	public static final MinecraftClient MC = MinecraftClient.getInstance();
	private static final KeyBinding CONTROL = new KeyBinding("key.owo-ui-academy.begin", GLFW.GLFW_KEY_G, "key.categories.misc");
	private static final KeyBinding INFECTIONS = new KeyBinding("key.owo-ui-academy.saa", GLFW.GLFW_KEY_R, "key.categories.misc");

	@Override
	public void onInitializeClient() {
		if (MC.player != null&& MC.player.input != null)LOGGER.info(MC.player.input.toString());
		KeyBindingHelper.registerKeyBinding(CONTROL);
		KeyBindingHelper.registerKeyBinding(INFECTIONS);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (CONTROL.wasPressed()) {
				client.setScreen(new TestScreen());
			}
			while (INFECTIONS.wasPressed()){
					Entity entity = client.targetedEntity;
					if (entity != null && entity instanceof MobEntity && entity instanceof MobEntityInterface && !((MobEntityInterface) entity).isZombe()
//					){
							&& (stages == 3 ? entity.getType().isIn(INTELLIGENT_BEINGS) : true) && ((MinecraftClientInterface)client).getZombesUUID().size()<maxZomie) {
						((MobEntityInterface) entity).Infections(client);

					}
			}
		});

//		Hud.add(Identifier.of(MOD_ID, "hint"), () ->
//				Containers.verticalFlow(Sizing.content(), Sizing.content())
//						.child(Components.label(
//								Text.empty()
//										.append(Text.literal("! ").formatted(Formatting.YELLOW, Formatting.BOLD))
//										.append(" Press ")
//										.append(KeyBindingHelper.getBoundKeyOf(BEGIN).getLocalizedText().copy().formatted(Formatting.BLUE))
//										.append(" to\nbegin owo-ui Academy")
//						).horizontalTextAlignment(HorizontalAlignment.CENTER).shadow(true))
//						.surface(Surface.flat(0x77000000).and(Surface.outline(0xFF121212)))
//						.padding(Insets.of(5))
//						.positioning(Positioning.relative(100, 35))
//		);
	}
}