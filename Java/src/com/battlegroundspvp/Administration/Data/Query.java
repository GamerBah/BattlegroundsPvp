package com.battlegroundspvp.Administration.Data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Query {

    // PLAYER DATA
    CREATE_PLAYER_DATA("INSERT IGNORE INTO players (uuid, name) VALUES (?, ?)"),
    GET_PLAYER_DATA_FROM_UUID("SELECT * FROM players WHERE uuid = ?"),
    GET_PLAYER_DATA_FROM_NAME("SELECT * FROM players WHERE name = ?"),
    GET_ALL_PLAYER_DATA("SELECT * FROM players"),
    // -- CORE
    UPDATE_PLAYER_NAME("UPDATE players SET name = ? WHERE id = ?"),
    UPDATE_PLAYER_RANK("UPDATE players SET rank = ? WHERE id = ?"),
    UPDATE_PLAYERS_ACHIEVEMENTS("UPDATE players SET achievements = ? WHERE id = ?"),
    UPDATE_PLAYER_DAILY_REWARD("UPDATE players SET dailyReward = ? WHERE id = ?"),
    UPDATE_PLAYER_TRAIL("UPDATE players SET trail = ? WHERE id = ?"),
    UPDATE_PLAYER_WARCRY("UPDATE players SET warcry = ? WHERE id = ?"),
    UPDATE_PLAYER_GORE("UPDATE players SET gore = ? WHERE id = ?"),
    UPDATE_PLAYER_DAILY_REWARD_LAST("UPDATE players SET dailyRewardLast = ? WHERE id = ?"),
    UPDATE_PLAYER_PLAYERS_RECRUITED("UPDATE players SET playersRecruited = ? WHERE id = ?"),
    UPDATE_PLAYER_RECRUITED_BY("UPDATE players SET recruitedBy = ? WHERE id = ?"),
    UPDATE_PLAYER_LAST_ONLINE("UPDATE players SET lastOnline = ? WHERE id = ?"),
    UPDATE_PLAYER_FRIENDS("UPDATE players SET friends = ? WHERE id = ?"),
    UPDATE_PLAYER_OWNED_COSMETICS("UPDATE players SET cosmetics = ? WHERE id = ?"),
    UPDATE_PLAYER_COINS("UPDATE players SET coins = ? WHERE id = ?"),
    // -- KIT PVP
    CREATE_KITPVP_DATA("INSERT IGNORE INTO kitpvp_data (id) VALUE (?)"),
    GET_KITPVP_DATA("SELECT * FROM kitpvp_data WHERE id = ?"),
    UPDATE_KITPVP_KILLS("UPDATE kitpvp_data SET kills = ? WHERE id = ?"),
    UPDATE_KITPVP_DEATHS("UPDATE kitpvp_data SET deaths = ? WHERE id = ?"),
    UPDATE_KITPVP_SOULS("UPDATE kitpvp_data SET souls = ? WHERE id = ?"),
    UPDATE_KITPVP_OWNED_KITS("UPDATE kitpvp_data SET ownedKits = ? WHERE id = ?"),
    UPDATE_KITPVP_LAST_KILLED_BY("UPDATE kitpvp_data SET lastKilledBy = ? WHERE id = ?"),
    UPDATE_KITPVP_KILLSTREAKS_ENDED("UPDATE kitpvp_data SET killstreaksEnded = ? WHERE id = ?"),
    UPDATE_KITPVP_REVENGE_KILLS("UPDATE kitpvp_data SET revengeKills = ? WHERE id = ?"),
    UPDATE_KITPVP_HIGHEST_KILLSTREAK("UPDATE kitpvp_data SET highestKillstreak = ? WHERE id = ?"),
    UPDATE_KITPVP_TITLE("UPDATE kitpvp_data SET title = ? WHERE id = ?"),
    // -- PLAYER SETTINGS
    CREATE_SETTINGS_DATA("INSERT IGNORE INTO player_settings (id) VALUE (?)"),
    GET_SETTINGS_DATA("SELECT * FROM player_settings WHERE id = ?"),
    UPDATE_SETTINGS_TEAM_REQUESTS("UPDATE player_settings SET teamRequests = ? WHERE id = ?"),
    UPDATE_SETTINGS_PRIVATE_MESSAGING("UPDATE player_settings SET privateMessaging = ? WHERE id = ?"),
    UPDATE_SETTINGS_STEALTHY_JOIN("UPDATE player_settings SET stealthyJoin = ? WHERE id = ?"),
    UPDATE_SETTINGS_PARTICLE_QUALITY("UPDATE player_settings SET cosmetics = ? WHERE id = ?"),
    UPDATE_SETTINGS_INSTANT_RESPAWN("UPDATE player_settings SET instantRespawn = ? WHERE id = ?"),

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
    GET_GLOBAL_STATS("SELECT * FROM global_stats"),
    UPDATE_GLOBAL_KILLS("UPDATE global_stats SET totalKills = ?"),
    UPDATE_GLOBAL_DEATHS("UPDATE global_stats SET totalDeaths = ?"),
    UPDATE_GLOBAL_SOULS("UPDATE global_stats SET totalSoulsEarned = ?"),
    UPDATE_GLOBAL_COINS("UPDATE global_stats SET totalCoinsEarned = ?"),
    UPDATE_GLOBAL_JOINS("UPDATE global_stats SET totalUniqueJoins = ?"),
    UPDATE_GLOBAL_MUTES("UPDATE global_stats SET totalMutes = ?"),
    UPDATE_GLOBAL_KICKS("UPDATE global_stats SET totalKicks = ?"),
    UPDATE_GLOBAL_BANS("UPDATE global_stats SET totalBans = ?"),
    UPDATE_GLOBAL_SUICIDES("UPDATE global_stats SET totalSuicides = ?"),
    UPDATE_GLOBAL_USED_ESSENCES("UPDATE global_stats SET totalUsedEssences = ?"),
    UPDATE_GLOBAL_ARROWS_FIRED("UPDATE global_stats SET totalArrowsFired = ?"),
    UPDATE_GLOBAL_PEARLS_THROWN("UPDATE global_stats SET totalEnderpearlsThrown = ?"),
    UPDATE_GLOBAL_STREAKS_ENDED("UPDATE global_stats SET totalKillstreaksEnded = ?");

    private final String query;

    @Override
    public String toString() {
        return query;
    }
}
