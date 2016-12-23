package com.battlegroundspvp.Administration.Runnables;
/* Created by GamerBah on 8/20/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Listeners.CombatListener;
import com.battlegroundspvp.Utils.Cosmetic;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.Packets.Particles.ParticleEffect;
import com.connorlinfoot.titleapi.TitleAPI;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

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
                if (!CombatListener.getTagged().containsKey(player.getUniqueId())) {
                    if (still.containsKey(player)) {
                        if (!Battlegrounds.getAfk().contains(player.getUniqueId())) {
                            if (!CombatListener.getTagged().containsKey(player.getUniqueId())) {
                                // Rare


                                // Epic
                                if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_RAIN_STORM)) {
                                    ParticleEffect.CLOUD.display(0.25F, 0.1F, 0.25F, 0, 6, player.getLocation().add(0, 2.5D, 0), 25);
                                    ParticleEffect.DRIP_WATER.display(0.15F, 0F, 0.15F, 0, 2, player.getLocation().add(0, 2.5D, 0), 25);
                                }
                                if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_LAVA_RAIN)) {
                                    ParticleEffect.SMOKE_LARGE.display(0.25F, 0.1F, 0.25F, 0, 8, player.getLocation().add(0, 2.5D, 0), 25);
                                    ParticleEffect.DRIP_LAVA.display(0.1F, 0, 0.1F, 0, 4, player.getLocation().add(0, 2.5D, 0), 25);
                                }

                                // Legendary
                                /* Flame Warrior Helix is in HelixRunnable.java */

                            }
                        }
                    } else {
                        if (plugin.getConfig().getBoolean("essenceActive")) {
                            ParticleEffect.REDSTONE.display(0.2F, 0.2F, 0.2F, 1, 5, player.getLocation().add(0, 1.25, 0), 25);
                        }
                        if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_RAIN_STORM)) {
                            ParticleEffect.DRIP_WATER.display(0.2F, 0, 0.2F, 0, 3, player.getLocation().add(0, 0.1D, 0), 25);
                            ParticleEffect.WATER_SPLASH.display(0.1F, 0, 0.1F, 0, 10, player.getLocation().add(0, 0.2D, 0), 25);
                        }
                        if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_LAVA_RAIN)) {
                            ParticleEffect.DRIP_LAVA.display(0.2F, 0, 0.2F, 0, 3, player.getLocation().add(0, 0.1D, 0), 25);
                            ParticleEffect.LAVA.display(0.1F, 0, 0.1F, 0, 1, player.getLocation().add(0, 0.1D, 0), 25);
                        }
                        if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_FLAME_WARRIOR)) {
                            ParticleEffect.FLAME.display(0, 0, 0, 0, 1, player.getLocation().add(0, 0.1, 0), 25);
                            ParticleEffect.FLAME.display(0, 0, 0, 0, 1, player.getLocation().add(0, 0.2, 0), 25);
                            ParticleEffect.FLAME.display(0, 0, 0, 0, 1, player.getLocation().add(0, 0.3, 0), 25);
                            ParticleEffect.SMOKE_LARGE.display(0.1F, 0.5F, 0.1F, 0, 5, player.getLocation(), 25);
                        }
                    }
                }
            }
        }
    }
}
