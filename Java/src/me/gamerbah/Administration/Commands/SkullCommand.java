package me.gamerbah.Administration.Commands;
/* Created by GamerBah on 8/29/2016 */

import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.EventSound;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCommand implements CommandExecutor {

    private Battlegrounds plugin;

    public SkullCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!playerData.hasRank(Rank.MODERATOR)) {
            plugin.sendNoPermission(player);
            return true;
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            player.sendMessage(ChatColor.RED + "You must be in Gamemode 1 to use this!");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        if (args.length == 0) {
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
            itemStack.setDurability((short) 3);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwner(player.getName());
            itemStack.setItemMeta(meta);

            player.getInventory().addItem(itemStack);
        }

        if (args.length == 1) {
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
            itemStack.setDurability((short) 3);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwner(args[0]);
            itemStack.setItemMeta(meta);

            player.getInventory().addItem(itemStack);
        }

        if (args.length > 1) {
            player.sendMessage(Battlegrounds.incorrectUsage + "/skull [name]");
            Battlegrounds.playSound(player, EventSound.ACTION_FAIL);
            return true;
        }

        return false;
    }

}