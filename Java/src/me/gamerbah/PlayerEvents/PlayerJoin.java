package me.gamerbah.PlayerEvents;
/* Created by GamerBah on 8/7/2016 */


import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Donations.Essence;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.attribute.Attribute;
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
        if (plugin.getPlayerData(event.getUniqueId()) == null) {
            plugin.createPlayerData(event.getUniqueId(), event.getName());
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                plugin.getPlayerData(event.getUniqueId()).setChallenges("");
                plugin.getPlayerData(event.getUniqueId()).setAchievements("");
            }, 2L);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        if (!plugin.getOne50Essence().containsKey(player.getUniqueId()))
            plugin.getOne50Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_50_PERCENT));
        if (!plugin.getOne100Essence().containsKey(player.getUniqueId()))
            plugin.getOne100Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_100_PERCENT));
        if (!plugin.getOne150Essence().containsKey(player.getUniqueId()))
            plugin.getOne150Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_150_PERCENT));
        if (!plugin.getThree50Essence().containsKey(player.getUniqueId()))
            plugin.getThree50Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_50_PERCENT));
        if (!plugin.getThree100Essence().containsKey(player.getUniqueId()))
            plugin.getThree100Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_100_PERCENT));
        if (!plugin.getThree150Essence().containsKey(player.getUniqueId()))
            plugin.getThree150Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_150_PERCENT));
        if (!plugin.getSix50Essence().containsKey(player.getUniqueId()))
            plugin.getSix50Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_50_PERCENT));
        if (!plugin.getSix100Essence().containsKey(player.getUniqueId()))
            plugin.getSix100Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_100_PERCENT));
        if (!plugin.getSix150Essence().containsKey(player.getUniqueId()))
            plugin.getSix150Essence().put(player.getUniqueId(), plugin.getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_150_PERCENT));

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

        player.setPlayerListName((playerData.hasRank(Rank.WARRIOR) ? playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase() + " " : "")
                + (playerData.hasRank(Rank.WARRIOR) ? ChatColor.WHITE : ChatColor.GRAY) + player.getName());

        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(20);
        plugin.respawn(player);
    }

}
