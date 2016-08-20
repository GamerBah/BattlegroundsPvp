package me.gamerbah.Utils.Kits;

import lombok.Data;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

/**
 * Represents a kit
 *
 * @author computerwizjared
 */
@Data
public abstract class Kit implements Listener, CommandExecutor {
    /**
     * Name of the kit
     */
    private String name = "";
    /**
     * Item representing the kit
     */
    private ItemStack item = new ItemStack(Material.AIR);
    /**
     * Lore of the kit
     */
    private String lore = "";
    /**
     * Type of the kit
     */
    private Rarity rarity = Rarity.COMMON;

    /**
     * Defines a kit
     *
     * @param name  Name of the kit
     * @param item  Item representing the kit
     * @param rarity Color of the kit
     */
    public Kit(String name, ItemStack item, Rarity rarity) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(rarity.getColor() + name);
        item.setItemMeta(meta);
        this.name = name;
        this.item = item;
        this.rarity = rarity;
    }

    /**
     * Defines a kit
     *
     * @param name  Name of the kit
     * @param item  Item representing the kit
     * @param lore  Lore of the kit
     * @param rarity Color of the kit
     */
    public Kit(String name, ItemStack item, String lore, Rarity rarity) {
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lores = new ArrayList<>();
        lores.add(lore);
        meta.setLore(lores);
        meta.setDisplayName(rarity.getColor() + name);
        item.setItemMeta(meta);
        this.name = name;
        this.item = item;
        this.lore = lore;
        this.rarity = rarity;
    }

    /**
     * Wear the kit
     *
     * @param player Player to wear kit
     */
    protected abstract void wear(Player player);

    /**
     * Wear the kit with permissions and messages
     *
     * @param player Player to wear kit
     */
    public void wearCheckLevel(Player player) {
        PlayerData playerData = Battlegrounds.getInstance().getPlayerData(player.getUniqueId());
        if (!KitManager.isPlayerInKit(player) || (playerData.hasRank(Rank.WARRIOR) && player.getLocation().distance(player.getWorld().getSpawnLocation()) < 200)) {
            KitManager.getPlayersInKits().put(player.getUniqueId(), this);

            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            if (player.getLocation().distance(player.getWorld().getSpawnLocation()) < 200) {
                wear(player);
                return;
            }
            wear(player);
        } else {
            player.sendMessage(ChatColor.RED + "You have not died yet!");
        }
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase(getName().replaceAll("\\s+", ""))) {
                wearCheckLevel(player);
            }
        }
        return false;
    }
}
