package net.dark.dropcountermod.mixin;

import net.dark.dropcountermod.GuiKillCounter;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
public class GameChatMixin {

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onGameMessage(Text message, boolean overlay, CallbackInfo ci) {
        Text tempReceiveMessage = Text.literal(I18n.translate("dark.dropcountermod.receiveMessage"));
        String stringToCheckContains = message.getString();
        if(stringToCheckContains.contains(tempReceiveMessage.getString())) {
            GuiKillCounter.handleMessage(message);
        }
    }
}
