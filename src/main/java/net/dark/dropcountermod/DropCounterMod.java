package net.dark.dropcountermod;

import net.dark.dropcountermod.command.ResetCounterCommand;
import net.dark.dropcountermod.gui.GuiKillCounter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class DropCounterMod implements ModInitializer {
	@Override
	public void onInitialize() {
		registerHudElements();
		registerCommands();
	}

	private void registerHudElements(){
		HudRenderCallback.EVENT.register(GuiKillCounter::renderKillCounter);
	}

	private void registerCommands(){
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			ResetCounterCommand.register(dispatcher);
		}));
	}
}