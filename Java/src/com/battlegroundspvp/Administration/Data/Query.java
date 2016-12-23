package com.battlegroundspvp.Administration.Data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Query {
    
    // PLAYER DATA
    CREATE_PLAYER_DATA("INSERT IGNORE INTO players (uuid, name) VALUES (?, ?)"),
    GET_PLAYER_DATA_FROM_UUID("SELECT * FROM players WHERE uuid = ?"),
    GET_PLAYER_DATA_FROM_NAME("SELECT * FROM players WHERE name = ?"),
    GET_ALL_PLAYER_DATA("SELECT * FROM players"),
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
    UPDATE_PLAYER_WARCRY("UPDATE players SET warcry = ? WHERE id = ?"),
    UPDATE_PLAYER_GORE("UPDATE players SET gore = ? WHERE id = ?"),
    UPDATE_PLAYER_DAILY_REWARD_LAST("UPDATE players SET dailyRewardLast = ? WHERE id = ?"),
    UPDATE_PLAYER_OWNED_KITS("UPDATE players SET ownedKits = ? WHERE id = ?"),
    UPDATE_PLAYER_LAST_KILLED_BY("UPDATE players SET lastKilledBy = ? WHERE id = ?"),
    UPDATE_PLAYER_KILLSTREAKS_ENDED("UPDATE players SET killstreaksEnded = ? WHERE id = ?"),
    UPDATE_PLAYER_REVENGE_KILLS("UPDATE players SET revengeKills = ? WHERE id = ?"),
    UPDATE_PLAYER_HIGHEST_KILLSTREAK("UPDATE players SET highestKillstreak = ? WHERE id = ?"),
    UPDATE_PLAYER_TITLE("UPDATE players SET title = ? WHERE id = ?"),
    UPDATE_PLAYER_PLAYERS_RECRUITED("UPDATE players SET playersRecruited = ? WHERE id = ?"),
    UPDATE_PLAYER_RECRUITED_BY("UPDATE players SET recruitedBy = ? WHERE id = ?"),
    UPDATE_PLAYER_LAST_ONLINE("UPDATE players SET lastOnline = ? WHERE id = ?"),
    UPDATE_PLAYER_FRIENDS("UPDATE players SET friends = ? WHERE id = ?"),
    UPDATE_PLAYER_OWNED_COSMETICS("UPDATE players SET cosmetics = ? WHERE id = ?"),
    UPDATE_PLAYER_PARTICLE_QUALITY("UPDATE players SET cosmetics = ? WHERE id = ?"),

    // DONATION DATA
    CREATE_ESSENCE_DATA("INSERT INTO essences (uuid, type, amount) VALUES (?, ?, ?)"),
    UPDATE_ESSENCE_AMOUNT("UPDATE essences SET amount = ? WHERE uuid = ? AND type = ?"),
    GET_ESSENCE_AMOUNT("SELECT * FROM essences WHERE uuid = ? AND type = ?"),
    DELETE_ESSENCE_DATA("DELETE FROM essences WHERE uuid = ? AND type = ?"),

    // PUNISHMENT DATA
    CREATE_PUNISHMENT("INSERT INTO punishment (uuid, name, type, date, duration, expiration, enforcer, reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"),
    UPDATE_PUNISHMENT_PARDONED("UPDATE punishment SET pardoned = ? WHERE uuid = ? AND type = ? AND date = ?"),
    GET_PUNISHMENT("SELECT * FROM punishment WHERE uuid = ? AND type = ? AND date = ?"),
    GET_ALL_PUNISHMENTS("SELECT * FROM punishment WHERE uuid = ?"),
    GET_PUNISHMENTS("SELECT * FROM punishment"),
    REMOVE_PUNISHMENT("DELETE FROM punishment WHERE uuid = ? AND type = ? AND date = ?"),
    
    // GLOBAL STATS
    GET_STATS("SELECT * FROM stats WHERE type = `?`"),
    UPDATE_UNIQUE_JOINS("UPDATE stats SET unique = ? WHERE type = `?`");

    private final String query;

    @Override
    public String toString() {
        return query;
    }
}
