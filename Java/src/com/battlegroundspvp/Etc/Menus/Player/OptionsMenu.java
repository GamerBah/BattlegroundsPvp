package com.battlegroundspvp.Etc.Menus.Player;
/* Created by GamerBah on 8/15/2016 */


import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class OptionsMenu {
    private Battlegrounds plugin;

    public OptionsMenu(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player, Player target) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        PlayerData targetData = plugin.getPlayerData(target.getUniqueId());
        Inventory inv = plugin.getServer().createInventory(null, 27, "Options for " + target.getName());

        inv.setItem(10, new I(Material.BARRIER)
                .name(BoldColor.RED.getColor() + "Report Player"));
        inv.setItem(12, new I(Material.FEATHER)
                .name(BoldColor.GREEN.getColor() + "Send a Private Message")
                .lore(ChatColor.GRAY + "Use " + ChatColor.RED + "/msg " + target.getName() + " <message> " + ChatColor.GRAY + "to send your message!"));
        inv.setItem(14, new I(Material.DIAMOND_SWORD).flag(ItemFlag.HIDE_ATTRIBUTES).enchantment(Enchantment.DIG_SPEED, 2)
                .name(BoldColor.AQUA.getColor() + "Send a Team Request"));
        inv.setItem(16, new I((playerData.getKitPvpData().getPlayersRated().contains(targetData.getId() + ",")
                ? Material.GOLD_INGOT : Material.IRON_INGOT))
                .name(BoldColor.GREEN.getColor() + "+1 Combat Skills")
                .lore(ChatColor.GRAY + (playerData.getKitPvpData().getPlayersRated().contains(targetData.getId() + ",")
                        ? "You've already given this player" : "Gives the player a +1 on how"))
                .lore(ChatColor.GRAY + (playerData.getKitPvpData().getPlayersRated().contains(targetData.getId() + ",")
                        ? "a +1 on their Combat Skills" : "good their Combat Skills are")));

        player.openInventory(inv);
    }

}
