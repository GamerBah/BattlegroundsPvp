package com.battlegroundspvp.PlayerEvents;
/* Created by GamerBah on 8/14/2016 */


import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Commands.SpectateCommand;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Kits.KitManager;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Packets.Particles.ParticleEffect;
import com.connorlinfoot.titleapi.TitleAPI;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerMove implements Listener {

    @Getter
    private static List<Player> launched = new ArrayList<>();
    private Battlegrounds plugin;

    public PlayerMove(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().distance(player.getWorld().getSpawnLocation()) > 9 && !SpectateCommand.getSpectating().contains(player)) {
            if (!KitManager.isPlayerInKit(player)) {
                plugin.respawn(player);
                TitleAPI.sendTitle(player, 5, 40, 10, " ", ChatColor.GRAY + "Choose a kit first!");
                Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                return;
            }
        }

        if (event.getTo().getBlock().getType().equals(Material.GOLD_PLATE)) {
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                return;
            }
            if (Battlegrounds.getAfk().contains(player.getUniqueId())) {
                return;
            }
            Set<Material> set = null;
            if (player.getTargetBlock(set, 15).getLocation().distance(player.getWorld().getSpawnLocation()) <= 8) {
                return;
            }
            if (player.getTargetBlock(set, 20).getLocation().distance(player.getWorld().getSpawnLocation()) <= 15
                    && player.getTargetBlock(set, 20).getLocation().getBlockY() >= 32) {
                return;
            }
            if (!KitManager.isPlayerInKit(player)) {
                return;
            }
            player.setVelocity(player.getLocation().getDirection().multiply(3));
            player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 2, 0.3F);
            BukkitTask task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                ParticleEffect.FIREWORKS_SPARK.display(0, 0.5F, 0, 0.05F, 5, player.getLocation(), 30);
            }, 0L, 1L);
            plugin.getServer().getScheduler().runTaskLater(plugin, task::cancel, 20);
        }
    }


    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            if (event.getTo().distance(player.getWorld().getSpawnLocation()) <= 15 || event.getTo().distance(player.getWorld().getSpawnLocation()) >= 60) {
                event.setCancelled(true);
                player.getInventory().addItem(new I(Material.ENDER_PEARL).name(BoldColor.GOLD.getColor() + "Enderpearl"));
            }
        }
    }
}
