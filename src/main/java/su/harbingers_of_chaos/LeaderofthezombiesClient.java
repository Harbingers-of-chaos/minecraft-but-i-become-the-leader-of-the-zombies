package su.harbingers_of_chaos;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.hud.Hud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import su.harbingers_of_chaos.screen.ScreenTest;

import static su.harbingers_of_chaos.Leaderofthezombies.LOGGER;
import static su.harbingers_of_chaos.Leaderofthezombies.MOD_ID;

public class LeaderofthezombiesClient implements ClientModInitializer {

	private static final KeyBinding CONTROL = new KeyBinding("key.owo-ui-academy.begin", GLFW.GLFW_KEY_G, "key.categories.misc");
	private static final KeyBinding INFECTIONS = new KeyBinding("key.owo-ui-academy.begin", GLFW.GLFW_KEY_R, "key.categories.misc");

	@Override
	public void onInitializeClient() {
		KeyBindingHelper.registerKeyBinding(CONTROL);
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (CONTROL.wasPressed()) {
				client.setScreen(new ScreenTest());
			}
			while (INFECTIONS.wasPressed()){

				client.setScreen(new ScreenTest());
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