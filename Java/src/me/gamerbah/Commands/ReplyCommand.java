package me.gamerbah.Commands;

import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public ReplyCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        // TODO: Add Mute Check

        if (!plugin.getMessagers().containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You have not messaged anyone!");
            return true;
        }

        Player target = plugin.getServer().getPlayer(plugin.getMessagers().get(player.getUniqueId()));

        if (target == null) {
            player.sendMessage(ChatColor.RED + "The player that you previously messaged is no longer online.");
            return true;
        }

        if (!plugin.getPlayerData(target.getUniqueId()).isPrivateMessaging()) {
            player.sendMessage(ChatColor.RED + "That player isn't accepting private messages anymore!");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
            return true;
        }

        plugin.getMessagers().put(player.getUniqueId(), target.getUniqueId());
        plugin.getMessagers().put(target.getUniqueId(), player.getUniqueId());

        String message = StringUtils.join(args, ' ', 0, args.length);

        if (Battlegrounds.getAfk().contains(target.getUniqueId())) {
            player.sendMessage(ChatColor.AQUA + target.getName() + " is AFK, so they might not see your message");
        }

        player.sendMessage(ChatColor.DARK_AQUA + "You" + ChatColor.RED + " \u00BB " + BoldColor.AQUA.getColor() + target.getName() + ChatColor.WHITE + ": " + ChatColor.AQUA + message.trim());
        target.sendMessage(BoldColor.AQUA.getColor() + player.getName() + ChatColor.RED + " \u00BB " + ChatColor.DARK_AQUA + "You" + ChatColor.WHITE + ": " + ChatColor.AQUA + message);

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 2, 2);
        target.playSound(target.getLocation(), Sound.BLOCK_NOTE_HARP, 2, 2);

        return true;
    }
}
