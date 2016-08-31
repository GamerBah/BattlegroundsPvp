package me.gamerbah.Administration.Utils;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.Normalizer;
import java.util.Iterator;
import java.util.regex.Matcher;

public class ChatFilter implements Listener {
    private Battlegrounds plugin;

    public ChatFilter(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.setMessage(censor(event.getMessage()));
    }

    public String censor(String message) {
        String messageToSend = message;
        String message_lower = message.toLowerCase();
        Iterator iter = plugin.getFilteredWords().iterator();
        while (iter.hasNext()) {
            String swear = (String) iter.next();
            if (message_lower.contains(" " + swear + " ") || message_lower.endsWith(" " + swear) || message_lower.startsWith(swear + " ")
                    || message_lower.equals(swear) || message_lower.contains(swear + swear)) {
                messageToSend = messageToSend.replaceAll("(?i)" + swear, Matcher.quoteReplacement(StringUtils.repeat("*", swear.length())));
            }
            if (message_lower.contains(swear + "ing")) {
                messageToSend = messageToSend.replaceAll("(?i)" + swear + "ing", Matcher.quoteReplacement(StringUtils.repeat("*", swear.length() + 3)));
            }
            if (message_lower.contains(swear + "hole") || message_lower.contains(swear + "head")) {
                messageToSend = messageToSend.replaceAll("(?i)" + swear, Matcher.quoteReplacement(StringUtils.repeat("*", swear.length())));
            }
            if (message_lower.contains(swear + "er") || message_lower.contains(swear + "es")) {
                messageToSend = messageToSend.replaceAll("(?i)" + swear, Matcher.quoteReplacement(StringUtils.repeat("*", swear.length())));
            }
            if (message_lower.contains(swear + "s")) {
                messageToSend = messageToSend.replaceAll("(?i)" + swear + "s", Matcher.quoteReplacement(StringUtils.repeat("*", swear.length() + 1)));
            }
        }

        String[] outwords = messageToSend.split(" ");
        for (int i = 0; i < outwords.length; i++) {
            while (iter.hasNext()) {
                String swearWord = " " + iter.next() + " ";

                String testWord = " " + outwords[i].toLowerCase() + " ";
                testWord = Normalizer.normalize(testWord, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                testWord = testWord.replaceAll("[^a-z]", " ").trim();

                if (swearWord.equals(testWord)) {
                    outwords[i] = StringUtils.repeat("*", swearWord.length());
                }
            }
        }

        messageToSend = StringUtils.join(outwords, " ");
        return messageToSend;
    }

}
