package su.harbingers_of_chaos;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.SpectateCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.Entity;

public class Leaderofthezombies implements ModInitializer {
	public static final String MOD_ID = "leader-of-the-zombies";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static byte stages = 0;
	public static byte collected = 0;
	public static String exercise;
	public static String exemple;
	public static int maxZomie = 0;
	@Override
	public void onInitialize() {
		setStages(1);
		LOGGER.info("Hello Fabric world!");
		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			matrixStack.drawText(MinecraftClient.getInstance().textRenderer,"Задача: "+exercise,5,5,16777215,true);
			matrixStack.drawText(MinecraftClient.getInstance().textRenderer,exemple+(stages==1?collected/2:collected),5,15,16777215,true);
		});
		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity.getType() == EntityType.PLAYER && killedEntity.getType() == EntityType.VILLAGER && stages == 2){
				collected++;
				if (collected == 1){
					setStages(0);
				}
			}
		});
	}


	public static void setExercise(byte stages,byte collected) {
		Leaderofthezombies.stages = stages;
		Leaderofthezombies.collected = collected;
	}
	public static void setStages(int value){
		stages++;
		collected = (byte) value;
		if (SharedConstants.isDevelopment) LOGGER.info("Stages:"+String.valueOf(stages));
		if (SharedConstants.isDevelopment) LOGGER.info("Exercise:"+String.valueOf(collected));
		setExercise();

	}
	public static void setExercise(){
		switch(stages){
			case 1:
				exercise = "Съесть 15 кусков сырого мяса";
				exemple = "Съедено: ";
				maxZomie = 0;
				break;
			case 2:
				exercise = "Убить 5 жителей";
				exemple = "Убито: ";
				maxZomie = 0;
				break;
			case 3:
				exercise = "Заразить 10 разумных существ: жители, разбойники и пиглины";
				exemple = "Заражено: ";
				maxZomie = 25;
				break;
			case 4:
				exercise = "Заразить 10 существ не своими зубами";
				exemple = "Заражено: ";
				maxZomie = 25;
				break;
			case 5:
				exercise = "Заполнить лимит по зароженным существам";
				exemple = "Заражено: ";
				maxZomie = 45;
				break;
			case 6:
				exercise = "Заразить вирусом одного из двух боссов";
				exemple = "Заражено: ";
				maxZomie = 45;
				break;
		}
	}
}