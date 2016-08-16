package me.gamerbah.Administration.Utils;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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

        String[] message = event.getMessage().split(" ");
        String finalMessage = "";

        for (String word : message) {
            for (String filteredWord : plugin.getFilteredWords()) {
                if (word.toLowerCase().contains(filteredWord.toLowerCase().trim())) {
                    word = StringUtils.repeat("*", word.length());
                }
            }

            finalMessage += word + " ";
        }

        event.setMessage(finalMessage.trim());
    }

}
