package com.battlegroundspvp.Listeners;
/* Created by GamerBah on 9/14/2016 */


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }


}
