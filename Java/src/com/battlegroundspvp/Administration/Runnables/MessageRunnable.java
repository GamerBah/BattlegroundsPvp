package com.battlegroundspvp.Administration.Runnables;
/* Created by GamerBah on 12/8/2016 */


import com.battlegroundspvp.Administration.Commands.ChatCommands;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageRunnable implements Runnable {

    private Battlegrounds plugin;
    private int amount = 0;

    public MessageRunnable(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        YamlConfiguration messageConfig = YamlConfiguration.loadConfiguration(file);
        if (amount >= messageConfig.getStringList("Messages").size()) {
            amount = 0;
        }

        if (!ChatCommands.chatSilenced) {
            plugin.getServer().broadcastMessage(" ");
            plugin.getServer().broadcastMessage(ChatColor.RED + "[" + BoldColor.GOLD.getColor() + "*" + ChatColor.RED + "] "
                    + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&',
                    messageConfig.getStringList("Messages").get(amount++)));
            plugin.getServer().broadcastMessage(" ");
        }
    }
}
