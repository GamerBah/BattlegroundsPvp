package me.gamerbah.Data;
/* Created by GamerBah on 7/6/2016 */

import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;

import java.sql.*;
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

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not close MySQL connection!");
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        try (ResultSet result = executeQuery(Query.GET_PLAYER, uuid.toString())) {
            if (result.next()) {
                PlayerData playerData = new PlayerData(result.getInt("id"), UUID.fromString(result.getString("uuid")),
                        result.getString("name"), result.getString("challenges"), result.getString("achievements"),
                        Rank.valueOf(result.getString("rank")), result.getInt("kills"), result.getInt("deaths"), result.getInt("souls"), result.getInt("coins"),
                        result.getBoolean("dailyReward"), new ArrayList<>());

                ResultSet punishments = executeQuery(Query.GET_PUNISHMENT, uuid.toString());
                if (punishments != null && punishments.next()) {
                    String name = playerData.getName();
                    Punishment.PunishType type = Punishment.PunishType.valueOf(punishments.getString("type"));
                    long time = punishments.getLong("time");
                    long expiration = punishments.getLong("expiration");
                    UUID enforcerUUID = UUID.fromString(punishments.getString("enforcerUUID"));
                    String reason = punishments.getString("reason");
                    playerData.getPunishments().add(new Punishment(uuid, name, type, time, expiration, enforcerUUID, reason));
                }
                return playerData;
            }
            result.getStatement().close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not get player data!");
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

    /**
     * Executes a MySQL query (NOT ASYNCHRONOUS)
     *
     * @param query MySQL query to execute
     * @return Result of MySQL query
     */
    public ResultSet executeQuery(Query query, Object... parameters) {
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
