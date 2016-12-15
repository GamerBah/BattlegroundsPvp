package com.battlegroundspvp;
/* Created by GamerBah on 8/7/2016 */


import com.battlegroundspvp.Administration.Commands.*;
import com.battlegroundspvp.Administration.Data.MySQL;
import com.battlegroundspvp.Administration.Data.PlayerData;
import com.battlegroundspvp.Administration.Data.Query;
import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Administration.Punishments.Commands.*;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Runnables.*;
import com.battlegroundspvp.Administration.Utils.ChatFilter;
import com.battlegroundspvp.Administration.Utils.PlayerCommandPreProccess;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Commands.*;
import com.battlegroundspvp.Etc.Menus.PunishMenu;
import com.battlegroundspvp.Listeners.*;
import com.battlegroundspvp.PlayerEvents.*;
import com.battlegroundspvp.Utils.EventSound;
import com.battlegroundspvp.Utils.KDRatio;
import com.battlegroundspvp.Utils.Kits.KitManager;
import com.battlegroundspvp.Utils.Messages.BoldColor;
import com.battlegroundspvp.Utils.Time;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.connorlinfoot.titleapi.TitleAPI;
import lombok.Getter;
import net.gpedro.integrations.slack.SlackApi;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.bossbar.BossBarAPI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    public SlackApi slackReports = null;
    public SlackApi slackStaffRequests = null;
    public SlackApi slackDonations = null;
    public SlackApi slackPunishments = null;
    public SlackApi slackErrorReporting = null;
    @Getter
    private HashSet<PlayerData> playerData = new HashSet<>();
    @Getter
    private ArrayList<PlayerData> allPlayerData = new ArrayList<>();
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
    private List<String> autoMessages = new ArrayList<>();
    @Getter
    private HashMap<UUID, UUID> messagers = new HashMap<>();
    @Getter
    private ArrayList<Location> fireworkBlocks = new ArrayList<>();
    @Getter
    private ArrayList<Location> launchers = new ArrayList<>();

    public static void playSound(Player player, EventSound eventSound) {
        player.playSound(player.getLocation(), eventSound.getSound1(), eventSound.getVol1(), eventSound.getPtch1());
        player.playSound(player.getLocation(), eventSound.getSound2(), eventSound.getVol2(), eventSound.getPtch2());
    }

    public void onEnable() {
        instance = this;
        registerCommands();
        registerListeners();

        sql = new MySQL(this);
        protocolManager = ProtocolLibrary.getProtocolManager();

        // Save Default Configuration
        saveDefaultConfig();

        // Reload player data on reload
        for (Player player : getServer().getOnlinePlayers()) {
            PlayerData playerData = getPlayerData(player.getUniqueId());
            ScoreboardListener scoreboardListener = new ScoreboardListener(this);
            KDRatio kdRatio = new KDRatio(this);
            //GlowAPI.setGlowing(player, null, getServer().getOnlinePlayers());
            scoreboardListener.getRanks().put(player.getUniqueId(), playerData.getRank().getColor() + "" + ChatColor.BOLD + playerData.getRank().getName().toUpperCase());
            scoreboardListener.getKills().put(player.getUniqueId(), playerData.getKills());
            scoreboardListener.getDeaths().put(player.getUniqueId(), playerData.getDeaths());
            scoreboardListener.getKds().put(player.getUniqueId(), ChatColor.GRAY + "" + kdRatio.getRatio(player));
            scoreboardListener.getSouls().put(player.getUniqueId(), playerData.getSouls());
            scoreboardListener.getCoins().put(player.getUniqueId(), playerData.getCoins());
            scoreboardListener.updateScoreboardKills(player, 0);
            scoreboardListener.updateScoreboardRank(player, playerData.getRank());
            scoreboardListener.updateScoreboardDeaths(player, 0);
            scoreboardListener.updateScoreboardSouls(player, 0);
            scoreboardListener.updateScoreboardCoins(player, 0);
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
            BossBarAPI.removeAllBars(player);
        }

        reloadPunishments();
        reloadAllPlayerData();

        // Initialize Various Repeating Tasks
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoUpdate(this), 120, 120);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new DonationUpdater(this), 0, 20);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new TrailRunnable(this), 0, 2);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HelixRunnable(this), 0, 5);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new AFKRunnable(this), 0, 20);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new PunishmentRunnable(this), 0, 20L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new WorldParticlesRunnable(this), 0, 2L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new MessageRunnable(this), 0, 6000L);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> sql.checkConnection(), 0, 72000L);

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
        slackDonations = new SlackApi("https://hooks.slack.com/services/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");
        slackPunishments = new SlackApi("https://hooks.slack.com/services/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");
        slackErrorReporting = new SlackApi("https://hooks.slack.com/services/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");

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
        launchers.add(new Location(getServer().getWorld("Colosseum"), 8, 27.05, 0));
        launchers.add(new Location(getServer().getWorld("Colosseum"), 6, 27.05, 6));
        launchers.add(new Location(getServer().getWorld("Colosseum"), 0, 27.05, 8));
        launchers.add(new Location(getServer().getWorld("Colosseum"), -8, 27.05, 0));
        launchers.add(new Location(getServer().getWorld("Colosseum"), -6, 27.05, -6));
        launchers.add(new Location(getServer().getWorld("Colosseum"), 0, 27.05, -8));
        launchers.add(new Location(getServer().getWorld("Colosseum"), 6, 27.05, -6));
        launchers.add(new Location(getServer().getWorld("Colosseum"), -6, 27.05, 6));

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
                                playSound(player, EventSound.INVENTORY_OPEN_MENU);
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
        Optional<PlayerData> playerDataStream = playerData.stream().filter(playerData -> playerData.getName().equalsIgnoreCase(name)).findFirst();

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
        getServer().getScheduler().runTaskLater(this, () -> playerData.add(sql.getPlayerData(uuid)), 5L);
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

    public void createEssenceData(UUID uuid, Essence.Type type) {
        sql.executeUpdate(Query.CREATE_ESSENCE_DATA, uuid.toString(), type.toString(), 1);
        getServer().getScheduler().runTaskLater(this, () -> getEssenceData(type).put(uuid, 1), 5L);
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

    public void reloadPunishments() {
        if (!sql.getAllPunishments().isEmpty()) {
            for (Punishment punishment : sql.getAllPunishments()) {
                playerPunishments.put(punishment.getUuid(), sql.getAllPunishments(punishment.getUuid()));
            }
        }
    }

    public void reloadAllPlayerData() {
        if (!sql.getAllPlayerData().isEmpty()) {
            for (PlayerData playerData : sql.getAllPlayerData()) {
                allPlayerData.add(playerData);
            }
        }
    }

    public void sendNoPermission(Player player) {
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Sorry! " + ChatColor.GRAY + "You aren't allowed to use this command!");
        playSound(player, EventSound.ACTION_FAIL);
    }

    public void sendIncorrectUsage(Player player, String msg) {
        player.sendMessage(BoldColor.RED.getColor() + "Oops! " + ChatColor.GRAY + "Try this: " + ChatColor.RED + msg);
        playSound(player, EventSound.ACTION_FAIL);
    }

    public void warnPlayer(Player player, Player target, Punishment.Reason reason) {
        if (!WarnCommand.getWarned().containsKey(target.getUniqueId())) {
            WarnCommand.getWarned().put(target.getUniqueId(), 1);
        } else {
            WarnCommand.getWarned().put(target.getUniqueId(), WarnCommand.getWarned().get(target.getUniqueId()) + 1);
        }
        int warns = WarnCommand.getWarned().get(target.getUniqueId());
        if (WarnCommand.getWarned().get(target.getUniqueId()) == 5) {
            getServer().getOnlinePlayers().stream().filter(players ->
                    getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.sendMessage(
                            BoldColor.DARK_RED.getColor() + " [ARES] " + ChatColor.GOLD + target.getName() + BoldColor.DARK_RED.getColor() + " (5)"
                                    + ChatColor.RED + "was " + (reason.getType().equals(Punishment.Type.MUTE) || reason.getType().equals(Punishment.Type.ALL) ? "muted" : "kicked")
                                    + " for " + ChatColor.GRAY + reason.getName() + (reason.getType().equals(Punishment.Type.MUTE)
                                    || reason.getType().equals(Punishment.Type.ALL) ? " for " + ChatColor.GRAY + Time.toString(reason.getLength() * 1000, true) : "")));
            getServer().getOnlinePlayers().stream().filter(players ->
                    getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.playSound(players.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1));
            player.closeInventory();

            if (reason.getType().equals(Punishment.Type.MUTE) || reason.getType().equals(Punishment.Type.ALL)) {
                HashMap<Punishment.Reason, Integer> punishment = new HashMap<>();
                punishment.put(reason, reason.getLength());
                MuteCommand.mutePlayer(target.getUniqueId(), player, punishment);
            } else {
                HashMap<Punishment.Reason, Integer> punishment = new HashMap<>();
                punishment.put(reason, -1);
                KickCommand.kickPlayer(target.getUniqueId(), player, punishment);
            }
            return;
        }
        if (WarnCommand.getWarned().get(target.getUniqueId()) >= 3) {
            getServer().getOnlinePlayers().stream().filter(players ->
                    getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.sendMessage(
                            BoldColor.DARK_RED.getColor() + " !!! " + ChatColor.GRAY + player.getName() + ChatColor.RED + " warned "
                                    + ChatColor.GOLD + target.getName() + " (" + warns + ")" + ChatColor.RED + " for " + ChatColor.GRAY + reason.getName()));
            getServer().getOnlinePlayers().stream().filter(players ->
                    getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                    .forEach(players -> players.playSound(players.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1));
            player.closeInventory();
            return;
        }
        player.closeInventory();
        getServer().getOnlinePlayers().stream().filter(players ->
                getPlayerData(players.getUniqueId()).hasRank(Rank.HELPER))
                .forEach(players -> players.sendMessage(
                        ChatColor.GRAY + player.getName() + ChatColor.RED + " warned "
                                + ChatColor.GOLD + target.getName() + " (" + warns + ")" + ChatColor.RED + " for " + ChatColor.GRAY + reason.getName()));
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
        return (getOne50Essence().get(player.getUniqueId()) + getOne100Essence().get(player.getUniqueId()) + getOne150Essence().get(player.getUniqueId())
                + getThree50Essence().get(player.getUniqueId()) + getThree100Essence().get(player.getUniqueId()) + getThree150Essence().get(player.getUniqueId())
                + getSix50Essence().get(player.getUniqueId()) + getSix100Essence().get(player.getUniqueId()) + getSix150Essence().get(player.getUniqueId()));
    }

    // Reloads a Custom Configuration File
    public void reloadCustomConfig(File file, String configName) {
        if (file == null) {
            file = new File(getDataFolder(), configName + ".yml");
        }
        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(file);

        // Look for defaults in the jar
        InputStream defConfigStream = this.getResource(configName + ".yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }
}
