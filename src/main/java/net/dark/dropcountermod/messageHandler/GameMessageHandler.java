package net.dark.dropcountermod.messageHandler;

import net.dark.dropcountermod.gui.GuiDropCounter;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMessageHandler {
    private static final int statueAndCardSiblingAmount = 4;
    private static final int normalDropSiblingAmount = 3;
    private static final int extraCharsInReceive = 3;
    private static final Map<Text, Integer> dropsToNumber = new HashMap<>();
    private static String receiveMessage;
    private static int lengthOfYouReceive;

    public static void handleMessage(Text message){
        updateReceiveMessage();
        if(!doesContainReceiveMessage(message)) return;
        handleDrop(message);
        GuiDropCounter.handleMessage(message);
    }
    public static void updateReceiveMessage(){
        receiveMessage = I18n.translate("dark.dropcountermod.receiveMessage");
        lengthOfYouReceive = receiveMessage.length();
    }
    private static boolean doesContainReceiveMessage(Text message){
        String messageAsString = message.getString();
        return messageAsString.contains(receiveMessage);
    }
    private static void handleDrop(Text message) {
        List<Text> siblings = message.getSiblings();
        if(siblings.size() == statueAndCardSiblingAmount) {
            handleStatueAndCard(siblings);
        }
        else if (siblings.size() == normalDropSiblingAmount) {
            handleMobAndNormalDrops(siblings);
        }
    }
    private static void handleStatueAndCard(List<Text> siblings){
        Text textWithoutYouReceive = getSubTextWithStyle(siblings.get(2), lengthOfYouReceive + extraCharsInReceive);
        textWithoutYouReceive.getSiblings().add(siblings.get(3));
        incrementDrop(textWithoutYouReceive);
    }
    private static void handleMobAndNormalDrops(List<Text> siblings){
        if (!siblings.get(2).getString().contains(receiveMessage)) {
            incrementDrop(siblings.get(2));
            return;
        }
        Text withoutYouReceive = getSubTextWithStyle(siblings.get(2), lengthOfYouReceive + extraCharsInReceive);
        incrementDrop(withoutYouReceive);
    }
    private static Text getSubTextWithStyle(Text text, int length){
        Style style = text.getStyle();
        MutableText subText = Text.of(text.getString().substring(length)).copy();
        subText.setStyle(style);
        return subText;
    }
    private static void incrementDrop(Text text){
        if (isFirstTimeDrop(text)) {
            dropsToNumber.put(text, 1);
        } else {
            int currentAmount = dropsToNumber.get(text);
            dropsToNumber.put(text, currentAmount + 1);
        }
    }
    private static boolean isFirstTimeDrop(Text dropName){
        return !dropsToNumber.containsKey(dropName);
    }
}
