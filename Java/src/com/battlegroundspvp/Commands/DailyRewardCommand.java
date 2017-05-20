package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/28/2016 */

import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Listeners.ScoreboardListener;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.connorlinfoot.titleapi.TitleAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;

public class DailyRewardCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public DailyRewardCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 0) {
            plugin.sendIncorrectUsage(player, "/dailyreward");
            return true;
        }

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (playerData.isDailyReward()) {
            player.sendMessage(ChatColor.RED + "You've already claimed your reward! Come back tomorrow for another one!");
            EventSound.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        playerData.setDailyReward(true);
        playerData.setDailyRewardLast(LocalDateTime.now());
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        scoreboardListener.updateScoreboardSouls(player, 50);
        scoreboardListener.updateScoreboardCoins(player, 10);

        TitleAPI.sendTitle(player, 5, 60, 20, BoldColor.GREEN.getColor() + "Daily Reward Claimed!", ChatColor.AQUA + "+50 Souls   " + ChatColor.LIGHT_PURPLE + "+10 Battle Coins");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.2F, 1F);

        return false;
    }

}