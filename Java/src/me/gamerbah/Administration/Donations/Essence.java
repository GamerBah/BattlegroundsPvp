package me.gamerbah.Administration.Donations;/* Created by GamerBah on 8/17/2016 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.gamerbah.Administration.Data.EssenceData;
import me.gamerbah.Battlegrounds;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class Essence {
    private Battlegrounds plugin;

    public Essence(Battlegrounds plugin) {
        this.plugin = plugin;
    }

    public static Type typeFromName(String string) {
        for (Type type : Type.values()) {
            if (type.getDisplayName().equals(string)) {
                return type;
            }
        }
        return null;
    }

    public void activateEssence(Player player, Type type) {
        plugin.getConfig().set("essenceActive", true);
        plugin.getConfig().set("essenceOwner", player.getName());
        plugin.getConfig().set("essenceIncrease", type.getIncrease());
        plugin.getConfig().set("essenceTimeRemaining", type.getTime() * 60 * 60);
        plugin.saveConfig();
        EssenceData essenceData = new EssenceData(plugin);
        int amount = plugin.getEssenceData(type).get(player.getUniqueId());
        if (amount == 1) {
            essenceData.removeEssence(player, type);
        } else {
            essenceData.setEssence(player, type, plugin.getEssenceData(type).get(player.getUniqueId()) - 1);
        }
    }

    void removeActiveEssence() {
        plugin.getConfig().set("essenceActive", false);
        plugin.getConfig().set("essenceOwner", "");
        plugin.getConfig().set("essenceIncrease", "");
        plugin.getConfig().set("essenceTimeRemaining", "");
        plugin.getConfig().set("essenceThanks", "");
        plugin.saveConfig();
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
