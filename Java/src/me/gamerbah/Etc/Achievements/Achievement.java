package me.gamerbah.Etc.Achievements;
/* Created by GamerBah on 9/4/2016 */


import lombok.AllArgsConstructor;
import lombok.Getter;
import me.gamerbah.Battlegrounds;
import me.gamerbah.Listeners.ScoreboardListener;
import me.gamerbah.Utils.Messages.BoldColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
@Getter
public enum Achievement {

    COMBAT(BoldColor.RED.getColor()),
    CHALLENGE(BoldColor.YELLOW.getColor()),
    COLLECTION(BoldColor.GREEN.getColor()),
    RECRUITMENT(BoldColor.PURPLE.getColor());

    private String color;

    public static Type getTypeFromName(String string) {
        for (Type type : Type.values()) {
            if (type.getName().equals(string)) {
                return type;
            }
        }
        return null;
    }

    public static void sendUnlockMessage(Player player, Type type) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.25F, 1F);
        Bukkit.getServer().getScheduler().runTaskLater(Battlegrounds.getInstance(), () -> {
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_TWINKLE, 0.875F, 1.2F);
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 0.9F, 1.1F);
        }, 15L);
        player.sendMessage(" ");
        player.sendMessage(AQUA + "\u00AB " + WHITE + "" + STRIKETHROUGH + "----------------------" + AQUA + " \u00BB");
        player.sendMessage(BoldColor.GOLD.getColor() + "  ACHIEVEMENT UNLOCKED!");
        BaseComponent component = new TextComponent(" \n" + type.getGroup().getColor() + "  " + type.getName() + "\n");
        component.addExtra(new TextComponent(BoldColor.GREEN.getColor() + "  \u2713 " + GRAY + type.getDescription()));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(AQUA + "You can view this achievement\n"
                + AQUA + "in your Player Profile!").create()));
        player.spigot().sendMessage(component);
        player.sendMessage(" ");
        player.sendMessage(YELLOW + "  Rewards:\n" + (type.getSoulReward() == 0 ? "" : GRAY + "    \u25CF " + BoldColor.AQUA.getColor() + type.getSoulReward() + " Souls\n")
                + (type.getCoinReward() == 0 ? "" : GRAY + "    \u25CF " + BoldColor.PINK.getColor() + type.getCoinReward() + " Battle Coins"));
        player.sendMessage(AQUA + "\u00AB " + WHITE + "" + STRIKETHROUGH + "----------------------" + AQUA + " \u00BB");
        player.sendMessage(" ");
        ScoreboardListener scoreboardListener = new ScoreboardListener(Battlegrounds.getInstance());
        scoreboardListener.updateScoreboardSouls(player, type.getSoulReward());
        scoreboardListener.updateScoreboardCoins(player, type.getCoinReward());
    }

    @AllArgsConstructor
    @Getter
    public enum Type {
        BRUTALITY_I(COMBAT, "Brutality I", "Kill 100 Players", 100, 150, 25, null),
        BRUTALITY_II(COMBAT, "Brutality II", "Kill 500 Players", 500, 300, 100, null),
        BRUTALITY_III(COMBAT, "Brutality III", "Kill 1,000 Players", 1000, 600, 250, null),
        BRUTALITY_IV(COMBAT, "Brutality IV", "Kill 2,500 Players", 2500, 1200, 500, null),
        BRUTALITY_V(COMBAT, "Brutality V", "Kill 5,000 Players", 5000, 2400, 1000, null),
        BRUTALITY_MASTER(COMBAT, "Brutality Mastery", "Kill 12,500 Players", 12500, 6000, 3250, "Killer"),

        VENGEFUL_I(COMBAT, "Vengeful I", "Get revenge on 10 players", 10, 100, 20, null),
        VENGEFUL_II(COMBAT, "Vengeful II", "Get revenge on 30 players", 30, 300, 60, null),
        VENGEFUL_III(COMBAT, "Vengeful III", "Get revenge on 90 players", 90, 900, 180, null),
        VENGEFUL_IV(COMBAT, "Vengeful IV", "Get revenge on 180 players", 180, 1800, 260, null),
        VENGEFUL_V(COMBAT, "Vengeful V", "Get revenge on 300 players", 300, 3000, 520, null),
        VENGEFUL_MASTER(COMBAT, "Vengeful Mastery", "Get revenge on 500 players", 500, 5000, 600, "Vengeful"),

        BUZZKILL_I(COMBAT, "Buzzkill I", "End 1 Killstreak", 1, 150, 10, null),
        BUZZKILL_II(COMBAT, "Buzzkill II", "End 3 Killstreaks", 3, 310, 20, null),
        BUZZKILL_III(COMBAT, "Buzzkill III", "End 7 Killstreaks", 7, 500, 40, null),
        BUZZKILL_IV(COMBAT, "Buzzkill IV", "End 15 Killstreaks", 15, 750, 80, null),
        BUZZKILL_V(COMBAT, "Buzzkill V", "End 25 Killstreaks", 25, 1000, 150, null),
        BUZZKILL_MASTER(COMBAT, "Buzzkill Mastery", "End 35 Killstreaks", 35, 1500, 225, "Buzzkill"),

        SADIST_I(COMBAT, "Sadist I", "Go on a 5 killstreak", 5, 30, 15, null),
        SADIST_II(COMBAT, "Sadist II", "Go on a 10 killstreak", 10, 65, 35, null),
        SADIST_III(COMBAT, "Sadist III", "Go on a 15 killstreak", 15, 100, 50, null),
        SADIST_IV(COMBAT, "Sadist IV", "Go on a 20 killstreak", 20, 145, 90, null),
        SADIST_V(COMBAT, "Sadist V", "Go on a 25 killstreak", 25, 200, 135, null),
        SADIST_MASTER(COMBAT, "Sadist Mastery", "Go on a 30 killstreak", 30, 350, 200, "Sadist"),

        DEATHKNELL(COMBAT, "Deathknell", "Die a total of 1000 times", 1000, 999, 99, "Fragile"),

        CHALLENGER_I(CHALLENGE, "Challenger I", "Complete a Daily Challenge", 1, 100, 10, null),
        CHALLENGER_II(CHALLENGE, "Challenger II", "Complete 5 Daily Challenges", 5, 200, 25, null),
        CHALLENGER_III(CHALLENGE, "Challenger II", "Complete 15 Daily Challenges", 15, 300, 40, null),
        CHALLENGER_IV(CHALLENGE, "Challenger IV", "Complete 35 Daily Challenges", 35, 400, 55, null),
        CHALLENGER_V(CHALLENGE, "Challenger V", "Complete 75 Daily Challenges", 75, 500, 75, null),
        CHALLENGER_MASTER(CHALLENGE, "Challenger Mastery", "Complete 150 Daily Challenges", 150, 750, 125, null),

        RECRUITER_I(RECRUITMENT, "Recruiter I", "Recruit a friend", 1, 50, 10, null),
        RECRUITER_II(RECRUITMENT, "Recruiter II", "Recruit 5 friends", 5, 75, 25, null),
        RECRUITER_III(RECRUITMENT, "Recruiter III", "Recruit 10 friends", 10, 150, 50, null),
        RECRUITER_IV(RECRUITMENT, "Recruiter IV", "Recruit 15 friends", 15, 300, 75, null),
        RECRUITER_V(RECRUITMENT, "Recruiter V", "Recruit 20 friends", 20, 500, 125, null),
        RECRUITER_MASTER(RECRUITMENT, "Recruiter Mastery", "Recruit 30 friends", 30, 1000, 200, "Ambassador"),

        ARMAMENT_I(COLLECTION, "Armament I", "Unlock 9 Kits", 9, 0, 50, null),
        ARMAMENT_II(COLLECTION, "Armament II", "Unlock 18 Kits", 18, 0, 100, null),
        ARMAMENT_III(COLLECTION, "Armament III", "Unlock 27 Kits", 27, 0, 200, null),
        ARMAMENT_IV(COLLECTION, "Armament IV", "Unlock 36 Kits", 36, 0, 350, null),
        ARMAMENT_V(COLLECTION, "Armament V", "Unlock 45 Kits", 45, 0, 500, null),
        ARMAMENT_MASTER(COLLECTION, "Armament Mastery", "Unlock all 54 Kits", 54, 0, 750, "Arms Master"),

        SHOWMANSHIP_I(COLLECTION, "Showmanship I", "Unlock 1 Particle Pack", 1, 75, 0, null),
        SHOWMANSHIP_II(COLLECTION, "Showmanship II", "Unlock 3 Particle Packs", 3, 150, 0, null),
        SHOWMANSHIP_III(COLLECTION, "Showmanship III", "Unlock 6 Particle Packs", 6, 350, 0, null),
        SHOWMANSHIP_IV(COLLECTION, "Showmanship IV", "Unlock 9 Particle Packs", 9, 600, 0, null),
        SHOWMANSHIP_V(COLLECTION, "Showmanship V", "Unlock 12 Particle Packs", 12, 850, 0, null),
        SHOWMANSHIP_MASTER(COLLECTION, "Showmanship Mastery", "Unlock all Particle Packs", 18, 1150, 0, "Showman"),

        WARCRY_I(COLLECTION, "Warcry I", "Unlock 1 Warcry", 1, 75, 0, null),
        WARCRY_II(COLLECTION, "Warcry II", "Unlock 3 Warcries", 3, 150, 0, null),
        WARCRY_III(COLLECTION, "Warcry III", "Unlock 6 Warcries", 6, 350, 0, null),
        WARCRY_IV(COLLECTION, "Warcry IV", "Unlock 9 Warcries", 9, 600, 0, null),
        WARCRY_V(COLLECTION, "Warcry V", "Unlock 12 Warcries", 12, 850, 0, null),
        WARCRY_MASTER(COLLECTION, "Warcry Mastery", "Unlock all Warcries", 18, 1150, 0, "Warcry"),

        SAVAGE_I(COLLECTION, "Savage I", "Unlock 1 Gore Effect", 1, 75, 0, null),
        SAVAGE_II(COLLECTION, "Savage II", "Unlock 5 Gore Effects", 5, 150, 0, null),
        SAVAGE_III(COLLECTION, "Savage III", "Unlock 10 Gore Effects", 10, 350, 0, null),
        SAVAGE_IV(COLLECTION, "Savage IV", "Unlock 15 Gore Effects", 15, 600, 0, null),
        SAVAGE_V(COLLECTION, "Savage V", "Unlock 20 Gore Effects", 20, 850, 0, null),
        SAVAGE_MASTER(COLLECTION, "Savage Mastery", "Unlock all Gore Effects", 27, 1150, 0, "Savage");

        private Achievement group;
        private String name;
        private String description;
        private int requirement;
        private int soulReward;
        private int coinReward;
        private String title;
    }

}
