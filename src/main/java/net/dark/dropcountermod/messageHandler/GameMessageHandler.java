package net.dark.dropcountermod.messageHandler;

import net.dark.dropcountermod.gui.GuiDropCounter;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

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
    private static final int maxDrops = 7;

    //region Handle message
    public static void handleMessage(Text message){
        updateReceiveMessage();
        if(!isMessageARelevantDrop(message)) return;
        //GuiDropCounter.updateDrawData(dropsToNumber);
        handleDrop(message);
        GuiDropCounter.updateDrawData(dropsToNumber);
    }
    private static boolean isMessageARelevantDrop(Text message) {
        String messageAsString = message.getString();
        //check if the message contains the receive-message
        if(!messageAsString.contains(receiveMessage)) return false;
        //check if the drop is a blueprint
        if(messageAsString.contains(I18n.translate("dark.dropcountermod.blueprint"))) return false;
        return true;
    }
    private static void handleDrop(Text message) {
        List<Text> siblings = message.getSiblings();
        if(siblings.size() == statueAndCardSiblingAmount) {
            handleStatueAndCard(siblings);
        }
        else if (siblings.size() == normalDropSiblingAmount) {
            handleNormalDrops(siblings);
        }
    }
    //endregion

    //region Handle Statues and Cards
    private static void handleStatueAndCard(List<Text> siblings){
        //get the full drop Name with the style
        Text dropName = getSubTextWithStyle(siblings.get(2), lengthOfYouReceive + extraCharsInReceive);
        dropName.getSiblings().add(siblings.get(3));
        if (!doesMapContainDrop(dropName)){
            //remove similar, if it is not present in the current drops
            removeOldStatueOrCard(dropName.toString());
        }
        incrementDrop(dropName);
    }
    private static void removeOldStatueOrCard(String dropAsString){
        //Checks if the Drop is a Card or a Statue, and removes old card/statues if present
        check_string_in_map(dropAsString, "Card");
        check_string_in_map(dropAsString, "Statue");
    }
    private static void check_string_in_map(String drop, String typeName){
        if(drop.contains(typeName)){
            //checks if the drop is equal to the string and if a matching string is in the drops
            if(doesMapContainString(typeName)){
                //removes the drop from the same type
                removeStringFromMap(typeName);
            }
            //Clear in case that card is not in drops, and it is maxDrops or greater, because the drop will be appended
            else if(dropsToNumber.keySet().size() >= maxDrops){
                dropsToNumber.clear();
            }
        }
    }
    private static boolean doesMapContainString(String string){
        //returns if the keys of the drops map contain a given key
        for (Text drop : dropsToNumber.keySet()) {
            String dropAsString = drop.toString();
            if(dropAsString.contains(string)) return true;
        }
        return false;
    }
    private static void removeStringFromMap(String string){
        //removes the key value pair from the map, which matches the string
        for (Text drop : dropsToNumber.keySet()) {
            String dropAsString = drop.toString();
            if (dropAsString.contains(string))
            {
                dropsToNumber.remove(drop);
                return;
            }
        }
    }
    //endregion

    //region Handle normal drops
    private static void handleNormalDrops(List<Text> siblings){
        Text drop = siblings.get(2);
        Text dropName = getDropNameWithoutYouReceive(drop);
        if (doesMapContainDrop(dropName) || dropsToNumber.keySet().size() < maxDrops)
        {
            incrementDrop(dropName);
        }
        else{
            //TODO: instead of clearing it, it should replace the drop with the same rarity(color), if present
            dropsToNumber.clear();
            incrementDrop(dropName);
        }
    }
    private static Text getDropNameWithoutYouReceive(Text drop){
        //returns the dropName without the receive-message part
        if (!drop.getString().contains(receiveMessage)) return drop;
        return getSubTextWithStyle(drop, lengthOfYouReceive + extraCharsInReceive);
    }
    private static void removeSimilarColor(Text drop){
        if(dropsToNumber.containsKey(drop)) return;
        if(doesMapContainColor(drop.getStyle().getColor())){
            removeColorFromMap(drop.getStyle().getColor());
        }
    }
    private static boolean doesMapContainColor(TextColor color){
        for (Text drop : dropsToNumber.keySet()) {
            if(drop.getString().contains("Card") || drop.getString().contains("Statue")) continue;
            TextColor keyColor = drop.getStyle().getColor();
            if (keyColor == color) return true;
        }
        return false;
    }
    private static void removeColorFromMap(TextColor color){
        for (Text drop : dropsToNumber.keySet()) {
            if(drop.getString().contains("Card") || drop.getString().contains("Statue")) continue;
            TextColor keyColor = drop.getStyle().getColor();
            if (keyColor == color){
                dropsToNumber.remove(drop, dropsToNumber.get(drop)) ;
            }
        }
    }
    //endregion

    //region Helper
    public static void updateReceiveMessage(){
        receiveMessage = I18n.translate("dark.dropcountermod.receiveMessage");
        lengthOfYouReceive = receiveMessage.length();
    }
    private static Text getSubTextWithStyle(Text text, int length){
        Style style = text.getStyle();
        MutableText subText = Text.of(text.getString().substring(length)).copy();
        subText.setStyle(style);
        return subText;
    }
    private static void incrementDrop(Text text){
        if (!doesMapContainDrop(text)) {
            dropsToNumber.put(text, 1);
        } else {
            int currentAmount = dropsToNumber.get(text);
            dropsToNumber.put(text, currentAmount + 1);
        }
    }
    private static boolean doesMapContainDrop(Text dropName){
        return dropsToNumber.containsKey(dropName);
    }
    public static void resetAllCounters(){
        dropsToNumber.clear();
    }
    //endregion
}
