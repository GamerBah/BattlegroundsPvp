package me.gamerbah.Administration.Utils;

import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AutoUpdate implements Runnable {
    private Battlegrounds plugin;
    private File updateFile;

    public AutoUpdate(Battlegrounds plugin) {
        this.plugin = plugin;

        if (!plugin.getServer().getUpdateFolderFile().exists()) {
            plugin.getServer().getUpdateFolderFile().mkdir();
        }

        updateFile = new File(plugin.getServer().getUpdateFolderFile().getPath() + File.separator + "Battlegrounds.jar");
    }

    @Override
    public void run() {
        if (updateFile == null || !updateFile.exists()) {
            return;
        }

        plugin.getServer().broadcastMessage(plugin.redBold + "SERVER: " + ChatColor.GRAY + "Reloading in 5 seconds for an update. Hang in there!");

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
                plugin.playSound(player, EventSound.COMMAND_SUCCESS);
            }
            plugin.getServer().broadcastMessage(plugin.redBold + "SERVER: " + ChatColor.GRAY
                    + "Update was a " + ChatColor.GREEN + "" + ChatColor.BOLD + "success" + ChatColor.GRAY + "! Now go have fun!");
        }, 100);
    }
}
