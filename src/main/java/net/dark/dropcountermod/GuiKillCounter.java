package net.dark.dropcountermod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class GuiKillCounter {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean visible;
    public static void toggleVisibility() {
        visible=!visible;
    }
    private static final Map<String, Integer> dropsToNumber = new HashMap<>();
    public GuiKillCounter(){
        visible = true;
    }

    public static boolean firstTimeDrop(String dropName){
        return !dropsToNumber.containsKey(dropName);
    }

    private static final int serverNameLength = 9;
    public static void handleMessage(String message, int receiveMessageLength) {
        String drop = message.substring(serverNameLength + receiveMessageLength + 1);
        if(firstTimeDrop(drop)) {
            dropsToNumber.put(drop, 1);
        }
        else {
            int currentAmount = dropsToNumber.get(drop);
            dropsToNumber.put(drop, currentAmount + 1);
        }
    }

    public static void resetAll(){
        dropsToNumber.clear();
    }
    public static void renderKillCounter(DrawContext context, float tickDelta) {
        int xPos = 10;
        int yPos = 10;
        int counter = 1;
        context.drawText(client.textRenderer, Text.literal(I18n.translate("dark.dropcountermod.title")), xPos, yPos, 0x000000, false);
        for (Map.Entry<String, Integer> entry : dropsToNumber.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            String tempPrintText = key + ": " + value;
            context.drawText(client.textRenderer, tempPrintText, xPos, yPos + (client.textRenderer.fontHeight * counter), 0x000000, false);
            ++counter;
        }
    }
}
