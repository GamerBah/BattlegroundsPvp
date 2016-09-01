package me.gamerbah.Administration.Runnables;
/* Created by GamerBah on 8/24/2016 */


import me.gamerbah.Battlegrounds;
import org.bukkit.entity.Player;

public class AFKRunnable implements Runnable {

    private Battlegrounds plugin;

    public AFKRunnable(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (TrailRunnable.getStill().containsKey(player)) {
                if (TrailRunnable.getStill().get(player) < 600) {
                    TrailRunnable.getStill().put(player, TrailRunnable.getStill().get(player) + 1);
                }
                if (TrailRunnable.getStill().get(player) == 600) {
                    if (!plugin.getAfk().contains(player.getUniqueId())) {
                        player.chat("/afk");
                    }
                }
            }
        }
    }
}