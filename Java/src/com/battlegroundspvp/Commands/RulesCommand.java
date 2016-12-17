package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/7/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RulesCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public RulesCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§m---------------§f[ " + BoldColor.RED.getColor() + "SERVER RULES " + ChatColor.WHITE + "]§m---------------");
            player.sendMessage(BoldColor.GOLD.getColor() + " 1. " + ChatColor.YELLOW + "Be respectful to everyone");
            player.sendMessage(BoldColor.GOLD.getColor() + " 2. " + ChatColor.YELLOW + "Play fair (no hacked clients or use of glitches)");
            player.sendMessage(ChatColor.GRAY + "    - If you see someone hacking or using a glitch, report them!");
            player.sendMessage(BoldColor.GOLD.getColor() + " 3. " + ChatColor.YELLOW + "Do not pretend to be a Staff Member");
            player.sendMessage(ChatColor.GRAY + "    - Let the Staff Members do their jobs");
            player.sendMessage(BoldColor.GOLD.getColor() + " 4. " + ChatColor.YELLOW + "No racist, sexual, or other offensive types of remarks");
            player.sendMessage(BoldColor.GOLD.getColor() + " 5. " + ChatColor.YELLOW + "Do not ask about an application you sent in");
            player.sendMessage(BoldColor.GOLD.getColor() + " 6. " + ChatColor.YELLOW + "Targeting is annoying and we strongly discourage it!");
            player.sendMessage(BoldColor.GOLD.getColor() + " 7. " + ChatColor.YELLOW + "Your account = Your responsibility!");
            player.sendMessage(ChatColor.GRAY + "    - If it's a shared account, we will still punish accordingly!");
            player.sendMessage(BoldColor.GOLD.getColor() + " 8. " + ChatColor.YELLOW + "Do not team-kill (leave team, kill teammate)");
            player.sendMessage(BoldColor.GOLD.getColor() + " 9. " + ChatColor.YELLOW + "Ban evading will result in an IP-Ban!");
            player.sendMessage(BoldColor.GOLD.getColor() + " 10. " + ChatColor.YELLOW + "Do not advertise for other servers");
            player.sendMessage("\n" + ChatColor.GRAY + "More info about these rules, as well as more detailed rules,");
            player.sendMessage(ChatColor.GRAY + "can be found on the forums! battlegroundspvp.com");
        }

        return false;
    }
}
