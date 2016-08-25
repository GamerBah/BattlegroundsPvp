package me.gamerbah.Administration.Punishments;/* Created by GamerBah on 8/8/2016 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Punishment {
    private final int id;
    private final UUID uuid;
    private final String name;
    private final Type type;
    private final LocalDateTime date;
    private final int duration;
    private final LocalDateTime expiration;
    private final UUID enforcer;
    private final Reason reason;
    private boolean pardoned;

    public enum Type {
        BAN,
        TEMP_BAN,
        MUTE,
        KICK
    }

    @AllArgsConstructor
    @Getter
    public enum Reason {
        SPAM_CAPS("Spam (Caps)", "§7Player is spamming the chat\n§7with fully capitalized words", "Please don't spam the chat with capitalized words!", 1500, Punishment.Type.MUTE, null),
        SPAM_LETTERS("Spam (Letters)", "§7Player is spamming then\n§7chat with random letters", "Please don't spam the chat with random letters!", 900, Punishment.Type.MUTE, null),
        SPAM_GENERIC("Spam (Generic)", "§7Player is spamming the\n§7in some sort of way", "Please don't spam the chat! We want to keep it clean!", 1200, Punishment.Type.MUTE, null),
        SWEARING("Swearing", "§7Player is using profane wornds in\n§7either public chat or private messages", "Please don't swear! There are kids that play on the server!", 1500, Punishment.Type.MUTE, null),
        HARASSMENT("Player Harassment", "§7Player is verbally harassing others\n§7and creating a toxic environment", "Harassment of other players is not tolerated.", 1800, Punishment.Type.TEMP_BAN, Punishment.Type.MUTE),
        GLITCH_ABUSE("Glitch Exploiting", "§7Player was caught exploiting\n§7a glitch with the plugin or arena", "If you find a bug, please report it! Don't use it to your advantage!", 1500, Punishment.Type.KICK, Punishment.Type.TEMP_BAN),
        DISRESPECT("Player Disrespect", "§7Player was disrespecting a\n§7Staff member or other player", "Please treat the Staff and our players with respect.", 1800, Punishment.Type.MUTE, Punishment.Type.TEMP_BAN),
        MODDED_CLIENT("Modded Client", "§7Player was found using a modded\n§7client to gain an advantage", "Hacked Clients are not allowed. Please read our rules before joining.", -1, Punishment.Type.BAN, null),
        MODDED_CLIENT_SUSPECTED("Suspected Modded Client", "§7Player was suspected of\n§7using a modded client", "You were suspected of having a hacked client!", 3600, Punishment.Type.TEMP_BAN, Punishment.Type.BAN);

        private String name;
        private String description;
        private String message;
        private int length;
        private Punishment.Type type1;
        private Punishment.Type type2;
    }
}
