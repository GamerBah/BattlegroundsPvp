package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 9/7/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Runnables.AutoUpdate;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Menus.KSlotsMenu;
import me.gamerbah.Listeners.ScoreboardListener;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ReloadCommand implements CommandExecutor {
    private Battlegrounds plugin;

    public ReloadCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!playerData.hasRank(Rank.OWNER)) {
            plugin.sendNoPermission(player);
        } else {
            plugin.getServer().broadcastMessage(BoldColor.RED.getColor() + "SERVER: " + ChatColor.GRAY + "Reloading in 5 seconds. Hang in there!");
            plugin.getServer().broadcastMessage(ChatColor.GRAY + "(You'll get sent back to the spawn once the reload is complete)");
            AutoUpdate.updating = true;
            ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
            for (Player players : plugin.getServer().getOnlinePlayers()) {
                players.closeInventory();
                if (KSlotsMenu.usingSlots.containsKey(players)) {
                    PlayerData playersData = plugin.getPlayerData(players.getUniqueId());
                    scoreboardListener.getSouls().put(players.getUniqueId(), playersData.getSouls());
                    playersData.setSouls(playersData.getSouls() + KSlotsMenu.usingSlots.get(players) * 400);
                    scoreboardListener.updateScoreboardSouls(players);
                    players.sendMessage(" ");
                    players.sendMessage(ChatColor.YELLOW + "To prevent data loss, your slots roll has been cancelled,");
                    players.sendMessage(ChatColor.YELLOW + "and you've been refunded " + BoldColor.AQUA.getColor() + KSlotsMenu.usingSlots.get(players) * 400 + " Souls");
                }
                FreezeCommand.reloadFreeze = true;
                players.setWalkSpeed(0F);
                players.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -50, true, false));
                players.setFoodLevel(6);
                players.setSaturation(0);
                players.sendMessage(" ");
                players.sendMessage(ChatColor.YELLOW + "To prevent data corruption, you've been frozen.");
                players.sendMessage(ChatColor.YELLOW + "You'll be unfrozen once the reload is complete\n ");
            }
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getServer().reload();
                for (Player players : plugin.getServer().getOnlinePlayers()) {
                    Battlegrounds.playSound(players, EventSound.ACTION_SUCCESS);
                    players.setWalkSpeed(0.2F);
                }
                plugin.getServer().broadcastMessage(BoldColor.RED.getColor() + "SERVER: " + ChatColor.GRAY
                        + "Server Reload was a " + BoldColor.GREEN.getColor() + "success" + ChatColor.GRAY + "! Now go have fun!");
                AutoUpdate.updating = false;
            }, 100);
        }
        return false;
    }
}
