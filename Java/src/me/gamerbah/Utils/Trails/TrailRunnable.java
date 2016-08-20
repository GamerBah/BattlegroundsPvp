package me.gamerbah.Utils.Trails;
/* Created by GamerBah on 8/20/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.inventivetalent.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrailRunnable implements Runnable {

    private Battlegrounds plugin;
    private Map<Player, Location> playerLocations = new HashMap<>();
    private List<Player> still = new ArrayList<>();

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
                if (!still.contains(player)) {
                    playerLocations.put(player, player.getLocation());
                    still.add(player);
                }
            } else {
                playerLocations.put(player, player.getLocation());
                still.remove(player);
            }

            if (still.contains(player)) {
                if (playerData.getTrail().equals(Trail.Type.RAIN_STORM)) {
                    ParticleEffect.CLOUD.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 2.5D, 0), 0.25, 0.1, 0.25, 0, 6, 25);
                    ParticleEffect.DRIP_WATER.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 2.5D, 0), 0.15, 0, 0.15, 0, 2, 25);
                }
                if (playerData.getTrail().equals(Trail.Type.LAVA_RAIN)) {
                    ParticleEffect.SMOKE_LARGE.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 2.25D, 0), 0.25, 0.1, 0.25, 0, 8, 25);
                    ParticleEffect.DRIP_LAVA.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 2.5D, 0), 0.1, 0, 0.1, 0, 4, 25);
                }
            } else {
                if (playerData.getTrail().equals(Trail.Type.RAIN_STORM)) {
                    ParticleEffect.DRIP_WATER.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.1D, 0), 0.2, 0, 0.2, 0, 3, 25);
                    ParticleEffect.WATER_SPLASH.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.2D, 0), 0.1, 0, 0.1, 0, 10, 25);
                }
                if (playerData.getTrail().equals(Trail.Type.LAVA_RAIN)) {
                    ParticleEffect.DRIP_LAVA.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.1D, 0), 0.2, 0, 0.2, 0, 3, 25);
                    ParticleEffect.LAVA.send(Bukkit.getOnlinePlayers(), player.getLocation().add(0, 0.1D, 0), 0.1, 0, 0.1, 0, 1, 25);
                }
            }
        }
    }
}
