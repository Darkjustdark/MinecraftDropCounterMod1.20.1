package net.dark.dropcountermod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import java.util.List;

public class DropCounterMod implements ModInitializer {
	@Override
	public void onInitialize() {
		HudRenderCallback.EVENT.register(GuiKillCounter::renderKillCounter);
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			ResetCounterCommand.register(dispatcher);
		}));
	}
}