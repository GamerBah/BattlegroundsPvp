package me.gamerbah.Listeners;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Commands.ReportCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if (inventory.getName().contains("Reporting:")) {
            if (ReportCommand.getReportBuilders().containsKey(player.getUniqueId())) {
                ReportCommand.getReportBuilders().remove(player.getUniqueId());
            }
            if (ReportCommand.getReportArray().containsKey(player.getUniqueId())) {
                ReportCommand.getReportArray().remove(player.getUniqueId());
            }
        }
        if (inventory.getName().contains("Punishment Creation:")) {
            if (Battlegrounds.punishmentCreation.containsKey(player)) {
                Battlegrounds.punishmentCreation.remove(player);
            }
        }
    }
}
