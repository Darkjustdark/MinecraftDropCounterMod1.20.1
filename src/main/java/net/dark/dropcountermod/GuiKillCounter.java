package net.dark.dropcountermod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiKillCounter {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean visible;
    public static void toggleVisibility() {
        visible=!visible;
    }
    private static final Map<Text, Integer> dropsToNumber = new HashMap<>();
    public GuiKillCounter(){
        visible = true;
    }

    public static boolean firstTimeDrop(Text dropName){
        return !dropsToNumber.containsKey(dropName);
    }

    private static void incrementInMap(Text text){
        if (firstTimeDrop(text)) {
            dropsToNumber.put(text, 1);
        } else {
            int currentAmount = dropsToNumber.get(text);
            dropsToNumber.put(text, currentAmount + 1);
        }
    }
    private static final int statueAndCardSiblingAmount = 4;
    private static final int normalDropSiblingAmount = 3;

    private static final int lengthOfYouReceive = 14;//TODO: lokalisieren
    public static void handleMessage(Text temp) {
        List<Text> siblings = temp.getSiblings();
        if(siblings.size() == statueAndCardSiblingAmount) {
            Style style = siblings.get(2).getStyle();
            MutableText textWithoutYouReceive = Text.of(siblings.get(2).getString().substring(lengthOfYouReceive)).copy();
            textWithoutYouReceive.setStyle(style);
            textWithoutYouReceive.getSiblings().add(siblings.get(3));
            incrementInMap(textWithoutYouReceive);
        } else if (siblings.size() == normalDropSiblingAmount) {
            if (siblings.get(2).getString().contains("You receive")) { //TODO: lokalisieren
                Style style = siblings.get(2).getStyle();
                MutableText withoutYouReceive = Text.of(siblings.get(2).getString().substring(lengthOfYouReceive)).copy();
                withoutYouReceive.setStyle(style);
                incrementInMap(withoutYouReceive);
                return;
            }
            incrementInMap(siblings.get(2));
        }
    }

    public static void resetAll(){
        dropsToNumber.clear();
    }
    public static void renderKillCounter(DrawContext context, float tickDelta) {
        int xPos = 10;
        int yPos = 10;
        int numberOfDifferentDrops = 1;
        context.drawText(client.textRenderer, Text.literal(I18n.translate("dark.dropcountermod.title")), xPos, yPos, 0x000000, false);
        for (Map.Entry<Text, Integer> entry : dropsToNumber.entrySet()) {
            Text key = entry.getKey();
            Integer value = entry.getValue();
            MutableText temp = key.copy();
            temp.append(Text.of(": " + value));
            context.drawText(client.textRenderer, temp, xPos, yPos + (client.textRenderer.fontHeight * numberOfDifferentDrops), 0x000000, false);
            ++numberOfDifferentDrops;
        }
    }
}
