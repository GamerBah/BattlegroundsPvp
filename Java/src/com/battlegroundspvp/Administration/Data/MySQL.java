package com.battlegroundspvp.Administration.Data;
/* Created by GamerBah on 7/6/2016 */

import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Administration.Punishments.Punishment;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Cosmetic;
import org.bukkit.entity.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class MySQL {

    private Battlegrounds plugin;
    private Connection connection = null;

    public MySQL(Battlegrounds plugin) {
        this.plugin = plugin;
        String host = plugin.getConfig().getString("host");
        String database = plugin.getConfig().getString("database");
        String username = plugin.getConfig().getString("username");
        String password = plugin.getConfig().getString("password");
        String url = "jdbc:mysql://" + host + "/" + database;

        try {
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                plugin.getLogger().info("Successfully connected to the database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("Failed to connect to the database! Check that the host and database names are correct, as well as username and password!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

    }

    public boolean isConnected() {
        final String CHECK_SQL_QUERY = "SELECT 1";
        boolean isConnected = false;
        try {
            final PreparedStatement statement = connection.prepareStatement(CHECK_SQL_QUERY);
            isConnected = true;
            statement.closeOnCompletion();
        } catch (SQLException | NullPointerException e) {
            try {
                String host = plugin.getConfig().getString("host");
                String database = plugin.getConfig().getString("database");
                String username = plugin.getConfig().getString("username");
                String password = plugin.getConfig().getString("password");
                String url = "jdbc:mysql://" + host + "/" + database;
                connection = DriverManager.getConnection(url, username, password);
                if (connection != null) {
                    plugin.getLogger().info("Reconnected to the database...");
                    isConnected = true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                plugin.getLogger().severe("Failed to reconnect to the database! Check that the host and database names are correct, as well as username and password!");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
        return isConnected;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not close MySQL connection!");
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        try (ResultSet result = executeQuery(Query.GET_PLAYER_DATA_FROM_UUID, uuid.toString())) {
            if (result.next()) {
                return new PlayerData(result.getInt("id"), UUID.fromString(result.getString("uuid")),
                        result.getString("name"), Rank.valueOf(result.getString("rank")), result.getInt("kills"), result.getInt("deaths"), result.getInt("souls"), result.getInt("coins"),
                        result.getInt("killstreaksEnded"), result.getInt("revengeKills"), result.getInt("highestKillstreak"), result.getInt("playersRecruited"), result.getInt("recruitedBy"),
                        result.getBoolean("dailyReward"), result.getBoolean("teamRequests"), result.getBoolean("privateMessaging"), result.getBoolean("stealthyJoin"),
                        Cosmetic.Item.valueOf(result.getString("trail")), LocalDateTime.parse(result.getString("dailyRewardLast")), LocalDateTime.parse(result.getString("lastOnline")),
                        result.getString("ownedKits"), result.getString("lastKilledBy"), result.getString("title"), result.getString("friends"));
            }
            result.getStatement().close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get the player data!");
            e.printStackTrace();
        }
        return null;
    }

    public PlayerData getPlayerData(String name) {
        try (ResultSet result = executeQuery(Query.GET_PLAYER_DATA_FROM_NAME, name)) {
            if (result.next()) {
                return new PlayerData(result.getInt("id"), UUID.fromString(result.getString("uuid")),
                        result.getString("name"), Rank.valueOf(result.getString("rank")), result.getInt("kills"), result.getInt("deaths"), result.getInt("souls"), result.getInt("coins"),
                        result.getInt("killstreaksEnded"), result.getInt("revengeKills"), result.getInt("highestKillstreak"), result.getInt("playersRecruited"), result.getInt("recruitedBy"),
                        result.getBoolean("dailyReward"), result.getBoolean("teamRequests"), result.getBoolean("privateMessaging"), result.getBoolean("stealthyJoin"),
                        Cosmetic.Item.valueOf(result.getString("trail")), LocalDateTime.parse(result.getString("dailyRewardLast")), LocalDateTime.parse(result.getString("lastOnline")),
                        result.getString("ownedKits"), result.getString("lastKilledBy"), result.getString("title"), result.getString("friends"));
            }
            result.getStatement().close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get the player data!");
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<PlayerData> getAllPlayerData() {
        try (ResultSet result = executeQuery(Query.GET_ALL_PLAYER_DATA)) {
            ArrayList<PlayerData> playerDatas = new ArrayList<>();
            while (result.next()) {
                playerDatas.add(new PlayerData(result.getInt("id"), UUID.fromString(result.getString("uuid")),
                        result.getString("name"), Rank.valueOf(result.getString("rank")), result.getInt("kills"), result.getInt("deaths"), result.getInt("souls"), result.getInt("coins"),
                        result.getInt("killstreaksEnded"), result.getInt("revengeKills"), result.getInt("highestKillstreak"), result.getInt("playersRecruited"), result.getInt("recruitedBy"),
                        result.getBoolean("dailyReward"), result.getBoolean("teamRequests"), result.getBoolean("privateMessaging"), result.getBoolean("stealthyJoin"),
                        Cosmetic.Item.valueOf(result.getString("trail")), LocalDateTime.parse(result.getString("dailyRewardLast")), LocalDateTime.parse(result.getString("lastOnline")),
                        result.getString("ownedKits"), result.getString("lastKilledBy"), result.getString("title"), result.getString("friends")));
            }
            result.getStatement().close();
            return playerDatas;
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get all player data");
            e.printStackTrace();
        }
        return null;
    }

    public int getEssenceAmount(Player player, Essence.Type type) {
        try (ResultSet result = executeQuery(Query.GET_ESSENCE_AMOUNT, player.getUniqueId().toString(), type.toString())) {
            if (result.next()) {
                return result.getInt("amount");
            }
            result.getStatement().close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get the essence data!");
            e.printStackTrace();
        }
        return 0;
    }

    public Punishment getPunishment(UUID uuid, Punishment.Type type, LocalDateTime date) {
        try (ResultSet result = executeQuery(Query.GET_PUNISHMENT, uuid.toString(), type.toString(), date.toString())) {
            if (result.next()) {
                return new Punishment(result.getInt("id"), UUID.fromString(result.getString("uuid")), result.getString("name"), Punishment.Type.valueOf(result.getString("type")),
                        LocalDateTime.parse(result.getString("date")), result.getInt("duration"), LocalDateTime.parse(result.getString("expiration")), UUID.fromString(result.getString("enforcer")),
                        Punishment.Reason.valueOf(result.getString("reason")), result.getBoolean("pardoned"));
            }
            result.getStatement().close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get the punishment!");
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Punishment> getAllPunishments(UUID uuid) {
        try (ResultSet result = executeQuery(Query.GET_ALL_PUNISHMENTS, uuid.toString())) {
            ArrayList<Punishment> punishments = new ArrayList<>();
            while (result.next()) {
                punishments.add(new Punishment(result.getInt("id"), UUID.fromString(result.getString("uuid")), result.getString("name"), Punishment.Type.valueOf(result.getString("type")),
                        LocalDateTime.parse(result.getString("date")), result.getInt("duration"), LocalDateTime.parse(result.getString("expiration")), UUID.fromString(result.getString("enforcer")),
                        Punishment.Reason.valueOf(result.getString("reason")), result.getBoolean("pardoned")));
            }
            result.getStatement().close();
            return punishments;
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get punishments!");
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Punishment> getAllPunishments() {
        try (ResultSet result = executeQuery(Query.GET_PUNISHMENTS)) {
            ArrayList<Punishment> punishments = new ArrayList<>();
            while (result.next()) {
                punishments.add(new Punishment(result.getInt("id"), UUID.fromString(result.getString("uuid")), result.getString("name"), Punishment.Type.valueOf(result.getString("type")),
                        LocalDateTime.parse(result.getString("date")), result.getInt("duration"), LocalDateTime.parse(result.getString("expiration")), UUID.fromString(result.getString("enforcer")),
                        Punishment.Reason.valueOf(result.getString("reason")), result.getBoolean("pardoned")));
            }
            result.getStatement().close();
            return punishments;
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get punishments!");
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
        if (isConnected()) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
                    int i = 1;
                    for (Object parameter : parameters) {
                        preparedStatement.setObject(i++, parameter);
                    }
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    plugin.getLogger().severe("Could not execute MySQL query: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Executes a MySQL query (NOT ASYNCHRONOUS)
     *
     * @param query MySQL query to execute
     * @return Result of MySQL query
     */
    public ResultSet executeQuery(Query query, Object... parameters) {
        if (isConnected()) {
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
        return null;
    }
}
