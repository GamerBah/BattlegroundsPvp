package com.battlegroundspvp.Administration.Data;
/* Created by GamerBah on 6/18/2016 */

import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.Cosmetic;
import com.battlegroundspvp.Utils.Enums.ParticleQuality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.battlegroundspvp.Administration.Data.Query.*;

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
    private int kills, deaths, souls, coins, killstreaksEnded, revengeKills, highestKillstreak, playersRecruited, recruitedBy;
    @Getter
    private boolean dailyReward, teamRequests, privateMessaging, stealthyJoin, instantRespawn;
    @Getter
    private Cosmetic.Item trail, warcry, gore;
    @Getter
    private LocalDateTime dailyRewardLast, lastOnline;
    @Getter
    private String ownedKits, lastKilledBy, title, friends, ownedCosmetics;
    @Getter
    private ParticleQuality particleQuality;

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

    public void setTrail(Cosmetic.Item trail) {
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

    public void setLastKilledBy(Player player) {
        sql.executeUpdate(UPDATE_PLAYER_LAST_KILLED_BY, this.lastKilledBy = player.getUniqueId().toString(), id);
    }

    public void setLastKilledBy(String uuid) {
        sql.executeUpdate(UPDATE_PLAYER_LAST_KILLED_BY, this.lastKilledBy = uuid, id);
    }

    public void setKillstreaksEnded(int amount) {
        sql.executeUpdate(UPDATE_PLAYER_KILLSTREAKS_ENDED, this.killstreaksEnded = amount, id);
    }

    public void setRevengeKills(int amount) {
        sql.executeUpdate(UPDATE_PLAYER_REVENGE_KILLS, this.revengeKills = amount, id);
    }

    public void setHighestKillstreak(int amount) {
        sql.executeUpdate(UPDATE_PLAYER_HIGHEST_KILLSTREAK, this.highestKillstreak = amount, id);
    }

    public void setTitle(String masteryTitle) {
        sql.executeUpdate(UPDATE_PLAYER_TITLE, this.title = masteryTitle, id);
    }

    public void setPlayersRecruited(int amount) {
        sql.executeUpdate(UPDATE_PLAYER_PLAYERS_RECRUITED, this.playersRecruited = amount, id);
    }

    public void setRecruitedBy(int recruiterId) {
        sql.executeUpdate(UPDATE_PLAYER_RECRUITED_BY, this.recruitedBy = recruiterId, id);
    }

    public void setLastOnline(LocalDateTime date) {
        sql.executeUpdate(UPDATE_PLAYER_LAST_ONLINE, date.toString().replace(" ", "T"), id);
        this.lastOnline = date;
    }

    public void setFriends(String friends) {
        sql.executeUpdate(UPDATE_PLAYER_FRIENDS, this.friends = friends, id);
    }

    public void setOwnedCosmetics(String list) {
        sql.executeUpdate(UPDATE_PLAYER_OWNED_COSMETICS, this.ownedCosmetics = list, id);
    }

    public void setWarcry(Cosmetic.Item warcry) {
        sql.executeUpdate(UPDATE_PLAYER_WARCRY, warcry.toString(), id);
        this.warcry = warcry;
    }

    public void setGore(Cosmetic.Item gore) {
        sql.executeUpdate(UPDATE_PLAYER_GORE, gore.toString(), id);
        this.gore = gore;
    }

    public void setParticleQuality(ParticleQuality particleQuality) {
        sql.executeUpdate(UPDATE_PLAYER_PARTICLE_QUALITY, particleQuality.toString(), id);
        this.particleQuality = particleQuality;
    }

    public void setInstantRespawn(boolean instantRespawn) {
        sql.executeUpdate(UPDATE_PLAYER_STEALTHY_JOIN, this.instantRespawn = instantRespawn, id);
    }
}
