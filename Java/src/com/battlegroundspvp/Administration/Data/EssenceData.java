package com.battlegroundspvp.Administration.Data;
/* Created by GamerBah on 8/22/2016 */


import com.battlegroundspvp.Administration.Donations.Essence;
import com.battlegroundspvp.Battlegrounds;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public class EssenceData {
    private final MySQL sql = Battlegrounds.getSql();

    @Getter
    private final int id;
    @Getter
    private final UUID uuid;
    @Getter
    private int one50, one100, one150, three50, three100, three150, six50, six100, six150;

    public void addEssence(Essence.Type type) {
        if (type.equals(Essence.Type.ONE_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one50 += 1, id);
        if (type.equals(Essence.Type.ONE_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one100 += 1, id);
        if (type.equals(Essence.Type.ONE_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one150 += 1, id);
        if (type.equals(Essence.Type.THREE_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three50 += 1, id);
        if (type.equals(Essence.Type.THREE_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three100 += 1, id);
        if (type.equals(Essence.Type.THREE_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three150 += 1, id);
        if (type.equals(Essence.Type.SIX_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six50 += 1, id);
        if (type.equals(Essence.Type.SIX_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six100 += 1, id);
        if (type.equals(Essence.Type.SIX_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six150 += 1, id);
    }

    public void addEssence(Essence.Type type, int amount) {
        if (type.equals(Essence.Type.ONE_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one50 += amount, id);
        if (type.equals(Essence.Type.ONE_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one100 += amount, id);
        if (type.equals(Essence.Type.ONE_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one150 += amount, id);
        if (type.equals(Essence.Type.THREE_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three50 += amount, id);
        if (type.equals(Essence.Type.THREE_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three100 += amount, id);
        if (type.equals(Essence.Type.THREE_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three150 += amount, id);
        if (type.equals(Essence.Type.SIX_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six50 += amount, id);
        if (type.equals(Essence.Type.SIX_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six100 += amount, id);
        if (type.equals(Essence.Type.SIX_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six150 += amount, id);
    }

    public void removeEssence(Essence.Type type) {
        if (type.equals(Essence.Type.ONE_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one50 -= 1, id);
        if (type.equals(Essence.Type.ONE_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one100 -= 1, id);
        if (type.equals(Essence.Type.ONE_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.one150 -= 1, id);
        if (type.equals(Essence.Type.THREE_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three50 -= 1, id);
        if (type.equals(Essence.Type.THREE_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three100 -= 1, id);
        if (type.equals(Essence.Type.THREE_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.three150 -= 1, id);
        if (type.equals(Essence.Type.SIX_HOUR_50_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six50 -= 1, id);
        if (type.equals(Essence.Type.SIX_HOUR_100_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six100 -= 1, id);
        if (type.equals(Essence.Type.SIX_HOUR_150_PERCENT))
            sql.executeUpdate(Query.UPDATE_ESSENCE_AMOUNT(type), this.six150 -= 1, id);
    }
}
