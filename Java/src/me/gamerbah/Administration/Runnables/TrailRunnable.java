package me.gamerbah.Administration.Runnables;
/* Created by GamerBah on 8/20/2016 */


import com.connorlinfoot.titleapi.TitleAPI;
import lombok.Getter;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Listeners.CombatListener;
import me.gamerbah.Utils.Cosmetic;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.inventivetalent.particle.ParticleEffect;

import java.util.HashMap;
import java.util.Map;

public class TrailRunnable implements Runnable {

    @Getter
    private static HashMap<Player, Integer> still = new HashMap<>();
    private Battlegrounds plugin;
    private Map<Player, Location> playerLocations = new HashMap<>();

    public TrailRunnable(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
            playerLocations.putIfAbsent(player, player.getLocation());

            if (player.getLocation().getX() == playerLocations.get(player).getX()
                    && player.getLocation().getY() == playerLocations.get(player).getY()
                    && player.getLocation().getZ() == playerLocations.get(player).getZ()) {
                if (!still.containsKey(player)) {
                    playerLocations.put(player, player.getLocation());
                    still.put(player, -1);
                }
            } else {
                playerLocations.put(player, player.getLocation());
                still.remove(player);
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    if (Battlegrounds.getAfk().contains(player.getUniqueId())) {
                        if (!still.containsKey(player)) {
                            TitleAPI.clearTitle(player);
                            player.sendMessage(ChatColor.GRAY + "You are no longer AFK");
                            Battlegrounds.playSound(player, EventSound.CLICK);
                            Battlegrounds.getAfk().remove(player.getUniqueId());
                            plugin.respawn(player);
                            player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        }
                    }
                }, 3L);
            }

            if (player.getGameMode() != GameMode.CREATIVE) {
                if (still.containsKey(player)) {
                    if (!Battlegrounds.getAfk().contains(player.getUniqueId())) {
                        if (!CombatListener.getTagged().containsKey(player.getUniqueId())) {
                            // Rare


                            // Epic
                            if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_RAIN_STORM)) {
                                ParticleEffect.CLOUD.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 2.5D, 0), 0.25, 0.1, 0.25, 0, 6, 25);
                                ParticleEffect.DRIP_WATER.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 2.5D, 0), 0.15, 0, 0.15, 0, 2, 25);
                            }
                            if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_LAVA_RAIN)) {
                                ParticleEffect.SMOKE_LARGE.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 2.25D, 0), 0.25, 0.1, 0.25, 0, 8, 25);
                                ParticleEffect.DRIP_LAVA.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 2.5D, 0), 0.1, 0, 0.1, 0, 4, 25);
                            }

                            // Legendary
                    /* Flame Warrior Helix is in HelixRunnable.java */

                        }
                    }
                } else {
                    if (plugin.getConfig().getBoolean("essenceActive")) {
                        ParticleEffect.REDSTONE.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 1.25, 0), 0.2, 0.2, 0.2, 1, 5, 25);
                    }
                    if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_RAIN_STORM)) {
                        ParticleEffect.DRIP_WATER.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.1D, 0), 0.2, 0, 0.2, 0, 3, 25);
                        ParticleEffect.WATER_SPLASH.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.2D, 0), 0.1, 0, 0.1, 0, 10, 25);
                    }
                    if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_LAVA_RAIN)) {
                        ParticleEffect.DRIP_LAVA.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.1D, 0), 0.2, 0, 0.2, 0, 3, 25);
                        ParticleEffect.LAVA.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.1D, 0), 0.1, 0, 0.1, 0, 1, 25);
                    }
                    if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_FLAME_WARRIOR)) {
                        ParticleEffect.FLAME.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.1, 0), 0, 0, 0, 0, 1, 25);
                        ParticleEffect.FLAME.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.2, 0), 0, 0, 0, 0, 1, 25);
                        ParticleEffect.FLAME.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.3, 0), 0, 0, 0, 0, 1, 25);
                        ParticleEffect.SMOKE_LARGE.send(Bukkit.getOnlinePlayers(), player.getLocation(), 0.1, 0.5, 0.1, 0, 5, 25);
                    }
                }
            }
        }
    }
}
