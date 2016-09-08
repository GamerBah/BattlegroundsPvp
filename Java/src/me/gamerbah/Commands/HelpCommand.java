package me.gamerbah.Commands;
/* Created by GamerBah on 8/31/2016 */

import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public HelpCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (args.length > 1) {
            player.sendMessage(Battlegrounds.incorrectUsage + "/help" + (playerData.hasRank(Rank.HELPER) ? " [\"staff\"]" : ""));
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§m------------------§f[ " + ChatColor.RED + "Command Help " + ChatColor.WHITE + "]§m------------------");
            if (playerData.hasRank(Rank.HELPER)) {
                player.sendMessage(ChatColor.GOLD + " You can use §e/help staff §6for Staff-related commands");
            }
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /afk " + ChatColor.GRAY + "- Sets you as away-from-keyboard");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /dailyreward " + ChatColor.GRAY + "- Claims an available Daily Reward");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /essences " + ChatColor.GRAY + "- Shows you your purchased Battle Essences");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /message " + ChatColor.GRAY + "- Sends a private message to a player");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /options " + ChatColor.GRAY + "- Shows options for a specified player");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /ping " + ChatColor.GRAY + "- Shows you your connection to Battlegrounds");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /refer " + ChatColor.GRAY + "- Refer the player who invited you to Battlegrounds!");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /reply " + ChatColor.GRAY + "- Replies to the most recent private message");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /report " + ChatColor.GRAY + "- Reports a player to Staff members");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /spawn " + ChatColor.GRAY + "- Teleports you back to the spawn");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /slots " + ChatColor.GRAY + "- Opens the \"K-Slots\" Machine");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /staffreq " + ChatColor.GRAY + "- Sends a message to offline Staff members");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /team " + ChatColor.GRAY + "- Team up with another player");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /thanks " + ChatColor.GRAY + "- Thanks the player with the active Battle Essence");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("staff")) {
            if (!playerData.hasRank(Rank.HELPER)) {
                plugin.sendNoPermission(player);
                return true;
            }
            player.sendMessage("§m---------------§f[ " + ChatColor.RED + "Staff Command Help " + ChatColor.WHITE + "]§m---------------");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /cmdspy " + ChatColor.GRAY + "- Allows you to see all executed commands");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /mute " + ChatColor.GRAY + "- Mutes a player");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /kick " + ChatColor.GRAY + "- Kicks a player from the server");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /punish " + ChatColor.GRAY + "- Views a player's punishment history");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /staff " + ChatColor.GRAY + "- Sends a message to the Staff chat channel");
            player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /unmute " + ChatColor.GRAY + "- unmutes a player");
            if (playerData.hasRank(Rank.MODERATOR)) {
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /clearchat " + ChatColor.GRAY + "- Clears the chat");
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /flyspeed " + ChatColor.GRAY + "- Changes your flying speed");
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /freeze " + ChatColor.GRAY + "- Freezes players");
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /lockchat " + ChatColor.GRAY + "- Locks the chat");
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /skull " + ChatColor.GRAY + "- Gets a players head");
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /temp-ban " + ChatColor.GRAY + "- Bans a player temporarily");
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /unban " + ChatColor.GRAY + "- Unbans a player");
            }
            if (playerData.hasRank(Rank.ADMIN)) {
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /ban " + ChatColor.GRAY + "- Permanently bans a player");
            }
            if (playerData.hasRank(Rank.OWNER)) {
                player.sendMessage(BoldColor.DARK_AQUA.getColor() + " /maintenance " + ChatColor.GRAY + "- Puts the server into Maintenance Mode");
            }
            return true;
        }

        return false;
    }

}