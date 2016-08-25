package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 8/25/2016 */


import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Battlegrounds;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class PunishMenu {
    private Battlegrounds plugin;

    public PunishMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openPlayersInventory(Player player, Player target) {
        Inventory inv = plugin.getServer().createInventory(null, 45, "Reporting: " + target.getName());
    }

    public void openInventory(Player player, Player target) {
        Inventory inv = plugin.getServer().createInventory(null, 27, "Punishing: " + target.getName());

        ArrayList<Punishment> punishments = plugin.getPlayerPunishments().get(target.getUniqueId());
    }
}
