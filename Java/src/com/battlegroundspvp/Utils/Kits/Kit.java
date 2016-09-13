package com.battlegroundspvp.Utils.Kits;

import com.battlegroundspvp.Administration.Commands.FreezeCommand;
import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.I;
import com.battlegroundspvp.Utils.Rarity;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

/**
 * Represents a kit
 *
 * @author computerwizjared
 */
@Data
public abstract class Kit implements Listener, CommandExecutor {
    /**
     * ID of the kit
     */
    private int id;
    /**
     * Name of the kit
     */
    private String name = "";
    /**
     * Item representing the kit
     * Contains lore and display name
     */
    private I item = new I(Material.AIR);
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
    public Kit(Integer id, String name, I item, Rarity rarity) {
        this.id = id;
        item.name(rarity.getColor() + (rarity.equals(Rarity.EPIC) || rarity.equals(Rarity.LEGENDARY) ? "" + ChatColor.BOLD : "") + name);
        this.name = name;
        this.item = item;
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
                if (!FreezeCommand.frozenPlayers.contains(player) && !FreezeCommand.frozen && Battlegrounds.getSql().getPlayerData(player.getUniqueId()).getOwnedKits().contains(this.getId() + ",")) {
                    wearCheckLevel(player);
                }
                if (!Battlegrounds.getSql().getPlayerData(player.getUniqueId()).getOwnedKits().contains(this.getId() + ",")) {
                    player.sendMessage(ChatColor.RED + "You haven't unlocked this kit yet!");
                    Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
                }
            }
        }
        return false;
    }
}
