package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public PingCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        String nmsVersion = plugin.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);

        Player target = player;

        if (args.length == 1) {
            target = plugin.getServer().getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "That player isn't online!");
                EventSound.playSound(player, EventSound.ACTION_FAIL);
                return true;
            }
        } else if (args.length > 2) {
            plugin.sendIncorrectUsage(player, "/ping [player]");
            return true;
        }

        int ping = -1;

        try {
            Object nmsPlayer = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer").cast(player).getClass().getMethod("getHandle").invoke(target);
            ping = nmsPlayer.getClass().getField("ping").getInt(nmsPlayer);
        } catch (Exception e) {
            plugin.getLogger().severe("Uh oh! Unable to retrieve the player's ping!");
            e.printStackTrace();
        }

        String status = BoldColor.RED + "TERRIBLE! ";
        if (ping <= 20) {
            status = BoldColor.PINK.getColor() + "AWESOME! ";
        }
        if (ping > 20 && ping <= 40) {
            status = BoldColor.PURPLE.getColor() + "GREAT! ";
        }
        if (ping > 40 && ping <= 70) {
            status = BoldColor.GREEN.getColor() + "Good! ";
        }
        if (ping > 70 && ping <= 120) {
            status = BoldColor.DARK_GREEN.getColor() + "Okay. ";
        }
        if (ping > 120 && ping <= 150) {
            status = BoldColor.YELLOW.getColor() + "Eh... ";
        }
        if (ping > 150 && ping <= 200) {
            status = BoldColor.GOLD.getColor() + "Bad. ";
        }
        if (ping > 200 && ping <= 250) {
            status = BoldColor.RED.getColor() + "AWFUL! ";
        }
        if (ping > 250) {
            status = BoldColor.DARK_RED.getColor() + "RIP. ";
        }

        if (player == target) {
            player.sendMessage(ChatColor.GRAY + "Your connection to Battlegrounds is " + status + ChatColor.GRAY + "(" + ping + "ms)");
        } else {
            player.sendMessage(ChatColor.RED + target.getName() + ChatColor.GRAY + "'s connection to Battlegrounds is " + status + ChatColor.GRAY + "(" + ping + "ms)");
        }

        return true;
    }

}
