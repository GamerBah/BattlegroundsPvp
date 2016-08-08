package me.gamerbah.Data;
/* Created by GamerBah on 6/18/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.gamerbah.Administration.Utils.Rank;
import me.gamerbah.Battlegrounds;

import java.util.UUID;

import static me.gamerbah.Data.Query.*;

@AllArgsConstructor
public class PlayerData {
    @Getter
    private final int id;
    @Getter
    private final UUID uuid;
    @Getter
    private String name, challenges, achievements;
    @Getter
    private Rank rank;
    @Getter
    private int kills, deaths;
    @Getter
    private boolean dailyReward;

    private final MySQL sql = Battlegrounds.getSql();

    public void setName(String name) {
        sql.executeUpdate(UPDATE_PLAYER_NAME, this.name = name, id);
    }

    public void setRank(Rank rank) {
        sql.executeUpdate(UPDATE_PLAYER_RANK, this.rank = rank, id);
    }

    public void setKills(int kills) {
        sql.executeUpdate(UPDATE_PLAYER_KILLS, this.kills = kills, id);
    }

    public void setDeaths(int deaths) {
        sql.executeUpdate(UPDATE_PLAYER_DEATHS, this.deaths = deaths, id);
    }

    public void setChallenges(String challenges) {
        sql.executeUpdate(UPDATE_PLAYER_CHALLENGES, this.challenges = challenges, id);
    }

    public void setAchievements(String achievements) {
        sql.executeUpdate(UPDATE_PLAYERS_ACHIEVEMENTS, this.achievements = achievements, id);
    }

    public void setDailyReward(boolean dailyReward) {
        sql.executeUpdate(UPDATE_PLAYER_CHALLENGES, this.dailyReward = dailyReward, id);
    }
}
