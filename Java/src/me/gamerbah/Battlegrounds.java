package me.gamerbah;
/* Created by GamerBah on 8/7/2016 */


import lombok.Getter;
import me.gamerbah.Administration.Commands.*;
import me.gamerbah.Administration.Punishments.Commands.MuteCommand;
import me.gamerbah.Administration.Punishments.Commands.UnmuteCommand;
import me.gamerbah.Administration.Utils.AutoUpdate;
import me.gamerbah.Commands.ReportCommand;
import me.gamerbah.Commands.SpawnCommand;
import me.gamerbah.Commands.StaffReqCommand;
import me.gamerbah.Commands.TeamCommand;
import me.gamerbah.Data.MySQL;
import me.gamerbah.Data.PlayerData;
import me.gamerbah.Data.Query;
import me.gamerbah.Events.*;
import me.gamerbah.Listeners.CombatListener;
import me.gamerbah.Listeners.ScoreboardListener;
import me.gamerbah.Utils.BoldColor;
import me.gamerbah.Utils.EventSound;
import net.gpedro.integrations.slack.SlackApi;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.glow.GlowAPI;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Battlegrounds extends JavaPlugin {
    @Getter
    private static Battlegrounds instance = null;
    @Getter
    private static MySQL sql = null;
    private HashSet<PlayerData> playerData = new HashSet<>();
    @Getter
    private static HashSet<UUID> afk = new HashSet<>();

    public SlackApi slackReports = null;
    public SlackApi slackStaffRequests = null;

    public static String incorrectUsage = BoldColor.RED.getColor() + "Oops! " + ChatColor.GRAY + "Try this: " + ChatColor.RED;
    public static Map<UUID, Integer> killStreak = new HashMap<>();
    public static Map<String, String> pendingTeams = new HashMap<>();
    public static Map<String, String> currentTeams = new ConcurrentHashMap<>();


    public void onEnable() {
        instance = this;

        registerCommands();
        registerListeners();

        sql = new MySQL(this);

        // Reload player data on reload
        for (Player player : getServer().getOnlinePlayers()) {
            playerData.add(sql.getPlayerData(player.getUniqueId()));
            GlowAPI.setGlowing(player, null, getServer().getOnlinePlayers());
        }

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoUpdate(this), 120, 120);

        // Initialize SlackApi
        slackReports = new SlackApi("https://hooks.slack.com/services/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");
        slackStaffRequests = new SlackApi("https://hooks.slack.com/services/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");

        ScoreboardListener scoreboardListener = new ScoreboardListener(this);
        scoreboardListener.updateScoreboards();
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
        getCommand("team").setExecutor(new TeamCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(this), this);
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

    // Respawn at location
    public void respawn(Player player, Location location) {
        player.spigot().respawn();
        player.teleport(location);
        getServer().getPluginManager().callEvent(new PlayerRespawnEvent(player, location, false));
    }

    // Respawn at world spawn
    public void respawn(Player player) {
        respawn(player, player.getWorld().getSpawnLocation().add(0.5, 0, 0.5));
    }

}
