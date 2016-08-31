package me.gamerbah.Administration.Runnables;
/* Created by GamerBah on 8/30/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Listeners.CombatListener;
import me.gamerbah.Utils.Trails.Trail;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.inventivetalent.particle.ParticleEffect;

public class HelixRunnable implements Runnable {

    double phi = 0;
    private Battlegrounds plugin;

    public HelixRunnable(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
            if (TrailRunnable.getStill().containsKey(player)) {
                if (!Battlegrounds.getAfk().contains(player.getUniqueId())) {
                    if (!CombatListener.getTagged().containsKey(player.getUniqueId())) {
                        if (playerData.getTrail().equals(Trail.Type.FLAME_WARRIOR)) {
                            phi += Math.PI / 16;
                            double x, y, z;
                            Location location = player.getLocation();
                            for (double t = 0; t <= 2 * Math.PI; t = t + Math.PI / 12) {
                                for (double i = 0; i <= 2; i = i + 1) {
                                    x = 0.5 * (2 * Math.PI - t) * 0.375 * Math.cos(t + phi + i * Math.PI);
                                    y = 0.425 * t;
                                    z = 0.5 * (2 * Math.PI - t) * 0.375 * Math.sin(t + phi + i * Math.PI);
                                    location.add(x, y, z);
                                    ParticleEffect.FLAME.send(Bukkit.getOnlinePlayers(), location, 0, 0, 0, 0, 1, 25);
                                    location.subtract(x, y, z);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
