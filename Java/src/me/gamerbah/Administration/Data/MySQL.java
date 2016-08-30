package me.gamerbah.Administration.Data;
/* Created by GamerBah on 7/6/2016 */

import me.gamerbah.Administration.Donations.Essence;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Trails.Trail;
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
                        result.getBoolean("dailyReward"), result.getBoolean("teamRequests"), result.getBoolean("privateMessaging"), result.getBoolean("stealthyJoin"),
                        Trail.Type.valueOf(result.getString("trail")), LocalDateTime.parse(result.getString("dailyRewardLast")), result.getString("ownedKits"));
            }
            result.getStatement().close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get the player data!");
            e.printStackTrace();
        }
        return null;
    }

    public PlayerData getPlayerData(String name) {
        try (ResultSet result = executeQuery(Query.GET_PLAYER_DATA_FROM_UUID, name)) {
            if (result.next()) {
                return new PlayerData(result.getInt("id"), UUID.fromString(result.getString("uuid")),
                        result.getString("name"), Rank.valueOf(result.getString("rank")), result.getInt("kills"), result.getInt("deaths"), result.getInt("souls"), result.getInt("coins"),
                        result.getBoolean("dailyReward"), result.getBoolean("teamRequests"), result.getBoolean("privateMessaging"), result.getBoolean("stealthyJoin"),
                        Trail.Type.valueOf(result.getString("trail")), LocalDateTime.parse(result.getString("dailyRewardLast")), result.getString("ownedKits"));
            }
            result.getStatement().close();
        } catch (SQLException e) {
            plugin.getLogger().severe("Uh oh! Unable to get the player data!");
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
