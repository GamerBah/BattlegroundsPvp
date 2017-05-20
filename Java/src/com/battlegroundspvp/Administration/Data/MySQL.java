package com.battlegroundspvp.Administration.Data;
/* Created by GamerBah on 7/6/2016 */

import com.battlegroundspvp.Administration.Data.Player.KitPvpData;
import com.battlegroundspvp.Administration.Data.Player.PlayerData;
import com.battlegroundspvp.Administration.Data.Player.PlayerSettings;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.Cosmetic;
import com.battlegroundspvp.Utils.Enums.ParticleQuality;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class MySQL {

    private static HikariDataSource dataSource = null;
    private Battlegrounds plugin;

    public MySQL(Battlegrounds plugin) {
        this.plugin = plugin;
        String host = plugin.getConfig().getString("host");
        String db = plugin.getConfig().getString("database");
        String user = plugin.getConfig().getString("username");
        String pass = plugin.getConfig().getString("password");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + "/" + db + "?useSSL=false");
        config.setUsername(user);
        config.setPassword(pass);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setLeakDetectionThreshold(3000);
        config.setMaximumPoolSize(100);
        try {
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Unable to start HikariCP data source");
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to close MySQL connection!");
        }
    }

    public void disconnect() {
        try {
            dataSource.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataSource.close();
    }

    public PlayerData getPlayerData(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_PLAYER_DATA_FROM_UUID, uuid.toString())) {
                if (result != null) {
                    if (result.next()) {
                        PlayerData playerData = new PlayerData(
                                result.getInt("id"),
                                UUID.fromString(result.getString("uuid")),
                                result.getString("name"),
                                Rank.valueOf(result.getString("rank")),
                                result.getInt("coins"),
                                result.getInt("playersRecruited"),
                                result.getInt("recruitedBy"),
                                result.getBoolean("dailyReward"),
                                Cosmetic.Item.valueOf(result.getString("trail")),
                                Cosmetic.Item.valueOf(result.getString("warcry")),
                                Cosmetic.Item.valueOf(result.getString("gore")),
                                LocalDateTime.parse(result.getString("dailyRewardLast")),
                                LocalDateTime.parse(result.getString("lastOnline")),
                                result.getString("friends"),
                                result.getString("cosmetics"),
                                null, null, null);
                        playerData.setKitPvpData(getKitPvpData(playerData));
                        playerData.setPlayerSettings(getPlayerSettings(playerData));
                        playerData.setEssenceData(getEssenceData(playerData));
                        result.getStatement().close();
                        closeConnection(connection);
                        return playerData;
                    }
                    result.getStatement().close();
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get the player data!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public PlayerData getPlayerData(String name) {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_PLAYER_DATA_FROM_NAME, name)) {
                if (result != null) {
                    if (result.next()) {
                        PlayerData playerData = new PlayerData(
                                result.getInt("id"),
                                UUID.fromString(result.getString("uuid")),
                                result.getString("name"),
                                Rank.valueOf(result.getString("rank")),
                                result.getInt("coins"),
                                result.getInt("playersRecruited"),
                                result.getInt("recruitedBy"),
                                result.getBoolean("dailyReward"),
                                Cosmetic.Item.valueOf(result.getString("trail")),
                                Cosmetic.Item.valueOf(result.getString("warcry")),
                                Cosmetic.Item.valueOf(result.getString("gore")),
                                LocalDateTime.parse(result.getString("dailyRewardLast")),
                                LocalDateTime.parse(result.getString("lastOnline")),
                                result.getString("friends"),
                                result.getString("cosmetics"),
                                null, null, null);
                        playerData.setKitPvpData(getKitPvpData(playerData));
                        playerData.setPlayerSettings(getPlayerSettings(playerData));
                        playerData.setEssenceData(getEssenceData(playerData));
                        result.getStatement().close();
                        closeConnection(connection);
                        return playerData;
                    }
                    result.getStatement().close();
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get the player data!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<PlayerData> getAllPlayerData() {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_ALL_PLAYER_DATA)) {
                ArrayList<PlayerData> playerDatas = new ArrayList<>();
                if (result != null) {
                    while (result.next()) {
                        PlayerData playerData = new PlayerData(
                                result.getInt("id"),
                                UUID.fromString(result.getString("uuid")),
                                result.getString("name"),
                                Rank.valueOf(result.getString("rank")),
                                result.getInt("coins"),
                                result.getInt("playersRecruited"),
                                result.getInt("recruitedBy"),
                                result.getBoolean("dailyReward"),
                                Cosmetic.Item.valueOf(result.getString("trail")),
                                Cosmetic.Item.valueOf(result.getString("warcry")),
                                Cosmetic.Item.valueOf(result.getString("gore")),
                                LocalDateTime.parse(result.getString("dailyRewardLast")),
                                LocalDateTime.parse(result.getString("lastOnline")),
                                result.getString("friends"),
                                result.getString("cosmetics"),
                                null, null, null);
                        playerData.setKitPvpData(getKitPvpData(playerData));
                        playerData.setPlayerSettings(getPlayerSettings(playerData));
                        playerData.setEssenceData(getEssenceData(playerData));
                        playerDatas.add(playerData);
                    }
                    result.getStatement().close();
                    closeConnection(connection);
                    return playerDatas;
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get all player data");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private KitPvpData getKitPvpData(PlayerData playerData) {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_KITPVP_DATA, playerData.getId())) {
                if (result != null) {
                    if (result.next()) {
                        KitPvpData kitPvpData = new KitPvpData(
                                result.getInt("id"),
                                result.getInt("kills"),
                                result.getInt("deaths"),
                                result.getInt("souls"),
                                result.getInt("combatRating"),
                                result.getInt("killstreaksEnded"),
                                result.getInt("revengeKills"),
                                result.getInt("highestKillstreak"),
                                result.getString("playersRated"),
                                result.getString("ownedKits"),
                                result.getString("lastKilledBy"),
                                result.getString("title")
                        );
                        result.getStatement().close();
                        closeConnection(connection);
                        return kitPvpData;
                    }
                    result.getStatement().close();
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get KitPvpData!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private PlayerSettings getPlayerSettings(PlayerData playerData) {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_SETTINGS_DATA, playerData.getId())) {
                if (result != null) {
                    if (result.next()) {
                        PlayerSettings playerSettings = new PlayerSettings(
                                result.getInt("id"),
                                result.getBoolean("teamRequests"),
                                result.getBoolean("privateMessaging"),
                                result.getBoolean("stealthyJoin"),
                                ParticleQuality.valueOf(result.getString("particleQuality"))
                        );
                        result.getStatement().close();
                        closeConnection(connection);
                        return playerSettings;
                    }
                    result.getStatement().close();
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get PlayerSettings!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public EssenceData getEssenceData(PlayerData playerData) {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_ESSENCE_DATA, playerData.getId())) {
                if (result != null) {
                    if (result.next()) {
                        EssenceData essenceData = new EssenceData(
                                result.getInt("id"),
                                UUID.fromString(result.getString("uuid")),
                                result.getInt("1_50"),
                                result.getInt("1_100"),
                                result.getInt("1_150"),
                                result.getInt("3_50"),
                                result.getInt("3_100"),
                                result.getInt("3_150"),
                                result.getInt("6_50"),
                                result.getInt("6_100"),
                                result.getInt("6_150")
                        );
                        result.getStatement().close();
                        closeConnection(connection);
                        return essenceData;
                    }
                    result.getStatement().close();
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get the essence data!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Punishment getPunishment(UUID uuid, Punishment.Type type, LocalDateTime date) {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_PUNISHMENT, uuid.toString(), type.toString(), date.toString())) {
                if (result != null) {
                    if (result.next()) {
                        Punishment punishment = new Punishment(result.getInt("id"), UUID.fromString(result.getString("uuid")), result.getString("name"), Punishment.Type.valueOf(result.getString("type")),
                                LocalDateTime.parse(result.getString("date")), result.getInt("duration"), LocalDateTime.parse(result.getString("expiration")), UUID.fromString(result.getString("enforcer")),
                                Punishment.Reason.valueOf(result.getString("reason")), result.getBoolean("pardoned"));
                        result.getStatement().close();
                        closeConnection(connection);
                        return punishment;
                    }
                    result.getStatement().close();
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get the punishment!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Punishment> getAllPunishments(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_ALL_PUNISHMENTS, uuid.toString())) {
                ArrayList<Punishment> punishments = new ArrayList<>();
                if (result != null) {
                    while (result.next()) {
                        punishments.add(new Punishment(result.getInt("id"), UUID.fromString(result.getString("uuid")), result.getString("name"), Punishment.Type.valueOf(result.getString("type")),
                                LocalDateTime.parse(result.getString("date")), result.getInt("duration"), LocalDateTime.parse(result.getString("expiration")), UUID.fromString(result.getString("enforcer")),
                                Punishment.Reason.valueOf(result.getString("reason")), result.getBoolean("pardoned")));
                    }
                    result.getStatement().close();
                    closeConnection(connection);
                    return punishments;
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get punishments!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Punishment> getAllPunishments() {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_PUNISHMENTS)) {
                ArrayList<Punishment> punishments = new ArrayList<>();
                if (result != null) {
                    while (result.next()) {
                        punishments.add(new Punishment(result.getInt("id"), UUID.fromString(result.getString("uuid")), result.getString("name"), Punishment.Type.valueOf(result.getString("type")),
                                LocalDateTime.parse(result.getString("date")), result.getInt("duration"), LocalDateTime.parse(result.getString("expiration")), UUID.fromString(result.getString("enforcer")),
                                Punishment.Reason.valueOf(result.getString("reason")), result.getBoolean("pardoned")));
                    }
                    result.getStatement().close();
                    closeConnection(connection);
                    return punishments;
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get punishments!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GlobalStats getGlobalStats() {
        try {
            Connection connection = dataSource.getConnection();
            try (ResultSet result = executeQuery(connection, Query.GET_GLOBAL_STATS)) {
                if (result != null) {
                    if (result.next()) {
                        GlobalStats globalStats = new GlobalStats(
                                result.getInt("totalKills"),
                                result.getInt("totalDeaths"),
                                result.getInt("totalSoulsEarned"),
                                result.getInt("totalCoinsEarned"),
                                result.getInt("totalUniqueJoins"),
                                result.getInt("totalMutes"),
                                result.getInt("totalKicks"),
                                result.getInt("totalBans"),
                                result.getInt("totalSuicides"),
                                result.getInt("totalUsedEssences"),
                                result.getInt("totalArrowsFired"),
                                result.getInt("totalEnderpearlsThrown"),
                                result.getInt("totalKillstreaksEnded")
                        );
                        result.getStatement().close();
                        closeConnection(connection);
                        return globalStats;
                    }
                }
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Uh oh! Unable to get global stats!");
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Executes a MySQL update
     *
     * @param query MySQL query to execute
     */
    public void executeUpdate(Query query, Object... parameters) {
        try {
            Connection connection = dataSource.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
                int i = 1;
                for (Object parameter : parameters) {
                    preparedStatement.setObject(i++, parameter);
                }
                preparedStatement.executeUpdate();
                preparedStatement.close();
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Could not execute MySQL query: " + e.getMessage());
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a MySQL update
     *
     * @param query MySQL Query String to execute
     */
    public void executeUpdate(String query, Object... parameters) {
        try {
            Connection connection = dataSource.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int i = 1;
                for (Object parameter : parameters) {
                    preparedStatement.setObject(i++, parameter);
                }
                preparedStatement.executeUpdate();
                preparedStatement.close();
                closeConnection(connection);
            } catch (SQLException e) {
                plugin.getLogger().severe("Could not execute MySQL query: " + e.getMessage());
                e.printStackTrace();
            }
            closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a MySQL query (NOT ASYNCHRONOUS)
     *
     * @param connection Connection to use
     * @param query      MySQL query to execute
     * @return Result of MySQL query
     */
    private ResultSet executeQuery(Connection connection, Query query, Object... parameters) {
        if (connection == null) {
            plugin.getLogger().severe("Could not execute MySQL query: Connection is null");
            return null;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            int i = 1;
            for (Object parameter : parameters) {
                preparedStatement.setObject(i++, parameter);
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not execute MySQL query: " + e.getMessage());
            return null;
        }
    }
}
