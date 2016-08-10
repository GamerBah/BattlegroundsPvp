package me.gamerbah.Administration.Punishments;/* Created by GamerBah on 8/8/2016 */

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Punishment {
    private UUID playerUUID;
    private PunishType type;
    private long time;
    private long expiration;
    private UUID enforcerUUID;
    private String reason;

    public enum PunishType {
        BAN,
        MUTE,
        KICK
    }
}
