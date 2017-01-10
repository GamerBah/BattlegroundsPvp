package com.battlegroundspvp.Commands;
/* Created by GamerBah on 8/15/2016 */

import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.connorlinfoot.titleapi.TitleAPI;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class SpectateCommand implements CommandExecutor {
    @Getter
    private static ArrayList<Player> spectating = new ArrayList<>();
    private Battlegrounds plugin;

    public SpectateCommand(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!playerData.hasRank(Rank.WARRIOR)) {
            plugin.sendNoPermission(player);
            return true;
        }
        plugin.respawn(player);
        if (!spectating.contains(player) || spectating.isEmpty()) {
            player.getInventory().clear();
            spectating.add(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, true));
            player.setAllowFlight(true);
            player.setFlying(true);
            TitleAPI.sendTitle(player, 5, 30, 10, ChatColor.GREEN + "You are now spectating!", " ");
            return true;
        }

        if (spectating.contains(player)) {
            spectating.remove(player);
            TitleAPI.sendTitle(player, 5, 30, 10, ChatColor.GREEN + "You are no longer spectating!", " ");
            return true;
        }

        return false;
    }
}
