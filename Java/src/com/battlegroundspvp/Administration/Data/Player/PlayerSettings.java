package com.battlegroundspvp.Administration.Data.Player;
/* Created by GamerBah on 1/3/2017 */


import com.battlegroundspvp.Administration.Data.MySQL;
import com.battlegroundspvp.Battlegrounds;
import com.battlegroundspvp.Utils.Enums.ParticleQuality;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.battlegroundspvp.Administration.Data.Query.*;

@AllArgsConstructor
public class PlayerSettings {
    private final MySQL sql = Battlegrounds.getSql();

    @Getter
    private final int id;
    @Getter
    private boolean teamRequests, privateMessaging, stealthyJoin;
    @Getter
    private ParticleQuality particleQuality;

    public void setTeamRequests(boolean teamRequests) {
        sql.executeUpdate(UPDATE_SETTINGS_TEAM_REQUESTS, this.teamRequests = teamRequests, id);
    }

    public void setPrivateMessaging(boolean privateMessaging) {
        sql.executeUpdate(UPDATE_SETTINGS_PRIVATE_MESSAGING, this.privateMessaging = privateMessaging, id);
    }

    public void setStealthyJoin(boolean stealthyJoin) {
        sql.executeUpdate(UPDATE_SETTINGS_STEALTHY_JOIN, this.stealthyJoin = stealthyJoin, id);
    }

    public void setParticleQuality(ParticleQuality particleQuality) {
        sql.executeUpdate(UPDATE_SETTINGS_PARTICLE_QUALITY, particleQuality.toString(), id);
        this.particleQuality = particleQuality;
    }
}
