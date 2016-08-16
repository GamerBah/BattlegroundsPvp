package me.gamerbah.Etc.Menus;
/* Created by GamerBah on 8/15/2016 */


import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.I;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class OptionsMenu {
    private Battlegrounds plugin;

    public OptionsMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player, Player target) {
        Inventory inv = plugin.getServer().createInventory(null, 27, "Options for " + target.getName());

        inv.setItem(11, new I(Material.BARRIER).name(ChatColor.RED + "Report Player"));
        inv.setItem(13, new I(Material.FEATHER).name(ChatColor.GREEN + "Send a Private Message")
                .lore(ChatColor.GRAY + "Use " + ChatColor.RED + "/msg " + target.getName() + " <message> " + ChatColor.GRAY + "to send your message!"));
        inv.setItem(15, new I(Material.DIAMOND_SWORD).name(ChatColor.AQUA + "Send a Team Request").enchantment(Enchantment.DIG_SPEED, 2).clearFlags());

        player.openInventory(inv);
    }

}
