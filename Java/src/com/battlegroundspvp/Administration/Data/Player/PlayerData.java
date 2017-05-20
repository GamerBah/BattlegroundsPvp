package com.battlegroundspvp.Administration.Data.Player;
/* Created by GamerBah on 6/18/2016 */

import com.battlegroundspvp.Administration.Data.EssenceData;
import com.battlegroundspvp.Administration.Data.MySQL;
import com.battlegroundspvp.Administration.Utils.Rank;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.Cosmetic;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.battlegroundspvp.Administration.Data.Query.*;

@AllArgsConstructor
public class PlayerData {
    private final MySQL sql = Battlegrounds.getSql();

    @Getter
    private final int id;
    @Getter
    private final UUID uuid;
    @Getter
    private String name;
    @Getter
    private Rank rank;
    @Getter
    private int coins, playersRecruited, recruitedBy;
    @Getter
    private boolean dailyReward;
    @Getter
    private Cosmetic.Item trail, warcry, gore;
    @Getter
    private LocalDateTime dailyRewardLast, lastOnline;
    @Getter
    private String friends, ownedCosmetics;
    @Getter
    private KitPvpData kitPvpData;
    @Getter
    private PlayerSettings playerSettings;
    @Getter
    private EssenceData essenceData;

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

    public void addCoins(int amount) {
        sql.executeUpdate(UPDATE_PLAYER_COINS, this.coins += amount, id);
    }

    public void setDailyReward(boolean dailyReward) {
        sql.executeUpdate(UPDATE_PLAYER_DAILY_REWARD, this.dailyReward = dailyReward, id);
    }

    public void setTrail(Cosmetic.Item trail) {
        sql.executeUpdate(UPDATE_PLAYER_TRAIL, trail.toString(), id);
        this.trail = trail;
    }

    public void setDailyRewardLast(LocalDateTime date) {
        sql.executeUpdate(UPDATE_PLAYER_DAILY_REWARD_LAST, date.toString().replace(" ", "T"), id);
        this.dailyRewardLast = date;
    }

    public void addPlayerRecruited() {
        sql.executeUpdate(UPDATE_PLAYER_PLAYERS_RECRUITED, this.playersRecruited += 1, id);
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

    public void setKitPvpData(KitPvpData kitPvpData) {
        this.kitPvpData = kitPvpData;
    }

    public void setPlayerSettings(PlayerSettings playerSettings) {
        this.playerSettings = playerSettings;
    }

    public void setEssenceData(EssenceData essenceData) {
        this.essenceData = essenceData;
    }
}
