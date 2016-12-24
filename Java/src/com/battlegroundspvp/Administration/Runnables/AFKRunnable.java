package com.battlegroundspvp.Administration.Runnables;
/* Created by GamerBah on 8/24/2016 */


import com.battlegroundspvp.Battlegrounds;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AFKRunnable implements Runnable {

    @Getter
    private static HashMap<Player, Integer> afkTimer = new HashMap<>();
    private Battlegrounds plugin;

    public AFKRunnable(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (afkTimer.containsKey(player)) {
                if (afkTimer.get(player) < 300) {
                    afkTimer.put(player, afkTimer.get(player) + 1);
                }
                if (afkTimer.get(player) == 300) {
                    if (!Battlegrounds.getAfk().contains(player.getUniqueId())) {
                        player.chat("/afk");
                    }
                }
            }
        }
    }
}