package me.gamerbah.Commands;
/* Created by GamerBah on 8/19/2016 */

import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Listeners.ScoreboardListener;
import me.gamerbah.Utils.EventSound;
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
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (args.length != 1) {
            player.sendMessage(plugin.incorrectUsage + "/thanks <player>");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        OfflinePlayer target = plugin.getServer().getOfflinePlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player doesn't exist!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());

        if (!plugin.getConfig().getString("essenceOwner").equals(target.getName())) {
            player.sendMessage(ChatColor.RED + "You can only thank a player that has a Battle Essence active!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        if (plugin.getConfig().getString("essenceOwner").equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "You can't thank yourself!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        List<String> thanks = plugin.getConfig().getStringList("essenceThanks");
        if (thanks.contains(player.getName())) {
            player.sendMessage(ChatColor.RED + "You already thanked this player for their Battle Essence!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        thanks.add(player.getName());
        plugin.getConfig().set("essenceThanks", thanks);
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        scoreboardListener.getSouls().put(player.getUniqueId(), playerData.getSouls());
        playerData.setSouls(playerData.getSouls() + 20);
        scoreboardListener.updateScoreboardSouls(player);
        player.sendMessage(ChatColor.GRAY + "You thanked " + target.getName() + " for the Essence! " + ChatColor.AQUA + "[+20 Souls]");

        if (!player.isOnline()) {
            targetData.setSouls(targetData.getSouls() + 20);
        } else {
            Player t = plugin.getServer().getPlayer(target.getUniqueId());
            scoreboardListener.getSouls().put(t.getUniqueId(), targetData.getSouls());
            targetData.setSouls(targetData.getSouls() + 20);
            scoreboardListener.updateScoreboardSouls(t);
            t.sendMessage(ChatColor.GRAY + player.getName() + " thanked you for your Essence! " + ChatColor.AQUA + "[+20 Souls]");
        }

        return false;
    }

}