package me.gamerbah.Administration.Data;
/* Created by GamerBah on 6/18/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.gamerbah.Administration.Punishments.Punishment;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Trails.Trail;

import java.util.List;
import java.util.UUID;

import static me.gamerbah.Administration.Data.Query.*;

@AllArgsConstructor
public class PlayerData {
    @Getter
    private final int id;
    @Getter
    private final UUID uuid;
    private final MySQL sql = Battlegrounds.getSql();
    @Getter
    private String name, challenges, achievements, essences;
    @Getter
    private Rank rank;
    @Getter
    private int kills, deaths, souls, coins;
    @Getter
    private boolean dailyReward, teamRequests, privateMessaging, stealthyJoin;
    @Getter
    private Trail.Type trail;
    @Getter
    private List<Punishment> punishments;

    public void setName(String name) {
        sql.executeUpdate(UPDATE_PLAYER_NAME, this.name = name, id);
    }

    public void setRank(Rank rank) {
        sql.executeUpdate(UPDATE_PLAYER_RANK, rank.toString(), id);
        this.rank = rank;
    }

    public boolean hasRank(Rank rank) {
        return this.rank.getLevel() >= rank.getLevel();
    }

    public void setKills(int kills) {
        sql.executeUpdate(UPDATE_PLAYER_KILLS, this.kills = kills, id);
    }

    public void setDeaths(int deaths) {
        sql.executeUpdate(UPDATE_PLAYER_DEATHS, this.deaths = deaths, id);
    }

    public void setSouls(int souls) {
        sql.executeUpdate(UPDATE_PLAYER_SOULS, this.souls = souls, id);
    }

    public void setCoins(int coins) {
        sql.executeUpdate(UPDATE_PLAYER_COINS, this.coins = coins, id);
    }

    public void setChallenges(String challenges) {
        sql.executeUpdate(UPDATE_PLAYER_CHALLENGES, this.challenges = challenges, id);
    }

    public void setAchievements(String achievements) {
        sql.executeUpdate(UPDATE_PLAYERS_ACHIEVEMENTS, this.achievements = achievements, id);
    }

    public void setDailyReward(boolean dailyReward) {
        sql.executeUpdate(UPDATE_PLAYER_DAILY_REWARD, this.dailyReward = dailyReward, id);
    }

    public void setTeamRequests(boolean teamRequests) {
        sql.executeUpdate(UPDATE_PLAYER_TEAM_REQUESTS, this.teamRequests = teamRequests, id);
    }

    public void setPrivateMessaging(boolean privateMessaging) {
        sql.executeUpdate(UPDATE_PLAYER_PRIVATE_MESSAGING, this.privateMessaging = privateMessaging, id);
    }

    public void setStealthyJoin(boolean stealthyJoin) {
        sql.executeUpdate(UPDATE_PLAYER_STEALTHY_JOIN, this.stealthyJoin = stealthyJoin, id);
    }

    public void setEssences(String essences) {
        sql.executeUpdate(UPDATE_PLAYER_ESSENCES, this.essences = essences, id);
    }

    public void setTrail(Trail.Type trail) {
        sql.executeUpdate(UPDATE_PLAYER_TRAIL, trail.toString(), id);
        this.trail = trail;
    }

    public void addPunishment(Punishment punishment) {
        punishments.add(punishment);
        sql.executeUpdate(Query.CREATE_PUNISHMENT, uuid.toString(), name, punishment.getType().name(), punishment.getTime(),
                punishment.getExpiration(), punishment.getEnforcerUUID().toString(), punishment.getReason());
    }

    public void removePunishment(Punishment punishment) {
        punishments.remove(punishment);
        sql.executeUpdate(Query.UPDATE_PUNISHMENT_TIME, 0, uuid.toString(), punishment.getType().name(), punishment.getTime());
    }
}
