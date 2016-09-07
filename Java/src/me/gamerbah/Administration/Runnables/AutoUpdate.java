package me.gamerbah.Administration.Runnables;

import me.gamerbah.Administration.Commands.FreezeCommand;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.PluginUtil;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Etc.Menus.KSlotsMenu;
import me.gamerbah.Listeners.ScoreboardListener;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AutoUpdate implements Runnable {
    public static boolean updating = false;
    private Battlegrounds plugin;
    private File updateFile;

    public AutoUpdate(Battlegrounds plugin) {
        this.plugin = plugin;

        if (!plugin.getServer().getUpdateFolderFile().exists()) {
            plugin.getServer().getUpdateFolderFile();
        }

        updateFile = new File(plugin.getServer().getUpdateFolderFile().getPath() + File.separator + "Battlegrounds.jar");
    }

    @Override
    public void run() {
        if (updateFile == null || !updateFile.exists()) {
            return;
        }
        plugin.getServer().broadcastMessage(BoldColor.RED.getColor() + "SERVER: " + ChatColor.GRAY + "Reloading in 5 seconds for an update. Hang in there!");
        plugin.getServer().broadcastMessage(ChatColor.GRAY + "(You'll get sent back to the spawn once the update is complete)");
        updating = true;
        ScoreboardListener scoreboardListener = new ScoreboardListener(plugin);
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.closeInventory();
            if (KSlotsMenu.usingSlots.containsKey(player)) {
                PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
                scoreboardListener.getSouls().put(player.getUniqueId(), playerData.getSouls());
                playerData.setSouls(playerData.getSouls() + KSlotsMenu.usingSlots.get(player) * 400);
                scoreboardListener.updateScoreboardSouls(player);
                player.sendMessage(" ");
                player.sendMessage(ChatColor.YELLOW + "To prevent data loss, your slots roll has been cancelled,");
                player.sendMessage(ChatColor.YELLOW + "and you've been refunded " + BoldColor.AQUA.getColor() + KSlotsMenu.usingSlots.get(player) * 400 + " Souls");
            }
            FreezeCommand.reloadFreeze = true;
            player.setWalkSpeed(0F);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -50, true, false));
            player.setFoodLevel(6);
            player.setSaturation(0);
            player.sendMessage(" ");
            player.sendMessage(ChatColor.YELLOW + "To prevent data corruption, you've been frozen.");
            player.sendMessage(ChatColor.YELLOW + "You'll be unfrozen once the update is complete\n ");
        }
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            PluginUtil.unload(plugin);
            try {
                Files.move(Paths.get(updateFile.getPath()), Paths.get(plugin.getServer().getUpdateFolderFile().getParentFile().getPath()
                        + File.separator + "Battlegrounds.jar"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                plugin.getLogger().severe("Unable to complete update!");
            }

            PluginUtil.load("Battlegrounds");

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                Battlegrounds.playSound(player, EventSound.ACTION_SUCCESS);
                player.setWalkSpeed(0.2F);
            }
            plugin.getServer().broadcastMessage(BoldColor.RED.getColor() + "SERVER: " + ChatColor.GRAY
                    + "Update was a " + BoldColor.GREEN.getColor() + "success" + ChatColor.GRAY + "! Now go have fun!");
            updating = false;
        }, 100);
    }
}
