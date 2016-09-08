package me.gamerbah.Commands;
/* Created by GamerBah on 9/7/2016 */

import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReferCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public ReferCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(Battlegrounds.incorrectUsage + "/refer <username>");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(Battlegrounds.incorrectUsage + "/refer <username>");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (playerData.getRecruitedBy() != -1) {
            player.sendMessage(ChatColor.RED + "You've already referred someone!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (args[0].equals(player.getName())) {
            player.sendMessage(ChatColor.RED + "You can't refer yourself!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (plugin.getPlayerData(args[0]) == null) {
            player.sendMessage(ChatColor.RED + "That player doesn't exist!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        PlayerData recruitData = plugin.getPlayerData(args[0]);
        recruitData.setPlayersRecruited(recruitData.getPlayersRecruited() + 1);
        Player recruiter = plugin.getServer().getPlayer(args[0]);
        if (recruiter != null) {
            recruiter.sendMessage(ChatColor.GREEN + "Successfully recruited " + player.getName() + "! " + ChatColor.AQUA + "[+75 Souls]" + ChatColor.LIGHT_PURPLE + "[+50 Battle Coins]");
            Battlegrounds.playSound(recruiter, EventSound.ACTION_SUCCESS);
        }
        playerData.setRecruitedBy(recruitData.getId());
        player.sendMessage(ChatColor.GREEN + "Successfully referred " + recruitData.getName() + "! " + ChatColor.AQUA + "[+25 Souls]" + ChatColor.LIGHT_PURPLE + "[+10 Battle Coins]");
        Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);

        return false;
    }

}