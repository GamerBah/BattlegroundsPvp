package me.gamerbah.Commands;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.EventSound;
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
                plugin.playSound(player, EventSound.COMMAND_FAIL);
                return true;
            }
        } else if (args.length > 2) {
            player.sendMessage(plugin.incorrectUsage + "/ping [player]");
            plugin.playSound(player, EventSound.COMMAND_FAIL);
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
        if (ping > 20 && ping <= 50) {
            status = BoldColor.PURPLE.getColor() + "GREAT! ";
        }
        if (ping > 50 && ping <= 100) {
            status = BoldColor.GREEN.getColor() + "Good! ";
        }
        if (ping > 100 && ping <= 150) {
            status = BoldColor.DARK_GREEN.getColor() + "Okay. ";
        }
        if (ping > 150 && ping <= 250) {
            status = BoldColor.YELLOW.getColor() + "Eh... ";
        }
        if (ping > 250 && ping <= 350) {
            status = BoldColor.GOLD.getColor() + "Bad. ";
        }
        if (ping > 350 && ping <= 450) {
            status = BoldColor.RED.getColor() + "AWFUL! ";
        }
        if (ping > 450) {
            status = BoldColor.DARK_RED.getColor() + "NON-EXISTENT! ";
        }

        if (player == target) {
            player.sendMessage(ChatColor.GRAY + "Your connection to Battlegrounds is " + status + ChatColor.GRAY + "(" + ping + "ms)");
        } else {
            player.sendMessage(ChatColor.RED + target.getName() + "'s connection to Battlegrounds is " + status + ChatColor.GRAY + "(" + ping + "ms)");
        }

        return true;
    }

}
