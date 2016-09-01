package me.gamerbah;
/* Created by GamerBah on 8/7/2016 */


import com.connorlinfoot.titleapi.TitleAPI;
import lombok.Getter;
import me.gamerbah.Administration.Commands.*;
import me.gamerbah.Administration.Data.MySQL;
import me.gamerbah.Administration.Data.PlayerData;
import me.gamerbah.Administration.Data.Query;
import me.gamerbah.Administration.Donations.Essence;
import me.gamerbah.Administration.Punishments.Commands.BanCommand;
import me.gamerbah.Administration.Punishments.Commands.MuteCommand;
import me.gamerbah.Administration.Punishments.Commands.UnbanCommand;
import me.gamerbah.Administration.Punishments.Commands.UnmuteCommand;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Runnables.*;
import me.gamerbah.Administration.Utils.ChatFilter;
import me.gamerbah.Administration.Utils.PlayerCommandPreProccess;
import me.gamerbah.Commands.*;
import me.gamerbah.Listeners.*;
import me.gamerbah.PlayerEvents.*;
import me.gamerbah.Utils.EventSound;
import me.gamerbah.Utils.KDRatio;
import me.gamerbah.Utils.Kits.KitManager;
import me.gamerbah.Utils.Messages.BoldColor;
import net.gpedro.integrations.slack.SlackApi;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.glow.GlowAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Battlegrounds extends JavaPlugin {
    public static String incorrectUsage = BoldColor.RED.getColor() + "Oops! " + ChatColor.GRAY + "Try this: " + ChatColor.RED;
    public static Map<UUID, Integer> killStreak = new HashMap<>();
    public static Map<String, String> pendingTeams = new HashMap<>();
    public static Map<String, String> currentTeams = new ConcurrentHashMap<>();
    @Getter
    private static Battlegrounds instance = null;
    @Getter
    private static MySQL sql = null;
    @Getter
    private static HashSet<UUID> afk = new HashSet<>();
    public SlackApi slackReports = null;
    public SlackApi slackStaffRequests = null;
    private HashSet<PlayerData> playerData = new HashSet<>();
    @Getter
    private HashMap<UUID, ArrayList<Punishment>> playerPunishments = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> one50Essence = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> one100Essence = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> one150Essence = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> three50Essence = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> three100Essence = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> three150Essence = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> six50Essence = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> six100Essence = new HashMap<>();
    @Getter
    private HashMap<UUID, Integer> six150Essence = new HashMap<>();
    @Getter
    private List<String> filteredWords = new ArrayList<>();
    @Getter
    private HashMap<UUID, UUID> messagers = new HashMap<>();
    @Getter
    private ArrayList<Location> fireworkBlocks = new ArrayList<>();
    @Getter
    private ArrayList<Location> launchers = new ArrayList<>();

    public void onEnable() {
        instance = this;
        registerCommands();
        registerListeners();

        sql = new MySQL(this);

        // Save Default Configuration
        saveDefaultConfig();

        // Reload player data on reload
        for (Player player : getServer().getOnlinePlayers()) {
            playerData.add(sql.getPlayerData(player.getUniqueId()));
            PlayerData playerData = getPlayerData(player.getUniqueId());
            ScoreboardListener scoreboardListener = new ScoreboardListener(this);
            KDRatio kdRatio = new KDRatio(this);
            GlowAPI.setGlowing(player, null, getServer().getOnlinePlayers());
            scoreboardListener.getRanks().put(player.getUniqueId(), playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase());
            scoreboardListener.getKills().put(player.getUniqueId(), playerData.getKills());
            scoreboardListener.getDeaths().put(player.getUniqueId(), playerData.getDeaths());
            scoreboardListener.getKds().put(player.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(player));
            scoreboardListener.getSouls().put(player.getUniqueId(), playerData.getSouls());
            scoreboardListener.getCoins().put(player.getUniqueId(), playerData.getCoins());
            respawn(player);
            TitleAPI.clearTitle(player);
            if (!getOne50Essence().containsKey(player.getUniqueId()))
                getOne50Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_50_PERCENT));
            if (!getOne100Essence().containsKey(player.getUniqueId()))
                getOne100Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_100_PERCENT));
            if (!getOne150Essence().containsKey(player.getUniqueId()))
                getOne150Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.ONE_HOUR_150_PERCENT));
            if (!getThree50Essence().containsKey(player.getUniqueId()))
                getThree50Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_50_PERCENT));
            if (!getThree100Essence().containsKey(player.getUniqueId()))
                getThree100Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_100_PERCENT));
            if (!getThree150Essence().containsKey(player.getUniqueId()))
                getThree150Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.THREE_HOUR_150_PERCENT));
            if (!getSix50Essence().containsKey(player.getUniqueId()))
                getSix50Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_50_PERCENT));
            if (!getSix100Essence().containsKey(player.getUniqueId()))
                getSix100Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_100_PERCENT));
            if (!getSix150Essence().containsKey(player.getUniqueId()))
                getSix150Essence().put(player.getUniqueId(), getSql().getEssenceAmount(player, Essence.Type.SIX_HOUR_150_PERCENT));
        }

        reloadPunishments();

        // Initialize Various Repeating Tasks
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoUpdate(this), 120, 120);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new DonationUpdater(this), 0, 20);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TrailRunnable(this), 0, 2);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HelixRunnable(this), 0, 5);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AFKRunnable(this), 0, 20);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new PunishmentRunnable(this), 0, 20L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new WorldParticlesRunnable(this), 0, 2L);

        // Save Filter File
        File filterFile = new File(getDataFolder(), "filter.txt");
        if (!filterFile.exists()) {
            saveResource("filter.txt", false);
        }
        try {
            Files.lines(FileSystems.getDefault().getPath(filterFile.getPath())).forEach(filterLine -> filteredWords.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', filterLine)));
        } catch (IOException e) {
            getLogger().severe("Could not get filtered words!");
        }

        // Initialize SlackApi
        slackReports = new SlackApi("https://hooks.slack.com/services/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");
        slackStaffRequests = new SlackApi("https://hooks.slack.com/services/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");

        // Register Firework Blocks
        fireworkBlocks.add(new Location(getServer().getWorld("Colosseum"), -5.5, 37.0, 0.5));
        fireworkBlocks.add(new Location(getServer().getWorld("Colosseum"), -3.5, 37.0, -3.5));
        fireworkBlocks.add(new Location(getServer().getWorld("Colosseum"), 0.5, 37.0, 6.5));
        fireworkBlocks.add(new Location(getServer().getWorld("Colosseum"), 6.5, 37.0, 0.5));
        fireworkBlocks.add(new Location(getServer().getWorld("Colosseum"), 4.5, 37.0, 4.5));
        fireworkBlocks.add(new Location(getServer().getWorld("Colosseum"), 0.5, 37.0, -5.5));
        fireworkBlocks.add(new Location(getServer().getWorld("Colosseum"), 4.5, 37.0, -3.5));
        fireworkBlocks.add(new Location(getServer().getWorld("Colosseum"), -3.5, 37.0, 4.5));

        // Register Launcher Blocks
        launchers.add(new Location(getServer().getWorld("Colosseum"), 8, 27.15, 0));
        launchers.add(new Location(getServer().getWorld("Colosseum"), 6, 27.15, 6));
        launchers.add(new Location(getServer().getWorld("Colosseum"), 0, 27.15, 8));
        launchers.add(new Location(getServer().getWorld("Colosseum"), -8, 27.15, 0));
        launchers.add(new Location(getServer().getWorld("Colosseum"), -6, 27.15, -6));
        launchers.add(new Location(getServer().getWorld("Colosseum"), 0, 27.15, -8));
        launchers.add(new Location(getServer().getWorld("Colosseum"), 6, 27.15, -6));
        launchers.add(new Location(getServer().getWorld("Colosseum"), -6, 27.15, 6));
    }

    public void onDisable() {
        sql.closeConnection();
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
        getCommand("afk").setExecutor(new AFKCommand(this));
        getCommand("message").setExecutor(new MessageCommand(this));
        getCommand("reply").setExecutor(new ReplyCommand(this));
        getCommand("ping").setExecutor(new PingCommand(this));
        getCommand("options").setExecutor(new OptionsCommand(this));
        getCommand("essence").setExecutor(new EssenceCommand(this));
        getCommand("essences").setExecutor(new EssencesCommand(this));
        getCommand("thanks").setExecutor(new ThanksCommand(this));
        getCommand("punish").setExecutor(new PunishCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("maintenance").setExecutor(new MaintenanceCommand(this));
        getCommand("slots").setExecutor(new SlotsCommand(this));
        getCommand("dailyreward").setExecutor(new DailyRewardCommand(this));
        getCommand("skull").setExecutor(new SkullCommand(this));
        getCommand("help").setExecutor(new HelpCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerFoodLevelChange(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
        getServer().getPluginManager().registerEvents(new ItemSpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandPreProccess(this), this);
        getServer().getPluginManager().registerEvents(new ChatFilter(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
        getServer().getPluginManager().registerEvents(new KitManager(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new ServerListPingListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerItemDrop(this), this);
        getServer().getPluginManager().registerEvents(new PlayerItemPickup(this), this);
        getServer().getPluginManager().registerEvents(new SpawnProtectListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerSwapHands(this), this);
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

    public PlayerData getPlayerData(String name) {
        Optional<PlayerData> playerDataStream = playerData.stream().filter(playerData -> playerData.getName().equals(name)).findFirst();

        if (playerDataStream.isPresent()) {
            return playerDataStream.get();
        } else {
            PlayerData dbPlayerData = sql.getPlayerData(name);
            if (dbPlayerData != null) {
                playerData.add(dbPlayerData);
                return getPlayerData(name);
            } else {
                return null;
            }
        }
    }

    public void createPlayerData(UUID uuid, String name) {
        sql.executeUpdate(Query.CREATE_PLAYER_DATA, uuid.toString(), name);
        getServer().getScheduler().runTaskLater(this, () -> playerData.add(sql.getPlayerData(uuid)), 4L);
    }

    public HashMap<UUID, Integer> getEssenceData(Essence.Type type) {
        switch (type) {
            case ONE_HOUR_50_PERCENT:
                return getOne50Essence();
            case ONE_HOUR_100_PERCENT:
                return getOne100Essence();
            case ONE_HOUR_150_PERCENT:
                return getOne150Essence();
            case THREE_HOUR_50_PERCENT:
                return getThree50Essence();
            case THREE_HOUR_100_PERCENT:
                return getThree100Essence();
            case THREE_HOUR_150_PERCENT:
                return getThree150Essence();
            case SIX_HOUR_50_PERCENT:
                return getSix50Essence();
            case SIX_HOUR_100_PERCENT:
                return getSix100Essence();
            case SIX_HOUR_150_PERCENT:
                return getSix150Essence();
        }
        return null;
    }

    public void createEssenceData(UUID uuid, Essence.Type type, int amount) {
        sql.executeUpdate(Query.CREATE_ESSENCE_DATA, uuid.toString(), type.toString(), amount);
        getServer().getScheduler().runTaskLater(this, () -> getEssenceData(type).put(uuid, amount), 4L);
    }

    private Punishment getPunishment(UUID uuid, Punishment.Type type, LocalDateTime date) {
        ArrayList<Punishment> punishments = playerPunishments.get(uuid);
        Optional<Punishment> punishmentStream = punishments.stream().filter(punishment -> punishment.getUuid().equals(uuid)
                && punishment.getType().equals(type) && punishment.getDate().equals(date)).findFirst();

        if (punishmentStream.isPresent()) {
            return punishmentStream.get();
        } else {
            Punishment punishment = sql.getPunishment(uuid, type, date);
            if (punishment != null) {
                punishments.add(punishment);
                playerPunishments.put(uuid, punishments);
                return getPunishment(uuid, type, date);
            } else {
                return null;
            }
        }
    }

    public void createPunishment(UUID uuid, String name, Punishment.Type type, LocalDateTime date, Integer duration, UUID enforcer, Punishment.Reason reason) {
        sql.executeUpdate(Query.CREATE_PUNISHMENT, uuid.toString(), name, type.toString(), date.toString(), duration,
                date.plusSeconds(duration).toString().replace(" ", "T"), enforcer.toString(), reason.toString());
        getServer().getScheduler().runTaskLater(this, () -> {
            ArrayList<Punishment> punishments = playerPunishments.get(uuid);
            punishments.add(sql.getPunishment(uuid, type, date));
            playerPunishments.put(uuid, punishments);
        }, 20L);
    }

    public void reloadPunishments() {
        if (!sql.getAllPunishments().isEmpty()) {
            for (Punishment punishment : sql.getAllPunishments()) {
                playerPunishments.put(punishment.getUuid(), sql.getAllPunishments(punishment.getUuid()));
            }
        }
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

    public int getTotalEssenceAmount(Player player) {
        return (getOne50Essence().get(player.getUniqueId()) + getOne100Essence().get(player.getUniqueId()) + getOne150Essence().get(player.getUniqueId())
                + getThree50Essence().get(player.getUniqueId()) + getThree100Essence().get(player.getUniqueId()) + getThree150Essence().get(player.getUniqueId())
                + getSix50Essence().get(player.getUniqueId()) + getSix100Essence().get(player.getUniqueId()) + getSix150Essence().get(player.getUniqueId()));
    }

}
