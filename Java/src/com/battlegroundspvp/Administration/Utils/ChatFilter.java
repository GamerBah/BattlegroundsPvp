package com.battlegroundspvp.Administration.Utils;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.Time;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.Normalizer;
import java.util.ArrayList;
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

        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (plugin.getPlayerPunishments().containsKey(player.getUniqueId())) {
            ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(player.getUniqueId());
            for (int i = 0; i < punishments.size(); i++) {
                Punishment punishment = punishments.get(i);
                if (!punishment.isPardoned()) {
                    event.setCancelled(true);
                    BaseComponent baseComponent = new TextComponent(ChatColor.RED + "You are muted! " + ChatColor.GRAY + "(Hover to view details)");
                    baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Muted by: "
                            + ChatColor.WHITE + plugin.getServer().getPlayer(punishment.getEnforcer()).getName() + "\n" + ChatColor.GRAY + "Reason: "
                            + ChatColor.WHITE + punishment.getReason().getName() + "\n" + ChatColor.GRAY + "Time Remaining: " + ChatColor.WHITE +
                            Time.toString(Time.punishmentTimeRemaining(punishment.getExpiration()), true)).create()));
                    player.spigot().sendMessage(baseComponent);
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                    return;
                }
            }
        }

        event.setMessage(censor(event.getMessage()));
    }

    public String censor(String message) {
        String messageToSend = message;
        String message_lower = message.toLowerCase();
        Iterator iter = plugin.getFilteredWords().iterator();
        while (iter.hasNext()) {
            String swear = (String) iter.next();
            if (message_lower.contains(swear.toLowerCase())) {
                if (message_lower.contains(" ass") || message_lower.contains("ass ") || message_lower.contains(" ass ")) {
                    messageToSend = messageToSend.replaceAll("(?i)" + swear, Matcher.quoteReplacement(StringUtils.repeat("*", swear.length())));
                }
                messageToSend = messageToSend.replaceAll("(?i)" + swear, Matcher.quoteReplacement(StringUtils.repeat("*", swear.length())));
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
