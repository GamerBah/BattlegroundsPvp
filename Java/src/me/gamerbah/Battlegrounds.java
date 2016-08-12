package me.gamerbah;
/* Created by GamerBah on 8/7/2016 */


import lombok.Getter;
import me.gamerbah.Administration.Commands.*;
import me.gamerbah.Administration.Punishments.Commands.MuteCommand;
import me.gamerbah.Administration.Punishments.Commands.UnmuteCommand;
import me.gamerbah.Commands.ReportCommand;
import me.gamerbah.Commands.StaffReqCommand;
import me.gamerbah.Data.MySQL;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Data.Query;
import me.gamerbah.Events.InventoryClick;
import me.gamerbah.Events.PlayerChat;
import me.gamerbah.Events.PlayerJoin;
import me.gamerbah.Utils.EventSound;
import net.gpedro.integrations.slack.SlackApi;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class Battlegrounds extends JavaPlugin {

    @Getter
    private static MySQL sql = null;
    private HashSet<PlayerData> playerData = new HashSet<>();
    public SlackApi slackReports = null;
    public SlackApi slackStaffRequests = null;

    public static String redBold = ChatColor.RED + "" + ChatColor.BOLD;
    public static String incorrectUsage = redBold + "Oops! " + ChatColor.GRAY + "Try this: " + redBold;

    public void onEnable() {
        registerCommands();
        registerListeners();

        sql = new MySQL(this);

        // Reload player data on reload
        for (Player player : getServer().getOnlinePlayers()) {
            playerData.add(sql.getPlayerData(player.getUniqueId()));
        }

        // Initialize SlackApi
        slackReports = new SlackApi("https://hooks.slack.com/services/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");
        slackStaffRequests = new SlackApi("https://hooks.slack.com/services/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");
    }

    public void onDisable() {

    }

    private void registerCommands() {
        getCommand("rank").setExecutor(new RankCommand(this));
        getCommand("clearchat").setExecutor(new ChatCommands(this));
        getCommand("lockchat").setExecutor(new ChatCommands(this));
        getCommand("cmdspy").setExecutor(new ChatCommands(this));
        getCommand("staff").setExecutor(new StaffChatCommand(this));
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
        getCommand("flyspeed").setExecutor(new FlySpeedCommand(this));
        getCommand("report").setExecutor(new ReportCommand(this));
        getCommand("staffreq").setExecutor(new StaffReqCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
    }

    public void playSound(Player player, EventSound eventSound) {
        player.playSound(player.getLocation(), eventSound.getSound1(), eventSound.getVol1(), eventSound.getPtch1());
        player.playSound(player.getLocation(), eventSound.getSound2(), eventSound.getVol2(), eventSound.getPtch2());
    }

    public PlayerData getPlayerData(UUID uuid) {
        Optional<PlayerData> playerDataStream = playerData.stream().filter(playerData -> playerData.getUuid().equals(uuid)).findFirst();

        if (playerDataStream.isPresent()) {
            return playerDataStream.get();
        } else {
            PlayerData dbPlayerData = sql.getPlayerData(uuid);
            if (dbPlayerData != null) {
                playerData.add(dbPlayerData);
                return getPlayerData(uuid);
            } else {
                return null;
            }
        }
    }

    public void createPlayerData(UUID uuid, String name) {
        sql.executeUpdate(Query.CREATE_PLAYER, uuid.toString(), name);
        getServer().getScheduler().runTaskLater(this, () -> playerData.add(sql.getPlayerData(uuid)), 4L);

    }

    public void sendNoPermission(Player player) {
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Sorry! " + ChatColor.GRAY + "You aren't allowed to use this command!");
        playSound(player, EventSound.COMMAND_FAIL);
    }

    public void respawn(Player player, Location location) {
        player.spigot().respawn();
        player.teleport(location);
        getServer().getPluginManager().callEvent(new PlayerRespawnEvent(player, location, false));
    }

}
