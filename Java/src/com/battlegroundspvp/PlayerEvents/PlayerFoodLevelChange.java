package com.battlegroundspvp.PlayerEvents;
/* Created by GamerBah on 8/15/2016 */


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerFoodLevelChange implements Listener {

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        event.setCancelled(true);
        player.setFoodLevel(20);
    }
}
