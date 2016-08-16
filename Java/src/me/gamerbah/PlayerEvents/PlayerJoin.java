package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.I;
import me.gamerbah.Utils.Kits.Kit;
import me.gamerbah.Utils.Kits.KitManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Battlegrounds plugin;

    public PlayerJoin(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        PlayerData playerData = plugin.getPlayerData(event.getUniqueId());

        if (playerData == null) {
            plugin.createPlayerData(event.getUniqueId(), event.getName());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        if (!player.hasPlayedBefore()) {
            event.setJoinMessage(BoldColor.GOLD.getColor() + "New! " + BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.GREEN.getColor() + "+"
                    + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName());
        } else {
            if (playerData.isStealthyJoin()) {
                event.setJoinMessage(null);
            } else {
                event.setJoinMessage(BoldColor.DARK_GRAY.getColor() + "[" + BoldColor.GREEN.getColor() + "+"
                        + BoldColor.DARK_GRAY.getColor() + "] " + ChatColor.WHITE + event.getPlayer().getName());
            }
        }

        player.setPlayerListName((playerData.hasRank(Rank.ELITE) ? playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase() + " " : "")
                + (playerData.hasRank(Rank.ELITE) ? ChatColor.WHITE : ChatColor.GRAY) + player.getName());

        player.getInventory().setItem(0, new I(Material.DIAMOND_SWORD)
                .name(BoldColor.AQUA + "Kit Selector")
                .lore(ChatColor.GRAY + "Choose which kit you'll use!"));

        if (KitManager.getPreviousKit().containsKey(player.getUniqueId())) {
            Kit kit = KitManager.getPreviousKit().get(player.getUniqueId());
            player.getInventory().setItem(1, new I(Material.BOOK)
                    .name(ChatColor.GOLD + "Previous Kit: " + kit.getName())
                    .lore(ChatColor.GRAY + "Equip your previous kit."));
        }

        player.getInventory().setItem(7, new I(Material.DIAMOND)
                .name(ChatColor.YELLOW + "" + ChatColor.BOLD + "Challenges")
                .lore(ChatColor.GRAY + "View your challenges, start")
                .lore(ChatColor.GRAY + "new ones, and receive rewards!"));

        player.getInventory().setItem(8, new I(Material.REDSTONE_COMPARATOR)
                .name(ChatColor.RED + "" + ChatColor.BOLD + "Settings")
                .lore(ChatColor.GRAY + "Change your personal settings!"));
    }

}
