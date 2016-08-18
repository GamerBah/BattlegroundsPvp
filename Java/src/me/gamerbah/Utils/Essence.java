package me.gamerbah.Utils;/* Created by GamerBah on 8/17/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Data.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Essence {
    private Battlegrounds plugin;

    public Essence(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    private static Type typeFromName(String string) {
        for (Type type : Type.values()) {
            if (type.getDisplayName().equals(string)) {
                return type;
            }
        }
        return null;
    }

    public static ArrayList<Type> convertToArray(String list) {
        ArrayList<Type> essences = new ArrayList<>();
        if (list == null) {
            return essences;
        }
        char charArray[] = list.toCharArray();
        int length = list.length();
        int amount = 0;
        for (int i = 0; i < length; i++) {
            char c = charArray[i];
            if (c == ',') {
                amount++;
            }
        }
        String split[] = list.split(",");
        for (int i = 0; i < amount; i++) {
            essences.add(typeFromName(split[i]));
        }
        return essences;
    }

    public int getEssenceAmount(Player player) {
        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());
        char charArray[] = playerData.getEssences().toCharArray();
        int length = playerData.getEssences().length();
        int amount = 0;
        for (int i = 0; i < length; i++) {
            char c = charArray[i];
            if (c == ',') {
                amount++;
            }
        }
        return amount;
    }

    @AllArgsConstructor
    @Getter
    public enum Type {
        ONE_HOUR_50_PERCENT("1 Hour (+50%)", 1, 50, ChatColor.GREEN),
        ONE_HOUR_100_PERCENT("1 Hour (+100%)", 1, 100, ChatColor.GREEN),
        ONE_HOUR_150_PERCENT("1 Hour (+150%)", 1, 150, ChatColor.GREEN),
        THREE_HOUR_50_PERCENT("3 Hour (+50%)", 3, 50, ChatColor.AQUA),
        THREE_HOUR_100_PERCENT("3 Hour (+100%)", 3, 100, ChatColor.AQUA),
        THREE_HOUR_150_PERCENT("3 Hour (+150%)", 3, 150, ChatColor.AQUA),
        SIX_HOUR_50_PERCENT("6 Hour (+50%)", 6, 50, ChatColor.LIGHT_PURPLE),
        SIX_HOUR_100_PERCENT("6 Hour (+100%)", 6, 100, ChatColor.LIGHT_PURPLE),
        SIX_HOUR_150_PERCENT("6 Hour (+150%)", 6, 150, ChatColor.LIGHT_PURPLE);

        private String displayName;
        private int time;
        private int increase;
        private ChatColor color;
    }
}
