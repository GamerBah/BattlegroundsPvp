package me.gamerbah.Data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Query {
    
    // PLAYER DATA
    CREATE_PLAYER("INSERT IGNORE INTO players (uuid, name) VALUES (?, ?)"),
    GET_PLAYER("SELECT * FROM players WHERE uuid = ?"),
    UPDATE_PLAYER_NAME("UPDATE players SET name = ? WHERE id = ?"),
    UPDATE_PLAYER_RANK("UPDATE players SET rank = ? WHERE id = ?"),
    UPDATE_PLAYER_KILLS("UPDATE players SET kills = ? WHERE id = ?"),
    UPDATE_PLAYER_DEATHS("UPDATE players SET deaths = ? WHERE id = ?"),
    UPDATE_PLAYER_CHALLENGES("UPDATE players SET challenges = ? WHERE id = ?"),
    UPDATE_PLAYERS_ACHIEVEMENTS("UPDATE players SET achievements = ? WHERE id = ?"),
    UPDATE_PLAYER_DAILY_REWARD("UPDATE players SET dailyReward = ? WHERE id = ?"),

    // PUNISHMENT DATA
    CREATE_PUNISHMENT("INSERT IGNORE INTO punishments (uuid, name) VALUES (?, ?)"),
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
