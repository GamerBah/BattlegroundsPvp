package me.gamerbah.Administration.Data;
/* Created by GamerBah on 6/18/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Utils.Trails.Trail;

import java.time.LocalDateTime;
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
    private String name;
    @Getter
    private Rank rank;
    @Getter
    private int kills, deaths, souls, coins;
    @Getter
    private boolean dailyReward, teamRequests, privateMessaging, stealthyJoin;
    @Getter
    private Trail.Type trail;
    @Getter
    private LocalDateTime dailyRewardLast;
    @Getter
    private String ownedKits;

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

    public void setTrail(Trail.Type trail) {
        sql.executeUpdate(UPDATE_PLAYER_TRAIL, trail.toString(), id);
        this.trail = trail;
    }

    public void setDailyRewardLast(LocalDateTime date) {
        sql.executeUpdate(UPDATE_PLAYER_DAILY_REWARD_LAST, date.toString().replace(" ", "T"), id);
        this.dailyRewardLast = date;
    }

    public void setOwnedKits(String list) {
        sql.executeUpdate(UPDATE_PLAYER_OWNED_KITS, this.ownedKits = list, id);
    }
}
