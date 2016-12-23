package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/19/2016 */

import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Listeners.ScoreboardListener;
import com.battlegroundspvp.Utils.Enums.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ThanksCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public ThanksCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            plugin.sendIncorrectUsage(player, "/thanks <player>");
            return true;
        }

        OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player doesn't exist!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());

        if (!plugin.getConfig().getString("essenceOwner").equals(target.getName())) {
            player.sendMessage(ChatColor.RED + "You can only thank a player that has a Battle Essence active!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (plugin.getConfig().getString("essenceOwner").equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "You can't thank yourself!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        List<String> thanks = plugin.getConfig().getStringList("essenceThanks");
        if (thanks.contains(player.getName())) {
            player.sendMessage(ChatColor.RED + "You already thanked this player for their Battle Essence!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        thanks.add(player.getName());
        plugin.getConfig().set("essenceThanks", thanks);
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        scoreboardListener.updateScoreboardSouls(player, 50);
        player.sendMessage(ChatColor.GRAY + "You thanked " + target.getName() + " for the Essence! " + ChatColor.AQUA + "[+50 Souls]");

        if (!target.isOnline()) {
            targetData.setSouls(targetData.getSouls() + 50);
        } else {
            Player t = plugin.getServer().getPlayer(target.getUniqueId());
            scoreboardListener.updateScoreboardSouls(t, 50);
            t.sendMessage(ChatColor.GRAY + player.getName() + " thanked you for your Essence! " + ChatColor.AQUA + "[+50 Souls]");
        }

        return false;
    }

}