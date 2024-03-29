package com.battlegroundspvp;
/* Created by GamerBah on 8/7/2016 */

import com.battlegroundspvp.Administration.Commands.*;
import com.battlegroundspvp.Administration.Data.GlobalStats;
import com.battlegroundspvp.Administration.Data.MySQL;
import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Data.Query;
import com.battlegroundspvp.Administration.Punishments.Commands.*;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Runnables.*;
import com.battlegroundspvp.Administration.Utils.ChatFilter;
import com.battlegroundspvp.Administration.Utils.PlayerCommandPreProccess;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Commands.*;
import com.battlegroundspvp.Etc.Menus.Punishment.PunishMenu;
import com.battlegroundspvp.Listeners.*;
import com.battlegroundspvp.PlayerEvents.*;
import com.battlegroundspvp.Utils.Enums.EventSound;
import com.battlegroundspvp.Utils.Enums.Time;
import com.battlegroundspvp.Utils.KDRatio;
import com.battlegroundspvp.Utils.Kits.KitManager;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.connorlinfoot.titleapi.TitleAPI;
import com.sun.istack.internal.Nullable;
import lombok.Getter;
import net.gpedro.integrations.slack.SlackApi;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.bossbar.BossBarAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Battlegrounds extends JavaPlugin {
    public static Map<UUID, Integer> killStreak = new HashMap<>();
    public static Map<String, String> pendingTeams = new HashMap<>();
    public static Map<String, String> currentTeams = new ConcurrentHashMap<>();
    public static Map<Player, Player> pendingFriends = new HashMap<>();
    public static HashMap<Player, HashMap<Punishment.Reason, Integer>> punishmentCreation = new HashMap<>();
    @Getter
    private static Battlegrounds instance = null;
    @Getter
    private static MySQL sql = null;
    @Getter
    private static ProtocolManager protocolManager;
    @Getter
    private static HashSet<UUID> afk = new HashSet<>();
    @Getter
    private static HashSet<UUID> cmdspies = new HashSet<>();
    @Getter
    private static HashSet<Player> fallDmg = new HashSet<>();
    public SlackApi slackReports = null;
    public SlackApi slackStaffRequests = null;
    public SlackApi slackDonations = null;
    public SlackApi slackPunishments = null;
    public SlackApi slackErrorReporting = null;
    @Getter
    private ArrayList<PlayerData> playerData = new ArrayList<>();
    @Getter
    private HashMap<UUID, ArrayList<Punishment>> playerPunishments = new HashMap<>();
    @Getter
    private GlobalStats globalStats = null;
    @Getter
    private List<String> filteredWords = new ArrayList<>();
    @Getter
    private List<String> safeWords = new ArrayList<>();
    @Getter
    private List<String> autoMessages = new ArrayList<>();
    @Getter
    private HashMap<UUID, UUID> messagers = new HashMap<>();
    @Getter
    private List<Location> uLaunchers = new ArrayList<>();
    @Getter
    private List<Location> fLaunchers = new ArrayList<>();
    @Getter
    private List<Location> fLaunchersParticle = new ArrayList<>();
    @Getter
    private List<Location> uLaunchersParticle = new ArrayList<>();

    public void onEnable() {
        instance = this;
        registerCommands();
        registerListeners();

        // Init SQL and Global Stat tracking
        sql = new MySQL(this);
        globalStats = sql.getGlobalStats();

        protocolManager = ProtocolLibrary.getProtocolManager();
        // Save Default Configuration
        saveDefaultConfig();

        // Reload player data on reload
        for (Player player : getServer().getOnlinePlayers()) {
            BossBarAPI.removeAllBars(player);
            PlayerData playerData = getPlayerData(player.getUniqueId());
            ScoreboardListener scoreboardListener = new ScoreboardListener(this);
            KDRatio kdRatio = new KDRatio(this);
            //GlowAPI.setGlowing(player, null, getServer().getOnlinePlayers());
            scoreboardListener.getRanks().put(player.getUniqueId(), playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase());
            scoreboardListener.getKills().put(player.getUniqueId(), playerData.getKitPvpData().getKills());
            scoreboardListener.getDeaths().put(player.getUniqueId(), playerData.getKitPvpData().getDeaths());
            scoreboardListener.getKds().put(player.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(player));
            scoreboardListener.getSouls().put(player.getUniqueId(), playerData.getKitPvpData().getSouls());
            scoreboardListener.getCoins().put(player.getUniqueId(), playerData.getCoins());
            scoreboardListener.updateScoreboardRank(player, playerData.getRank());
            scoreboardListener.updateScoreboardKills(player, 0);
            scoreboardListener.updateScoreboardDeaths(player, 0);
            scoreboardListener.updateScoreboardSouls(player, 0);
            scoreboardListener.updateScoreboardCoins(player, 0);
            respawn(player);
            TitleAPI.clearTitle(player);
        }

        // Initialize Launcher Lists
        uLaunchers = (List<Location>) getConfig().getList("launchersUp");
        uLaunchers.remove(0);
        fLaunchers = (List<Location>) getConfig().getList("launchersForward");
        fLaunchers.remove(0);

        for (Location location : uLaunchers)
            uLaunchersParticle.add(location.clone().add(0, 1, 0));
        for (Location location : fLaunchers)
            fLaunchersParticle.add(location.clone().add(0, 1, 0));

        // Initialize Various Repeating Tasks
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoUpdate(this), 120, 120);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new DonationUpdater(this), 0, 20);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TrailRunnable(this), 0, 2);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HelixRunnable(this), 0, 5);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AFKRunnable(this), 0, 20);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new PunishmentRunnable(this), 0, 20L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new WorldParticlesRunnable(this), 0, 2L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new MessageRunnable(this), 0, 6000L);

        // Save Filter File
        File filterFile = new File(getDataFolder(), "filter.txt");
        if (!filterFile.exists()) {
            saveResource("filter.txt", false);
        }
        try {
            Files.lines(FileSystems.getDefault().getPath(filterFile.getPath())).forEach(filterLine ->
                    filteredWords.add(ChatColor.translateAlternateColorCodes('&', filterLine)));
        } catch (IOException e) {
            getLogger().severe("Could not get filtered words!");
        }

        // Save SafeWords File
        File safeWordsFile = new File(getDataFolder(), "safewords.txt");
        if (!filterFile.exists()) {
            saveResource("safewords.txt", false);
        }
        try {
            Files.lines(FileSystems.getDefault().getPath(safeWordsFile.getPath())).forEach(wordLine ->
                    safeWords.add(ChatColor.translateAlternateColorCodes('&', wordLine)));
        } catch (IOException e) {
            getLogger().severe("Could not get safe words!");
        }

        // Initialize SlackApis
        slackReports = new SlackApi("https://hooks.slack.com/services/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");
        slackStaffRequests = new SlackApi("https://hooks.slack.com/services/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");
        slackDonations = new SlackApi("https://hooks.slack.com/services/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");
        slackPunishments = new SlackApi("https://hooks.slack.com/services/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");
        slackErrorReporting = new SlackApi("https://hooks.slack.com/services/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");


        // Make sure Menu Search Sign is in place
        getProtocolManager().getAsynchronousManager().registerAsyncHandler(
                new PacketAdapter(this, PacketType.Play.Client.UPDATE_SIGN) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacketType().equals(PacketType.Play.Client.UPDATE_SIGN)) {
                            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                                event.setCancelled(true);
                                Player player = event.getPlayer();
                                PunishMenu punishMenu = new PunishMenu(instance);

                                PacketContainer packetContainer = event.getPacket();
                                String term = packetContainer.getStringArrays().read(0)[0];
                                punishMenu.openPlayersMenu(player, PunishMenu.SortType.SEARCH, term, 0);
                                EventSound.playSound(player, EventSound.INVENTORY_OPEN_MENU);
                                Location loc = new Location(player.getWorld(), 0, 0, 0);
                                Sign sign = (Sign) loc.getBlock().getState();
                                sign.setLine(1, "§f§l^ ^ ^");
                                sign.setLine(2, "§bEnter the name");
                                sign.setLine(3, "§bto search for");
                                sign.update();
                            }
                        }
                    }
                }).syncStart();

        // Reload Relevant Data
        getServer().getScheduler().runTaskLater(this, () -> {
            reloadPunishments();
            reloadAllPlayerData();
        }, 5L);
    }

    public void onDisable() {
        sql.disconnect();
        uLaunchers.add(0, new Location(getServer().getWorlds().get(0), 3.1415, 3.1415, 3.1415));
        fLaunchers.add(0, new Location(getServer().getWorlds().get(0), 3.1415, 3.1415, 3.1415));
        getConfig().set("launchersUp", uLaunchers);
        getConfig().set("launchersForward", fLaunchers);
        saveConfig();
    }

    private void registerCommands() {
        getCommand("rank").setExecutor(new RankCommand(this));
        getCommand("clearchat").setExecutor(new ChatCommands(this));
        getCommand("lockchat").setExecutor(new ChatCommands(this));
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
        getCommand("donation").setExecutor(new DonationCommand(this));
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
        getCommand("refer").setExecutor(new ReferCommand(this));
        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("friend").setExecutor(new FriendCommand(this));
        getCommand("kick").setExecutor(new KickCommand(this));
        getCommand("tempban").setExecutor(new TempBanCommand(this));
        getCommand("crate").setExecutor(new CrateCommand(this));
        getCommand("warn").setExecutor(new WarnCommand(this));
        getCommand("rules").setExecutor(new RulesCommand(this));
        getCommand("spectate").setExecutor(new SpectateCommand(this));
        getCommand("launcher").setExecutor(new LauncherCommand(this));
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
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    public PlayerData getPlayerData(UUID uuid) {
        Optional<PlayerData> playerDataStream = playerData.stream().filter(playerData ->
                playerData.getUuid().equals(uuid)).findFirst();

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
        Optional<PlayerData> playerDataStream = playerData.stream().filter(playerData ->
                playerData.getName().equalsIgnoreCase(name)).findFirst();

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
        sql.executeUpdate(Query.CREATE_KITPVP_DATA, sql.getPlayerData(uuid).getId());
        sql.executeUpdate(Query.CREATE_SETTINGS_DATA, sql.getPlayerData(uuid).getId());
        sql.executeUpdate(Query.CREATE_ESSENCE_DATA, sql.getPlayerData(uuid).getId(), uuid);
        getServer().getScheduler().runTaskLater(this, () -> {
            playerData.add(sql.getPlayerData(uuid));
        }, 1L);
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
        }, 5L);
    }

    private void reloadPunishments() {
        if (!sql.getAllPunishments().isEmpty()) {
            for (Punishment punishment : sql.getAllPunishments()) {
                playerPunishments.put(punishment.getUuid(), sql.getAllPunishments(punishment.getUuid()));
            }
        }
    }

    private void reloadAllPlayerData() {
        if (!sql.getAllPlayerData().isEmpty()) {
            playerData.addAll(sql.getAllPlayerData());
        }
    }

    public void sendNoPermission(Player player) {
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Sorry! " + ChatColor.GRAY + "You aren't allowed to use this command!");
        EventSound.playSound(player, EventSound.ACTION_FAIL);
    }

    public void sendIncorrectUsage(Player player, String msg) {
        player.sendMessage(BoldColor.RED.getColor() + "Oops! " + ChatColor.GRAY + "Try this: " + ChatColor.RED + msg);
        EventSound.playSound(player, EventSound.ACTION_FAIL);
    }

    public void warnPlayer(@Nullable Player player, PlayerData targetData, Punishment.Reason reason) {
        if (!WarnCommand.getWarned().containsKey(targetData.getUuid())) {
            WarnCommand.getWarned().put(targetData.getUuid(), 1);
        } else {
            WarnCommand.getWarned().put(targetData.getUuid(), WarnCommand.getWarned().get(targetData.getUuid()) + 1);
        }
        int warns = WarnCommand.getWarned().get(targetData.getUuid());
        if (WarnCommand.getWarned().get(targetData.getUuid()) == 5) {
            getServer().getOnlinePlayers().stream().filter(players ->
                    getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.sendMessage(
                            BoldColor.DARK_RED.getColor() + " [ARES] " + ChatColor.GOLD + targetData.getName() + BoldColor.DARK_RED.getColor() + " (5)"
                                    + ChatColor.RED + "was " + (reason.getType().equals(Punishment.Type.MUTE) || reason.getType().equals(Punishment.Type.ALL) ? "muted" : "kicked")
                                    + " for " + ChatColor.GRAY + reason.getName() + (reason.getType().equals(Punishment.Type.MUTE)
                                    || reason.getType().equals(Punishment.Type.ALL) ? " for " + ChatColor.GRAY + Time.toString(reason.getLength() * 1000, true) : "")));
            getServer().getOnlinePlayers().stream().filter(players ->
                    getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.playSound(players.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1));
            if (player != null) {
                player.closeInventory();
            }

            if (reason.getType().equals(Punishment.Type.MUTE) || reason.getType().equals(Punishment.Type.ALL)) {
                HashMap<Punishment.Reason, Integer> punishment = new HashMap<>();
                punishment.put(reason, reason.getLength());
                MuteCommand.mutePlayer(targetData.getUuid(), player, punishment);
            } else {
                HashMap<Punishment.Reason, Integer> punishment = new HashMap<>();
                punishment.put(reason, -1);
                KickCommand.kickPlayer(targetData.getUuid(), player, punishment);
            }
            return;
        }
        if (WarnCommand.getWarned().get(targetData.getUuid()) >= 3) {
            getServer().getOnlinePlayers().stream().filter(players ->
                    getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.sendMessage(player != null ?
                            BoldColor.DARK_RED.getColor() + " !!! " + ChatColor.GRAY + player.getName() + ChatColor.RED + " warned "
                                    + ChatColor.GOLD + targetData.getName() + " (" + warns + ")" + ChatColor.RED + " for " + ChatColor.GRAY + reason.getName()
                            : BoldColor.DARK_RED.getColor() + " !!! " + BoldColor.AQUA.getColor() + "Ares" + ChatColor.GRAY + ": " + ChatColor.RED + "I automatically warned "
                            + ChatColor.GOLD + targetData.getName() + " (" + warns + ")" + ChatColor.RED + " for you " + ChatColor.GRAY + "(" + reason.getName() + ")"));
            getServer().getOnlinePlayers().stream().filter(players ->
                    getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.playSound(players.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1));
            if (player != null) {
                player.closeInventory();
            }
            return;
        }
        if (player != null) {
            player.closeInventory();
        }
        getServer().getOnlinePlayers().stream().filter(players ->
                getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                .forEach(players -> players.sendMessage(player != null ?
                        ChatColor.GRAY + player.getName() + ChatColor.RED + " warned "
                                + ChatColor.GOLD + targetData.getName() + " (" + warns + ")" + ChatColor.RED + " for " + ChatColor.GRAY + reason.getName()
                        : BoldColor.AQUA.getColor() + "Ares" + ChatColor.GRAY + ": " + ChatColor.RED + "I automatically warned "
                        + ChatColor.GOLD + targetData.getName() + " (" + warns + ")" + ChatColor.RED + " for you " + ChatColor.GRAY + "(" + reason.getName() + ")"));
        getServer().getOnlinePlayers().stream().filter(players ->
                getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                .forEach(players -> players.playSound(players.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1));
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
        PlayerData playerData = getPlayerData(player.getUniqueId());
        return (playerData.getEssenceData().getOne50()
                + playerData.getEssenceData().getOne100()
                + playerData.getEssenceData().getOne150()
                + playerData.getEssenceData().getThree50()
                + playerData.getEssenceData().getThree100()
                + playerData.getEssenceData().getThree150()
                + playerData.getEssenceData().getSix50()
                + playerData.getEssenceData().getSix100()
                + playerData.getEssenceData().getSix150()
        );
    }

}
