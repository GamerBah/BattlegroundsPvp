package com.battlegroundspvp.Administration.Runnables;
/* Created by GamerBah on 8/30/2016 */


import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Listeners.CombatListener;
import com.battlegroundspvp.Utils.Enums.Cosmetic;
import com.battlegroundspvp.Utils.Packets.Particles.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
                        if (playerData.getTrail().equals(Cosmetic.Item.TRAIL_FLAME_WARRIOR)) {
                            phi += Math.PI / 16;
                            double x, y, z;
                            Location location = player.getLocation();
                            for (double t = 0; t <= 2 * Math.PI; t = t + Math.PI / 12) {
                                for (double i = 0; i <= 2; i = i + 1) {
                                    x = 0.5 * (2 * Math.PI - t) * 0.375 * Math.cos(t + phi + i * Math.PI);
                                    y = 0.425 * t;
                                    z = 0.5 * (2 * Math.PI - t) * 0.375 * Math.sin(t + phi + i * Math.PI);
                                    location.add(x, y, z);
                                    ParticleEffect.FLAME.display(0, 0, 0, 0, 1, location, 25);
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
