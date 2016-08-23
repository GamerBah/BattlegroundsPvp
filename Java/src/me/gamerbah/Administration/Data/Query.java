package me.gamerbah.Administration.Data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Query {
    
    // PLAYER DATA
    CREATE_PLAYER_DATA("INSERT IGNORE INTO players (uuid, name) VALUES (?, ?)"),
    GET_PLAYER_DATA("SELECT * FROM players WHERE uuid = ?"),
    UPDATE_PLAYER_DATA("UPDATE players SET rank = ?, kills = ?, deaths = ?, souls = ?, coins = ?, trail = ?, dailyReward = ?, teamRequests = ?, privateMessaging = ?, stealthyJoin = ?, challenges = ?, achievements = ?, essences = ? WHERE id = ?"),
    UPDATE_PLAYER_NAME("UPDATE players SET name = ? WHERE id = ?"),
    UPDATE_PLAYER_RANK("UPDATE players SET rank = ? WHERE id = ?"),
    UPDATE_PLAYER_KILLS("UPDATE players SET kills = ? WHERE id = ?"),
    UPDATE_PLAYER_DEATHS("UPDATE players SET deaths = ? WHERE id = ?"),
    UPDATE_PLAYER_SOULS("UPDATE players SET souls = ? WHERE id = ?"),
    UPDATE_PLAYER_COINS("UPDATE players SET coins = ? WHERE id = ?"),
    UPDATE_PLAYER_CHALLENGES("UPDATE players SET challenges = ? WHERE id = ?"),
    UPDATE_PLAYERS_ACHIEVEMENTS("UPDATE players SET achievements = ? WHERE id = ?"),
    UPDATE_PLAYER_DAILY_REWARD("UPDATE players SET dailyReward = ? WHERE id = ?"),
    UPDATE_PLAYER_TEAM_REQUESTS("UPDATE players SET teamRequests = ? WHERE id = ?"),
    UPDATE_PLAYER_PRIVATE_MESSAGING("UPDATE players SET privateMessaging = ? WHERE id = ?"),
    UPDATE_PLAYER_STEALTHY_JOIN("UPDATE players SET stealthyJoin = ? WHERE id = ?"),
    UPDATE_PLAYER_TRAIL("UPDATE players SET trail = ? WHERE id = ?"),

    // DONATION DATA
    CREATE_ESSENCE_DATA("INSERT INTO essences (uuid, type, amount) VALUES (?, ?, ?)"),
    UPDATE_ESSENCE_AMOUNT("UPDATE essences SET amount = ? WHERE uuid = ? AND type = ?"),
    GET_ESSENCE_AMOUNT("SELECT * FROM essences WHERE uuid = ? AND type = ?"),
    DELETE_ESSENCE_DATA("DELETE FROM essences WHERE uuid = ? AND type = ?"),

    // PUNISHMENT DATA
    CREATE_PUNISHMENT("INSERT IGNORE INTO punishment (uuid, name, type, time, expiration, enforcerUUID, reason) VALUES (?, ?, ?, ?, ?, ?, ?)"),
    GET_PUNISHMENT("SELECT * FROM punishment WHERE uuid = ?"),
    UPDATE_PUNISHMENT_TIME("UPDATE punishment SET expiration = ? WHERE uuid = ? AND type = ? AND time = ?"),
    
    // GLOBAL STATS
    CREATE_STATS("INSERT IGNORE INTO stats (type) VALUES (?)"),
    GET_STATS("SELECT * FROM stats WHERE type = `?`"),
    UPDATE_UNIQUE_JOINS("UPDATE stats SET unique = ? WHERE type = `?`"),
    UPDATE_GLOBAL_KILLS("UPDATE stats SET kills = ? WHERE type = `?`"),
    UPDATE_GLOBAL_DEATHS("UPDATE stats SET deaths = ? WHERE type = `?`"),
    UPDATE_CHALLENGES_COMPLETED("UPDATE stats SET challenges = ? WHERE type = `?`");

    private final String query;

    @Override
    public String toString() {
        return query;
    }
}
