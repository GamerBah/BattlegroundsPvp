package com.battlegroundspvp.Administration.Data.Player;
/* Created by GamerBah on 1/3/2017 */

import com.battlegroundspvp.Administration.Data.MySQL;
import com.battlegroundspvp.Battlegrounds;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import static com.battlegroundspvp.Administration.Data.Query.*;

@AllArgsConstructor
public class KitPvpData {
    private final MySQL sql = Battlegrounds.getSql();

    @Getter
    private final int id;
    @Getter
    private int kills, deaths, souls, combatRating, killstreaksEnded, revengeKills, highestKillstreak;
    @Getter
    private String playersRated, ownedKits, lastKilledBy, title;

    public void addKill(int amount) {
        sql.executeUpdate(UPDATE_KITPVP_KILLS, this.kills += amount, id);
    }

    public void addDeath(int amount) {
        sql.executeUpdate(UPDATE_KITPVP_DEATHS, this.deaths += amount, id);
    }

    public void addSouls(int amount) {
        sql.executeUpdate(UPDATE_KITPVP_SOULS, this.souls += amount, id);
    }

    public void addCombatRating() {
        sql.executeUpdate(UPDATE_KITPVP_COMBAT_RATING, this.combatRating += 1, id);
    }

    public void addPlayerRated(int playerId) {
        sql.executeUpdate(UPDATE_KITPVP_PLAYERS_RATED, this.playersRated + playerId + ",", id);
        this.playersRated = playersRated + playerId + ",";
    }

    public void setOwnedKits(String list) {
        sql.executeUpdate(UPDATE_KITPVP_OWNED_KITS, this.ownedKits = list, id);
    }

    public void setLastKilledBy(Player player) {
        sql.executeUpdate(UPDATE_KITPVP_LAST_KILLED_BY, this.lastKilledBy = player.getUniqueId().toString(), id);
    }

    public void setLastKilledBy(String uuid) {
        sql.executeUpdate(UPDATE_KITPVP_LAST_KILLED_BY, this.lastKilledBy = uuid, id);
    }

    public void addKillstreakEnded() {
        sql.executeUpdate(UPDATE_KITPVP_KILLSTREAKS_ENDED, this.killstreaksEnded += 1, id);
    }

    public void addRevengeKill() {
        sql.executeUpdate(UPDATE_KITPVP_REVENGE_KILLS, this.revengeKills += 1, id);
    }

    public void setHighestKillstreak(int amount) {
        sql.executeUpdate(UPDATE_KITPVP_HIGHEST_KILLSTREAK, this.highestKillstreak = amount, id);
    }

    public void setTitle(String masteryTitle) {
        sql.executeUpdate(UPDATE_KITPVP_TITLE, this.title = masteryTitle, id);
    }

}
