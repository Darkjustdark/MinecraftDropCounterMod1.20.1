package net.dark.dropcountermod.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class GuiDropCounter {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Map<Text, Integer> dropsToNumber = new HashMap<>();
    public GuiDropCounter(){

    }
    public static void updateDrawData(Map<Text, Integer> drawData){
        dropsToNumber.clear();
        dropsToNumber.putAll(drawData);
    }
    public static void renderKillCounter(DrawContext context, float tickDelta) {
        int xPos = 10;
        int yPos = 10;
        int numberOfDifferentDrops = 1;
        context.drawText(client.textRenderer, Text.literal(I18n.translate("dark.dropcountermod.title")), xPos, yPos, 0x000000, false);
        for (Map.Entry<Text, Integer> entry : dropsToNumber.entrySet()) {
            Text key = entry.getKey();
            Integer value = entry.getValue();
            MutableText printText = key.copy();
            printText.append(Text.of(": " + value));
            context.drawText(client.textRenderer, printText, xPos, yPos + (client.textRenderer.fontHeight * numberOfDifferentDrops), 0x000000, false);
            ++numberOfDifferentDrops;
        }
    }
}
