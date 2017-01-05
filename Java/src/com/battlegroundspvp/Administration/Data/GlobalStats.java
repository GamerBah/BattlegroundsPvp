package com.battlegroundspvp.Administration.Data;
/* Created by GamerBah on 1/3/2017 */

import com.battlegroundspvp.Battlegrounds;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.battlegroundspvp.Administration.Data.Query.*;

@AllArgsConstructor
public class GlobalStats {
    private final MySQL sql = Battlegrounds.getSql();

    @Getter
    private int totalKills, totalDeaths, totalSoulsEarned, totalCoinsEarned, totalUniqueJoins, totalMutes, totalKicks, totalBans, totalSuicides, totalUsedEssences, totalArrowsFired, totalEnderpearlsThrown, totalKillstreaksEnded;

    public void addKill() {
        sql.executeUpdate(UPDATE_GLOBAL_KICKS, this.totalKills += 1);
    }

    public void addDeath() {
        sql.executeUpdate(UPDATE_GLOBAL_DEATHS, this.totalDeaths += 1);
    }

    public void addSouls(int amount) {
        sql.executeUpdate(UPDATE_GLOBAL_SOULS, this.totalSoulsEarned += amount);
    }

    public void addCoins(int amount) {
        sql.executeUpdate(UPDATE_GLOBAL_COINS, this.totalCoinsEarned += amount);
    }

    public void addUniqueJoin() {
        sql.executeUpdate(UPDATE_GLOBAL_JOINS, this.totalUniqueJoins += 1);
    }

    public void addMute() {
        sql.executeUpdate(UPDATE_GLOBAL_MUTES, this.totalMutes += 1);
    }

    public void addKick() {
        sql.executeUpdate(UPDATE_GLOBAL_KICKS, this.totalKicks += 1);
    }

    public void addBan() {
        sql.executeUpdate(UPDATE_GLOBAL_BANS, this.totalBans += 1);
    }

    public void addSuicide() {
        sql.executeUpdate(UPDATE_GLOBAL_SUICIDES, this.totalSuicides += 1);
    }

    public void addUsedEssence() {
        sql.executeUpdate(UPDATE_GLOBAL_USED_ESSENCES, this.totalUsedEssences += 1);
    }

    public void addArrowFired() {
        sql.executeUpdate(UPDATE_GLOBAL_ARROWS_FIRED, this.totalArrowsFired += 1);
    }

    public void addEnderpearlThrown() {
        sql.executeUpdate(UPDATE_GLOBAL_PEARLS_THROWN, this.totalEnderpearlsThrown += 1);
    }

    public void addKillstreakEnded() {
        sql.executeUpdate(UPDATE_GLOBAL_STREAKS_ENDED, this.totalKillstreaksEnded += 1);
    }

}
